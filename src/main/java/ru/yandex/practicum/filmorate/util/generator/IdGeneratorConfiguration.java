package ru.yandex.practicum.filmorate.util.generator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfiguration {
    @Bean("userIdGenerator")
    public IdGenerator userIdGenerator() {
        return new IdGenerator();
    }

    @Bean("filmIdGenerator")
    public IdGenerator filmIdGenerator() {
        return new IdGenerator();
    }
}
