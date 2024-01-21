package com.example.database.persistence.mappers;

import com.example.database.dtos.HallMapDto;
import com.example.database.models.HallMap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HallMapMapper {
    HallMapDto entityToDto(HallMap entity);

    HallMap dtoToEntity(HallMapDto dto);
}
