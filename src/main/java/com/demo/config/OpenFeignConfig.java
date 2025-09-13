package com.demo.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class OpenFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
