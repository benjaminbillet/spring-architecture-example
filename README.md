# Websockets and STOMP
Websocket is a protocol for bidirectional (full duplex) communication between a browser and a web server. It is standardized by IETF in [RFC 6455](https://tools.ietf.org/html/rfc6455).

Spring provide support for [websockets](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/html/websocket.html). It also includes a websocket-based message broker implementing the [STOMP](https://stomp.github.io/stomp-specification-1.2.html#Abstract) protocol, enabling the developer to leverage a full publish-subscribe infrastructure. Basically, we expose a websocket endpoint that will be used by the STOMP client to communicate with the broker (see `WebSocketConfiguration`). STOMP clients can then publish and subscribe to topics, using a dedicated Javascript library.

It is of course possible to use raw websockets (without STOMP messaging) using [websocket handlers](https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html#websocket-server-handler) directly (see Spring documentation).

# Chat example
Using the STOMP message broker, we implement a (very) simple chat frontend. Clients will publish in `/topic/send` and subscribe to `/topic/messages` (these names are not HTTP endpoints, they are topic names managed by the broker). In `ChatEndpoint` we do not create a rest controller but a regular controller and use `MessageMapping` annotation to catch messages sent to `/topic/send`. In addition, we use application listeners to catch when clients connect or disconnect, and publish messages accordingly.

The `chat.html` and `chat.js` resources are quite straightforward and rely on [webjars](https://www.webjars.org) for managing the dependencies (STOMP client, SockJS client, jQuery, axios and bootstrap) using gradle. In an actual application, a Javascript framework (e.g., angular or react) should be preferred.
