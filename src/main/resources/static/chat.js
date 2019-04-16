var stompClient = null;

function setConnected(connected) {
  $('#connect').prop('disabled', connected);
  $('#disconnect').prop('disabled', !connected);
}

function connect() {
  var username = $('#login').val();
  var password = $('#password').val();

  axios.post('/api/auth/authenticate', { 'username': username, 'password': password })
    .then(function (response) {
      var token = response.headers.authorization.substring(7);
      var socket = new SockJS('/websocket/chat?access_token=' + token);
      stompClient = Stomp.over(socket);
      stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected', frame);
        stompClient.subscribe('/topic/messages', function (data) {
          const parsed = JSON.parse(data.body);
          showMessage(parsed.userLogin, parsed.message);
        });
      });
    })
    .catch(function (error) {
      console.log(error);
    });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log('Disconnected');
}

function sendMessage() {
  if (stompClient !== null) {
    var message = $('#message').val();
    if (message != null && message !== '') {
      var payload = JSON.stringify({ 'message': message });
      stompClient.send('/topic/send', {}, payload);
    }
  }
}

function showMessage(author, message) {
  $('#chat').append('<tr><td>' + author + ': ' + message + '</td></tr>');
}

$(function () {
  $('form').on('submit', function (e) {
    e.preventDefault();
  });
  $('#connect').click(function () { connect(); });
  $('#disconnect').click(function () { disconnect(); });
  $('#send').click(function () { sendMessage(); });

  setConnected(false);
});
