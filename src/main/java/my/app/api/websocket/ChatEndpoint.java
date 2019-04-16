package my.app.api.websocket;

import java.security.Principal;
import java.time.Instant;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import my.app.dto.ChatMessageDto;

@Controller
public class ChatEndpoint implements ApplicationListener<AbstractSubProtocolEvent> {
  public static final String SYSTEM_USER = "system";

  private final SimpMessageSendingOperations messageSender;

  public ChatEndpoint(SimpMessageSendingOperations messageSender) {
    this.messageSender = messageSender;
  }

  @MessageMapping(ChatTopics.PUBLISH_TOPIC)
  @SendTo(ChatTopics.MESSAGES_TOPIC)
  public ChatMessageDto sendMessage(@Payload ChatMessageDto messageDto, StompHeaderAccessor stompHeaderAccessor,
      Principal principal) {
    messageDto.setUserLogin(principal.getName());
    messageDto.setSessionId(stompHeaderAccessor.getSessionId());
    messageDto.setTime(Instant.now());
    return messageDto;
  }

  @Override
  public void onApplicationEvent(AbstractSubProtocolEvent event) {
    String user = event.getUser().getName();
    if (event instanceof SessionConnectedEvent) {
      ChatMessageDto dto = new ChatMessageDto();
      dto.setTime(Instant.now());
      dto.setUserLogin(SYSTEM_USER);
      dto.setMessage(user + " joined the chat.");
      messageSender.convertAndSend(ChatTopics.MESSAGES_TOPIC, dto);
    } else if (event instanceof SessionDisconnectEvent) {
      SessionDisconnectEvent disconnectEvent = (SessionDisconnectEvent) event;
      ChatMessageDto dto = new ChatMessageDto();
      dto.setTime(Instant.now());
      dto.setUserLogin(SYSTEM_USER);
      dto.setSessionId(disconnectEvent.getSessionId());
      dto.setMessage(user  + " left the chat.");
      messageSender.convertAndSend(ChatTopics.MESSAGES_TOPIC, dto);
    }
  }
}
