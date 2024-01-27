package com.example.puzzle.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    @Value("${coolsms.api.sender}")
    private String sender;
    @Value("${coolsms.api.content}")
    private String content;

    @Bean
    public Message MessageConfig(){
        Message message = new Message();
        message.setFrom(sender);
        message.setText(content);
        return message;
    }
}
