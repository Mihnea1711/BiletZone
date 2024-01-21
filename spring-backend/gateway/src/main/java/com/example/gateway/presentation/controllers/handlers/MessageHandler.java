package com.example.gateway.presentation.controllers.handlers;

import com.example.gateway.business.interfaces.IMessageService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.MessageDto;
import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class MessageHandler {
    private final IMessageService _messageService;

    public MessageHandler(IMessageService messageService) {
        this._messageService = messageService;
    }

    public Mono<ResponseEntity<CustomResponse<Object>>> addMessage(Long eventId, MessageDto messageDto) {
        log.info("Handling confirmation message adding..");
        return _messageService.addMessage(eventId, messageDto)
                .map(result -> {
                    CustomResponse<Object> response = new CustomResponse<>(
                            "Message was added successfully",
                            messageDto.messageText()
                    );
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> handleErrorResponse(e, "WebClientResponseException during adding message"))
                .doOnSuccess(response -> log.info("Adding message response: {}", response));
    }

    public Mono<ResponseEntity<List<MessageDto>>> getAllMessages(String eventId) {
        log.info("Handling messages retrieval...");

        return this._messageService.getAllMessages(eventId)
                .map(messages -> {
                    if (messages.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
                    } else {
                        return ResponseEntity.ok(messages);
                    }
                })
                .doOnSuccess(response -> log.info("Messages retrieval completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<MessageDto>>> getMessageById(Long messageId) {
        log.info("Handling message retrieval...");

        return _messageService.getMessageById(messageId)
                .map(result -> {
                    CustomResponse<MessageDto> response = new CustomResponse<>(
                            result.getMessage(),
                            result.getPayload()
                    );
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> handleErrorResponse(e, "WebClientResponseException during message retrieval"))
                .doOnSuccess(response -> log.info("Message retrieval completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<MessageDto>>> updateMessage(Long messageId, MessageDto updatedMessage) {
        log.info("Handling message update...");

        return _messageService.updateMessage(messageId, updatedMessage)
                .map(result -> {
                    CustomResponse<MessageDto> response = new CustomResponse<>(
                            result.getMessage(),
                            result.getPayload()
                    );
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> handleErrorResponse(e, "WebClientResponseException during message update"))
                .doOnSuccess(response -> log.info("Message update completed with status: {}", response.getStatusCode()));
    }

    public Mono<ResponseEntity<CustomResponse<Void>>> deleteMessage(Long messageId) {
        log.info("Handling message deletion...");

        return _messageService.deleteMessage(messageId)
                .map(result -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(result))
                .onErrorResume(e -> handleErrorResponse(e, "WebClientResponseException during message deletion"))
                .doOnSuccess(response -> log.info("Message deletion completed with status: {}", response.getStatusCode()));
    }

    private <T> Mono<ResponseEntity<CustomResponse<T>>> handleErrorResponse(Throwable e, String errorMessage) {
        log.error("{}: '{}'", errorMessage, e.getMessage());
        int statusCode = (e instanceof WebClientResponseException) ?
                ((WebClientResponseException) e).getStatusCode().value() :
                HttpStatus.INTERNAL_SERVER_ERROR.value();

        CustomResponse<T> customResponse = new CustomResponse<>(errorMessage);
        return Mono.just(ResponseEntity.status(statusCode).body(customResponse));
    }
}
