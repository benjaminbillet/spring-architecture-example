package my.app.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer
    implements WebSocketMessageBrokerConfigurer {

  private final ApplicationProperties config;

  public WebSocketConfiguration(ApplicationProperties config) {
    this.config = config;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
  }

  @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
      String[] allowedOrigins = new String[0];
      if (config.getHttp().getCors().getAllowedOrigins() != null) {
        allowedOrigins = config.getHttp().getCors().getAllowedOrigins().toArray(new String[0]);
      }

      registry.addEndpoint("/websocket/chat")
        .setHandshakeHandler(defaultHandshakeHandler())
        .setAllowedOrigins(allowedOrigins)
        .withSockJS();
    }

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages.nullDestMatcher().authenticated()
      // the message endpoint is authenticated
      .simpDestMatchers("/topic/**").authenticated()
      // message types other than MESSAGE and SUBSCRIBE are forbidden
      .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
      .anyMessage().denyAll(); // deny everything else
  }

  // disable CSRF for websockets (we manage our tokens)
  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

  private DefaultHandshakeHandler defaultHandshakeHandler() {
    return new DefaultHandshakeHandler() {
      @Override
      protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();
        // uncomment this to allow anonymous authentication
        /*if (principal == null) {
          Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(AuthUtils.ANONYMOUS);
          principal = new AnonymousAuthenticationToken("websocket-configuration", "anonymous", authorities);
        }*/
        return principal;
      }
    };
  }
}
