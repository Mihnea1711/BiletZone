package com.example.gateway.business.notification;

import com.example.gateway.business.interfaces.IMailService;
import com.example.gateway.business.interfaces.IProfileService;
import com.example.gateway.business.interfaces.IUserService;
import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.UserDto;
import com.example.gateway.dtos.requests.MailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;

import static com.example.gateway.utils.Constants.UPCOMING_EVENTS_NOTIFICATION;
import static com.example.gateway.utils.Utils.buildNotificationMail;
import static com.example.gateway.utils.Utils.dateFormatter;

@Component
@Slf4j
public class GatewayNotificationRunner implements CommandLineRunner {
    private final IUserService _userService;
    private final IMailService _mailService;
    private final IProfileService _profileService;

    @Autowired
    public GatewayNotificationRunner(IUserService userService, IMailService mailService, IProfileService profileService) {
        this._userService = userService;
        this._mailService = mailService;
        this._profileService = profileService;
    }

    public void run(String... args) throws Exception {
//        List<UserDto> users = this._userService.getUsersForNotifications();
//
//        for (UserDto user : users) {
//            processUser(user);
//        }
    }

    private void processUser(UserDto user) {
        this._profileService.getFavoriteEvents(user.uuid())
                .publishOn(Schedulers.boundedElastic())
                .map(response -> {
                    if (!response.getPayload().isEmpty() && hasEventsWithinMonth(response.getPayload())) {
                        String mailMessage = buildNotificationMail(user.email(), response.getPayload());
                        MailRequest mailRequest = new MailRequest(user.email(), UPCOMING_EVENTS_NOTIFICATION, mailMessage);

                        this._mailService.sendRegularMail(mailRequest)
                        .subscribe();
                    }

                    return ResponseEntity.ok();
                })
                .doOnSuccess(response -> log.info("Notification runner finished"))
                .subscribe();
    }

    private boolean hasEventsWithinMonth(List<EventDto> events) {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthLater = currentDate.plusMonths(1);

        return events.stream()
                .anyMatch(event -> {
                    LocalDate eventDate = LocalDate.parse(event.date(), dateFormatter);
                    return eventDate.isAfter(currentDate) && eventDate.isBefore(oneMonthLater);
                });
    }
}
