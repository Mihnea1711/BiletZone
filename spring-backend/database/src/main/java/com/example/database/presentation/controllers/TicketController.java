package com.example.database.presentation.controllers;

import com.example.database.business.interfaces.ITicketService;
import com.example.database.dtos.TicketDto;
import com.example.database.dtos.responses.CustomResponse;
import com.example.database.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController("ticket_controller")
@RequestMapping(Constants.DATABASE_PREFIX)
@Slf4j
public class TicketController {
    private final ITicketService _ticketService;
    @Autowired
    public TicketController(ITicketService ticketService) {
        this._ticketService = ticketService;
    }
    @PostMapping("/ticket")
    public ResponseEntity<CustomResponse<TicketDto>> addTicket(@RequestBody TicketDto ticketDto) {
        try {
            TicketDto addedTicket = _ticketService.createTicket(ticketDto);
            String message = "Ticket added successfully";
            CustomResponse<TicketDto> response = new CustomResponse<>(message, addedTicket);
            log.info("Ticket added successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            log.error("An error occurred while adding the ticket", ex);
            String errorMessage = "An error occurred while adding the ticket";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }
    //    @DeleteMapping("/ticket/{id}")
//    public ResponseEntity<TicketDto> deleteTicket(@PathVariable Long id) {
//        Optional<TicketDto> ticket = _ticketService.findById(id);
//        if (!ticket.isEmpty()) {
//           _ticketService.deleteTicket(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<CustomResponse<Void>> deleteTicket(@PathVariable Long id) {
        try {
            Optional<TicketDto> ticket = _ticketService.findById(id);
            if (ticket.isPresent()) {
                _ticketService.deleteTicket(id);
                String message = "Ticket deleted successfully";
                CustomResponse<Void> response = new CustomResponse<>(message, null);
                log.info("Ticket deleted successfully");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            } else {
                String errorMessage = "Ticket not found for deletion";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(errorMessage, null));
            }
        } catch (Exception ex) {
            log.error("An error occurred while deleting the ticket", ex);
            String errorMessage = "An error occurred while deleting the ticket";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }
    @PutMapping("ticket/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,  @RequestBody TicketDto ticketDto) {
        Optional<TicketDto> ticketDto1 = _ticketService.getTicketByID(id);
        if (!ticketDto1.isEmpty()) {
            _ticketService.updateTicket(id, ticketDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{eventId}/tickets")
    public ResponseEntity<CustomResponse<List<TicketDto>>> getTicketsByEventId(@PathVariable Long eventId) {
        try {
            List<TicketDto> tickets = _ticketService.getTicketsByEventId(eventId);

            if (!tickets.isEmpty()) {
                String message = "Tickets retrieved successfully";
                CustomResponse<List<TicketDto>> response = new CustomResponse<>(message, tickets);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                String errorMessage = "No tickets found for the specified event";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(errorMessage, null));
            }
        } catch (Exception ex) {
            log.error("An error occurred while retrieving tickets by event ID", ex);
            String errorMessage = "An unexpected error occurred while retrieving tickets by event ID";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(errorMessage, null));
        }
    }
}
