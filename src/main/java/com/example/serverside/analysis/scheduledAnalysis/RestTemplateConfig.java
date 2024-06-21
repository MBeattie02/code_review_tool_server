package com.example.serverside.analysis.scheduledAnalysis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for creating RestTemplate bean.
 * RestTemplate is used for making HTTP requests in the application.
 * This class defines a bean method to create a RestTemplate instance, which can then be autowired into other components.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean.
     * RestTemplate is used throughout the application for making HTTP requests.
     *
     * @return A new instance of RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
