package com.example.database.business.services;

import com.example.database.business.interfaces.ITicketService;
import com.example.database.dtos.TicketDto;
import com.example.database.models.Event;
import com.example.database.models.Ticket;
import com.example.database.persistence.interfaces.IEventRepository;
import com.example.database.persistence.interfaces.ITicketRepository;
import com.example.database.persistence.mappers.TicketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TicketService implements ITicketService {
    @Autowired
    private ITicketRepository _ticketRepository;
    @Autowired
    private TicketMapper _ticketMapper;
    @Autowired
    private IEventRepository _eventRepository;
    @Override
    public TicketDto createTicket(TicketDto ticketDto){
        Optional<Event> eventOptional = this._eventRepository.findById(ticketDto.eventID());
        if(eventOptional.isPresent()){
            Ticket ticket = new Ticket(ticketDto.id(), ticketDto.name(), ticketDto.price(), ticketDto.quantity(), eventOptional.get());
            Ticket newTicket = _ticketRepository.save(ticket);
            return new TicketDto(newTicket.getId(), newTicket.getName(), newTicket.getPrice(), newTicket.getQuantity(), newTicket.getEvent().getId());
        }
        else
            return null;

    }
    @Override
    public void deleteTicket(Long id){
        _ticketRepository.deleteById(id);
    }
    public Optional<TicketDto> findById(Long id){
        Optional<Ticket> ticket =  this._ticketRepository.findById(id);
        return ticket.map(this._ticketMapper::entityToDto);
    }
    @Override
    public TicketDto updateTicket(Long id, TicketDto ticketDto){
        Optional<Ticket> existingTicket = _ticketRepository.findById(id);
        if (existingTicket.isPresent()) {
            Ticket newExistingTicket = existingTicket.get();
            newExistingTicket.setName(ticketDto.name());
            newExistingTicket.setPrice(ticketDto.price());
            newExistingTicket.setQuantity(ticketDto.quantity());
            Ticket updatedTicket = _ticketRepository.saveAndFlush(newExistingTicket);
            return _ticketMapper.entityToDto(updatedTicket);
        } else {
//            Optional<Event> eventOptional = this._eventRepository.findById(ticketDto.eventID());
//            Ticket ticket = new Ticket(ticketDto.id(), ticketDto.name(), ticketDto.price(), ticketDto.quantity(), eventOptional.get());
//            Ticket newTicket = _ticketRepository.save(ticket);
//            return new TicketDto(newTicket.getId(), newTicket.getName(), newTicket.getPrice(), newTicket.getQuantity(), newTicket.getEvent().getId());
            return null;
        }
    }
    public Optional<TicketDto> getTicketByID(Long id) {
        Optional<Ticket> ticket =  this._ticketRepository.findById(id);
        return ticket.map(this._ticketMapper::entityToDto);
    }

    @Override
    public List<TicketDto> getTicketsByEventId(Long eventId) {
        return _ticketRepository.findByIdEvent(eventId).stream().map(ticket -> _ticketMapper.entityToDto(ticket)).toList();
    }



}