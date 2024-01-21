package com.example.gateway.presentation.controllers;

import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.business.interfaces.IMessageService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.MessageDto;
import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.controllers.handlers.MailHandler;
import com.example.gateway.presentation.controllers.handlers.MessageHandler;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.JWT;
import com.example.gateway.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@RestController(value = "message_controller")
@RequestMapping(Endpoints.GATEWAY_PREFIX)
@Slf4j
public class MessageController {
    private final MessageHandler _messageHandler;

    @Autowired
    public MessageController(IMessageService mailService) {
        this._messageHandler = new MessageHandler(mailService);
    }

    @PostMapping(Endpoints.DATABASE_ADD_MESSAGE+"{eventId}")
    public Mono<ResponseEntity<CustomResponse<Object>>> addMessage(
            @PathVariable Long eventId,
            @RequestBody MessageDto messageDto
    ) {
        return _messageHandler.addMessage(eventId,messageDto);
    }

    @GetMapping(Endpoints.GET_ALL_MESSAGES + "{eventId}")
    public Mono<ResponseEntity<List<MessageDto>>> getEvents(
            @PathVariable String eventId)
    {
        return _messageHandler.getAllMessages(eventId);

    }

    @GetMapping(Endpoints.UNIVERSAL_MESSAGE + "{messageId}")
    public Mono<ResponseEntity<CustomResponse<MessageDto>>> getMessageById(@PathVariable Long messageId) {
        return _messageHandler.getMessageById(messageId);
    }

    @PutMapping(Endpoints.UNIVERSAL_MESSAGE + "{messageId}")
    public Mono<ResponseEntity<CustomResponse<MessageDto>>> updateMessage(
            @PathVariable Long messageId,
            @RequestBody MessageDto updatedMessage
    ) {
        return _messageHandler.updateMessage(messageId, updatedMessage);
    }

    @DeleteMapping(Endpoints.UNIVERSAL_MESSAGE + "{messageId}")
    public Mono<ResponseEntity<CustomResponse<Void>>> deleteMessage(@PathVariable Long messageId) {
        return _messageHandler.deleteMessage(messageId);
    }
}
