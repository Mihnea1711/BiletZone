package com.example.gateway.business.services;

import com.example.gateway.business.interfaces.IUserService;
import com.example.gateway.dtos.UserDto;
import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.presentation.config.ServiceConfig;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final ServiceConfig _serviceConfigs;
    private final WebClient webClient;

    @Autowired
    public UserService(ServiceConfig serviceConfigs, WebClient.Builder webClientBuilder) {
        this._serviceConfigs = serviceConfigs;
        this.webClient = webClientBuilder.build();
    }

    public Mono<UserDto> findUserByUUID(String uuid) {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.IDM_GET_USER_BY_UUID_ENDPOINT + uuid, "");

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public UserDto findUserByEmail(String email) {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.IDM_GET_USER_BY_EMAIL_ENDPOINT + email, "");

        RestTemplate restTemplate = new RestTemplate();

        UserDto userDto = restTemplate.getForObject(uri, UserDto.class);
        System.out.println(userDto);

        return userDto;
    }

    @Override
    public List<UserDto> getUsersForNotifications() {
        String host = this._serviceConfigs.IDM_HOST;
        int port = this._serviceConfigs.IDM_PORT;
        String uri = Utils.BuildEndpoint(host, port, Endpoints.IDM_GET_USERS_FOR_NOTIFICATIONS, "");

        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<UserDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<UserDto>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, responseType);

        return responseEntity.getBody();
    }
}
