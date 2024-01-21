package com.example.mail.presentation.config;

import com.example.mail.presentation.middleware.LoggingMiddleware;
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
