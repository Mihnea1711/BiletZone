package com.example.gateway.business.interfaces;

import com.example.gateway.dtos.UserDto;
import com.example.gateway.dtos.requests.AuthRequest;
import com.example.gateway.dtos.requests.ConfirmationRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import reactor.core.publisher.Mono;

public interface IAuthService {
    Mono<CustomResponse<String>> registerUser(UserDto userDto);
    Mono<CustomResponse<String>> loginUser(AuthRequest ar);
    Mono<CustomResponse<Object>> confirmMail(ConfirmationRequest confirmationRequest);
}
