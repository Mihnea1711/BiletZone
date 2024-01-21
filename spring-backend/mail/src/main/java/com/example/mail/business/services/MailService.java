package com.example.mail.business.services;

import com.example.mail.business.interfaces.IMailService;
import com.example.mail.dtos.requests.MailRequest;
import com.example.mail.utils.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.scanner.Constant;

import java.util.Objects;


@Service
@Component
@NoArgsConstructor
public class MailService implements IMailService {
    private JavaMailSender _javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this._javaMailSender = javaMailSender;
    }

    @Override
    public void sendConfirmationEmail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getTo());
        message.setSubject(Constants.DEFAULT_CONFIRMATION_SUBJECT);
        String mailMessage = Constants.DEFAULT_CONFIRMATION_MESSAGE + "\n" + mailRequest.getMessage();
        message.setText(mailMessage);
        send(message);
    }

    @Override
    public void sendSimpleEmail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getTo());
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getMessage());

        System.out.println(message);

        send(message);
    }

    @Async
    protected void send(SimpleMailMessage simpleMailMessage) {
        try {
            MimeMessage mimeMessage = this._javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(Objects.requireNonNull(simpleMailMessage.getTo()));
            helper.setSubject(Objects.requireNonNull(simpleMailMessage.getSubject()));
            helper.setText(Objects.requireNonNull(simpleMailMessage.getText()));

            this._javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Failed to send mail");
            throw new IllegalStateException("failed to send email");
        }
    }
}
