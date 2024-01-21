package com.example.gateway.presentation.config;

import com.example.gateway.presentation.middleware.JWTMiddleware;
import com.example.gateway.presentation.middleware.LoggingMiddleware;
import com.example.gateway.utils.Endpoints;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final LoggingMiddleware _loggingMiddleware;
    private final JWTMiddleware _jwtMiddleware;

    public WebConfig(LoggingMiddleware loggingInterceptor, JWTMiddleware jwtMiddleware) {
        this._loggingMiddleware = loggingInterceptor;
        this._jwtMiddleware = jwtMiddleware;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(_loggingMiddleware).addPathPatterns("/**"); // Apply the interceptor to all paths
        registry.addInterceptor(_jwtMiddleware).addPathPatterns(Endpoints.GATEWAY_PREFIX + "/**"); // Apply the interceptor to all paths
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedHeaders("*")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE");
//                .allowedHeaders(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION, HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS)
//                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
