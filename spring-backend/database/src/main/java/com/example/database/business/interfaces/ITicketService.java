package com.example.database.business.interfaces;

import com.example.database.dtos.TicketDto;

import java.util.List;
import java.util.Optional;

public interface ITicketService {
    public TicketDto createTicket(TicketDto ticketDto);
    public void deleteTicket(Long id);
    public Optional<TicketDto> findById(Long id);
    public TicketDto updateTicket(Long id, TicketDto ticketDto);
    public Optional<TicketDto> getTicketByID(Long id);
    public List<TicketDto> getTicketsByEventId(Long eventId);
}
