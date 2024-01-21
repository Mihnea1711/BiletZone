package com.example.database.persistence.mappers;

import com.example.database.dtos.ProfileDto;
import com.example.database.dtos.PurchasedTicketDto;
import com.example.database.models.Profile;
import com.example.database.models.PurchasedTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface ProfileMapper {

    @Mappings({
    })
    ProfileDto entityToDto(Profile entity);

    @Mappings({
    })
    Profile dtoToEntity(ProfileDto dto);


}
