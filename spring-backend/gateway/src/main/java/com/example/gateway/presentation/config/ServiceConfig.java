package com.example.gateway.presentation.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    public final String DATABASE_HOST = "localhost";
    public final int DATABASE_PORT = 8081;

    public final String IDM_HOST = "localhost";
    public final int IDM_PORT = 8082;

    public final String MAIL_HOST = "localhost";
    public final int MAIL_PORT = 8083;


}
