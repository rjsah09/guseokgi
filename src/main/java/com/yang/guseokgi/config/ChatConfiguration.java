package com.yang.guseokgi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //stomp 접속 주소 설정
        registry.addEndpoint("/stomp/chat")   //엔드포인트 설정
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();  //SockJs 추가(웹소켓을 지원하지 않는 브라우저를 위한 폴링, 롱폴링 방식 추가 목적)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //sub
        registry.enableSimpleBroker("/sub");

        //pub
        registry.setApplicationDestinationPrefixes("/pub");
    }

}
