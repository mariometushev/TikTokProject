package com.example.tiktokproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TikTokProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TikTokProjectApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper(){
        return modelMapper();
    }
}
