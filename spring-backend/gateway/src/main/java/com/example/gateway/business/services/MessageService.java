package com.example.gateway.business.services;

import com.example.gateway.business.interfaces.IMessageService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.MessageDto;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.config.ServiceConfig;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService implements IMessageService {
    private final ServiceConfig _serviceConfigs;
    private final WebClient webClient;

    @Autowired
    public MessageService(ServiceConfig serviceConfigs, WebClient.Builder webClientBuilder) {
        this._serviceConfigs = serviceConfigs;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<CustomResponse<MessageDto>> addMessage(Long eventId, MessageDto messageDto) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpointWithoutQuery(host, port, Endpoints.GATEWAY_PREFIX + Endpoints.DATABASE_ADD_MESSAGE) + eventId;

        return webClient
                .post()
                .uri(uri)
                .body(BodyInserters.fromValue(messageDto))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<List<MessageDto>> getAllMessages(String eventId) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpointWithoutQuery(host, port,Endpoints.GATEWAY_PREFIX +  Endpoints.GET_ALL_MESSAGES) + eventId;

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MessageDto>>() {})
                .onErrorResume(WebClientResponseException.class, e -> {
                    // Extract the HTTP status code from the exception
                    int statusCode = e.getStatusCode().value();

                    // Return an empty list and handle the error status separately
                    return Mono.just(Collections.emptyList());
                });    }

    @Override
    public Mono<CustomResponse<MessageDto>> getMessageById(Long messageId) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpointWithoutQuery(host, port, Endpoints.UNIVERSAL_MESSAGE) + messageId;

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(MessageDto.class)
                .map(messageDto -> new CustomResponse<>("Message retrieved successfully", messageDto))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClientResponseException during message retrieval: '{}'", e.getMessage());
                    return Mono.just(new CustomResponse<>("WebClientResponseException during message retrieval", null));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during message retrieval: '{}'", e.getMessage());
                    return Mono.just(new CustomResponse<>("Unexpected error during message retrieval", null));
                });
    }

    public Mono<CustomResponse<MessageDto>> updateMessage(Long messageId, MessageDto messageDto) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpointWithoutQuery(host, port, Endpoints.GATEWAY_PREFIX + Endpoints.UNIVERSAL_MESSAGE) + messageId;

        return webClient
                .put()
                .uri(uri)
                .body(BodyInserters.fromValue(messageDto))
                .retrieve()
                .bodyToMono(MessageDto.class)
                .map(result -> new CustomResponse<>("Message updated successfully", result))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClientResponseException during updating message: '{}'", e.getMessage());
                    return Mono.just(new CustomResponse<>("WebClientResponseException during message updating", null));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unexpected error during message updating: '{}'", e.getMessage());
                    return Mono.just(new CustomResponse<>("Unexpected error during message updating", null));
                });
    }

    @Override
    public Mono<CustomResponse<Void>> deleteMessage(Long messageId) {
        String host = this._serviceConfigs.DATABASE_HOST;
        int port = this._serviceConfigs.DATABASE_PORT;
        String uri = Utils.BuildEndpointWithoutQuery(host, port, Endpoints.GATEWAY_PREFIX + Endpoints.UNIVERSAL_MESSAGE) + messageId;

        return webClient
                .delete()
                .uri(uri)
                .retrieve()
                .toBodilessEntity()
                .map(responseEntity -> new CustomResponse<>("Message was deleted successfully", null));
    }
}
