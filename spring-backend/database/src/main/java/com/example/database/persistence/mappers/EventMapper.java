package com.example.database.persistence.mappers;

import com.example.database.dtos.EventDto;
import com.example.database.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring"   )
public interface EventMapper {
    @Mappings({

            // Add more mappings if needed
    })
    EventDto entityToDto(Event entity);

    @Mappings({

            // Add more mappings if needed
    })
    Event dtoToEntity(EventDto dto);
}
