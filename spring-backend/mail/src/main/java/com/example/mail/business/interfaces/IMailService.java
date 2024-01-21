package com.example.mail.business.interfaces;

import com.example.mail.dtos.requests.MailRequest;

public interface IMailService {
    void sendSimpleEmail(MailRequest mailRequest);
    void sendConfirmationEmail(MailRequest mailRequest);
}
