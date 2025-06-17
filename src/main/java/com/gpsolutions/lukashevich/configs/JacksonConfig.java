package com.gpsolutions.lukashevich.configs;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public SimpleModule stringDeserializerModule(CustomStringDeserializer stringDeserializer) {
        final var module = new SimpleModule();
        module.addDeserializer(String.class, stringDeserializer);
        return module;
    }
}