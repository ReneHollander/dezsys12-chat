package at.renehollander.mobileapp.configuration;

import at.renehollander.mobileapp.handler.ChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatHandler(), "/chat").setAllowedOrigins("*");
    }
}

//@Configuration
//public class WebSocketConfiguration {
//
////    @Autowired
////    private AutowireCapableBeanFactory beanFactory;
//
//    @Bean
//    public ChatHandler chatEndpoint() {
//        return new ChatHandler();
//    }
//
//    @Bean
//    public ServerEndpointExporter endpointExporter() {
//        return new ServerEndpointExporter();
//    }
//
//}
