package com.example.gateway.business.interfaces;

import com.example.gateway.dtos.requests.MailRequest;
import com.example.gateway.dtos.responses.CustomResponse;
import reactor.core.publisher.Mono;

public interface IMailService {
        Mono<CustomResponse<Object>> sendConfirmationMail(MailRequest mailRequest);
        Mono<CustomResponse<Object>> sendRegularMail(MailRequest mailRequest);
}
