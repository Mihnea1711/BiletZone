package com.example.database.presentation.config;

import com.example.database.presentation.middleware.LoggingMiddleware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoggingMiddleware _loggingMiddleware;

    public WebConfig(LoggingMiddleware loggingInterceptor) {
        this._loggingMiddleware = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(_loggingMiddleware)
                .addPathPatterns("/**"); // Apply the interceptor to all paths
    }
}
