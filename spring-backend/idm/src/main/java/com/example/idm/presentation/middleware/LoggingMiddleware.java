package com.example.idm.presentation.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Component
public class LoggingMiddleware implements HandlerInterceptor {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        logRequest(request);
        return true;
    }

    private void logRequest(HttpServletRequest request) {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(FORMATTER);

        System.out.println("------------------------------------------------------------");
        System.out.println("Request received at: " + formattedTimestamp);
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Remote Address: " + request.getRemoteAddr());
        System.out.println("Headers: ");
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> System.out.println(headerName + ": " + request.getHeader(headerName)));
        System.out.println("------------------------------------------------------------");
    }
}
