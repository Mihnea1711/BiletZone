package com.example.database.business.interfaces;

import com.example.database.dtos.MessageDto;
import com.example.database.models.Event;
import com.example.database.models.Message;

import java.util.List;

public interface IMessageService {
    public MessageDto addMessage(Long eventId, MessageDto messageDto);
    public MessageDto getMessageById(Long messageId);
    public List<MessageDto> getMessagesByEventId(Long eventId);
    public MessageDto updateMessage(Long messageId, MessageDto updatedMessageDto);
    public void deleteMessage(Long messageId);
}
