package com.example.gateway.business.interfaces;

import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.MessageDto;
import com.example.gateway.dtos.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IMessageService {
    Mono<CustomResponse<MessageDto>> addMessage(Long eventId, MessageDto messageDto);
    Mono<List<MessageDto>> getAllMessages(String eventId);
    public Mono<CustomResponse<MessageDto>> getMessageById(Long messageId);

    Mono<CustomResponse<MessageDto>> updateMessage(Long messageId, MessageDto updatedMessage);

    Mono<CustomResponse<Void>> deleteMessage(Long messageId);
}
