package com.example.idm.persistence.mappers;

import com.example.idm.dtos.UserDto;
import com.example.idm.models.User;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto entityToDto(User entity);

    @Named("dtoToEntity")
    default User dtoToEntity(UserDto dto) {
        return new User(dto.email(), dto.password(), dto.role(), dto.confirmationToken(), dto.isConfirmed(), dto.isNotificationsEnabled());
    }
}
