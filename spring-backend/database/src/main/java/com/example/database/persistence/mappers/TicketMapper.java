package com.example.database.persistence.mappers;

import com.example.database.dtos.TicketDto;
import com.example.database.models.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { EventMapper.class })

public interface TicketMapper {
    @Mappings({
            //@Mapping(target = "event", source = "event")

    })
    TicketDto entityToDto(Ticket entity);

    @Mappings({
            // @Mapping(target = "eventID", source = "eventID")
    })
    Ticket dtoToEntity(TicketDto dto);
}
