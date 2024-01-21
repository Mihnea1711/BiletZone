package com.example.database.persistence.mappers;

import com.example.database.dtos.MessageDto;

import com.example.database.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageDto entityToDto(Message entity);

    Message dtoToEntity(MessageDto dto);
}
