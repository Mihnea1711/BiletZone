package com.example.gateway.business.services;

import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.config.ServiceConfig;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MailService implements IMailService {
    private final ServiceConfig _serviceConfigs;
    private final WebClient webClient;

    @Autowired
    public MailService(ServiceConfig serviceConfigs, WebClient.Builder webClientBuilder) {
        this._serviceConfigs = serviceConfigs;
        this.webClient = webClientBuilder.build();
    }
    @Override
    public Mono<CustomResponse<Object>> sendConfirmationMail(MailRequest mailRequest) {
        String host = this._serviceConfigs.MAIL_HOST;
        int port = this._serviceConfigs.MAIL_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.MAIL_SEND_CONFIRMATION, "");

        return webClient
                .post()
                .uri(uri)
                .body(BodyInserters.fromValue(mailRequest))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<Object>> sendRegularMail(MailRequest mailRequest) {
        String host = this._serviceConfigs.MAIL_HOST;
        int port = this._serviceConfigs.MAIL_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.MAIL_SEND_REGULAR, "");

        System.out.println("sunt aici lol");
        System.out.println(mailRequest);

        return webClient
                .post()
                .uri(uri)
                .body(BodyInserters.fromValue(mailRequest))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
