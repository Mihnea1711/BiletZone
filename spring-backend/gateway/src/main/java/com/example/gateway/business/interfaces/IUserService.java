package com.example.gateway.business.interfaces;

import com.example.gateway.dtos.UserDto;
import com.example.gateway.dtos.responses.CustomResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserService {
    List<UserDto> getUsersForNotifications();
}
