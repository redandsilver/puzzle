package com.example.puzzle.config;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoolSmsConfig {
    @Value("${coolsms.api.key}")
    String API_KEY;
    @Value("${coolsms.api.secret}")
    String API_SECRET;

    @Bean
    public DefaultMessageService CoolSmsConfig(){
        return NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET, "https://api.coolsms.co.kr");
    }

}