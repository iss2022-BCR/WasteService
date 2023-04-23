package unibo.wasteservice_gui;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/* This class enables WebSocket and STOMP messaging.
@Configuration: this indicate that this class is a Spring configuration class.
*/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //The following method enables a simple memory based message broker to carry messages
        // back to the client in destinations prefixed with "/topic"
        config.enableSimpleBroker("/topic");
        // If messages are annotated with @MessageMapping the destination will be
        // in /app/<path-in-MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    // This method registers the /weebsocket endpoint.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*");
    }

}


