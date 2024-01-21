package com.example.database.business.services;

import com.example.database.business.interfaces.IMessageService;
import com.example.database.dtos.MessageDto;
import com.example.database.models.Event;
import com.example.database.models.Message;
import com.example.database.persistence.interfaces.IEventRepository;
import com.example.database.persistence.interfaces.IMessageRepository;
import com.example.database.persistence.mappers.MessageMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService {
    private final IMessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final EventService eventService;

    @Autowired
    public MessageService(IMessageRepository messageRepository, MessageMapper messageMapper, EventService eventService) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.eventService = eventService;
    }

    @Transactional
    public MessageDto addMessage(Long eventId, MessageDto messageDto) {
        // Verifică dacă evenimentul există
        Optional<Event> optionalEvent = eventService.getEventByID(eventId);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();

            // Mapare manuală a atributelor
            Message message = new Message();
            message.setEventId(event);
            message.setUserUUID(messageDto.userUUID());  // Adaugă userUUID în Message
            message.setMessageText(messageDto.messageText());

            Message savedMessage = messageRepository.save(message);
            return messageMapper.entityToDto(savedMessage);
        } else {
            throw new ResourceNotFoundException("Event not found with id: " + eventId);
        }
    }

    public MessageDto getMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        return messageMapper.entityToDto(message);
    }

    public List<MessageDto> getMessagesByEventId(Long eventId) {
        List<Message> messages = messageRepository.findByEventId_Id(eventId);
        return messages.stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDto updateMessage(Long messageId, MessageDto updatedMessageDto) {
        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        // Mapare manuală a atributelor
        existingMessage.setUserUUID(updatedMessageDto.userUUID());
        existingMessage.setMessageText(updatedMessageDto.messageText());

        Message updatedMessage = messageRepository.save(existingMessage);
        return messageMapper.entityToDto(updatedMessage);
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
        } else {
            throw new ResourceNotFoundException("Message not found with id: " + messageId);
        }
    }

}
