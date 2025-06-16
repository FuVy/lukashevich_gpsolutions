package com.gpsolutions.lukashevich.configs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public SimpleModule stringDeserializerModule() {
        final var module = new SimpleModule();

        module.addDeserializer(String.class, new StringDeserializer() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctx) throws java.io.IOException {
                final var value = super.deserialize(p, ctx);
                if (value == null) {
                    return null;
                }

                final var trimmed = value.trim();
                if (trimmed.isEmpty()) {
                    return null;
                }

                return trimmed;
            }
        });

        return module;
    }
}