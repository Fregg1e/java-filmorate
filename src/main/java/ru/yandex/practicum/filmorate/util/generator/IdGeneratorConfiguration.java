package ru.yandex.practicum.filmorate.util.generator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfiguration {
    @Bean
    public IdGenerator userIdGenerator() {
        return new IdGenerator();
    }

    @Bean
    public IdGenerator filmIdGenerator() {
        return new IdGenerator();
    }
}
