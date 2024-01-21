package com.example.gateway.business.services;

import com.example.gateway.business.interfaces.IAuthService;
import com.example.gateway.dtos.UserDto;
import com.example.gateway.dtos.requests.AuthRequest;
import com.example.gateway.dtos.requests.ConfirmationRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.config.ServiceConfig;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService implements IAuthService {
    private final ServiceConfig _serviceConfigs;
    private final WebClient webClient;

    @Autowired
    public AuthService(ServiceConfig serviceConfigs, WebClient.Builder webClientBuilder) {
        this._serviceConfigs = serviceConfigs;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<CustomResponse<String>> registerUser(UserDto userDto) {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;

        return webClient
                .post()
                .uri(Utils.BuildEndpoint(host, port, Endpoints.IDM_REGISTER_ENDPOINT, ""))
                .body(BodyInserters.fromValue(userDto))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<String>> loginUser(AuthRequest ar) {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;

         return webClient
                 .post()
                .uri(Utils.BuildEndpoint(host, port, Endpoints.IDM_LOGIN_ENDPOINT, ""))
                .body(BodyInserters.fromValue(ar))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<CustomResponse<Object>> confirmMail(ConfirmationRequest confirmationRequest) {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;

        return webClient
                .patch()
                .uri(Utils.BuildEndpoint(host, port, Endpoints.IDM_CONFIRM_MAIL_ENDPOINT, ""))
                .body(BodyInserters.fromValue(confirmationRequest))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
