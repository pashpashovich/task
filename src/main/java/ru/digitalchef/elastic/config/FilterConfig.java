package ru.digitalchef.elastic.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FilterConfig {

    @Value("${filter.enabled}")
    private boolean enabled;

    @Value("${filter.color}")
    private String color;

    @Value("${filter.available}")
    private Boolean available;
}

