package com.example.gateway.utils;

import com.example.gateway.dtos.EventDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.gateway.utils.Constants.*;
import static com.example.gateway.utils.Endpoints.CONFIRMATION_MAIL_ANGULAR_ENDPOINT;
import static com.example.gateway.utils.JWT.JWT_SECRET;

public final class Utils {
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String BuildEndpoint(String host, Integer port, String endpoint, String queryParamsRaw) {
        return "http://" + host + ":" + port.toString() + endpoint + "?" + queryParamsRaw;
    }

    public static String BuildEndpointWithoutQuery(String host, Integer port, String endpoint) {
        return "http://" + host + ":" + port.toString() + endpoint;
    }

    public static String BuildRawQuery(String name, String city, String beforeThan, String afterThan, String eventType, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();

        // Add tagged parameters to the query
        if (name != null) {
            queryBuilder.append("name=").append(name).append("&");
        }
        if (city != null) {
            queryBuilder.append("city=").append(city).append("&");
        }
        if (beforeThan != null) {
            queryBuilder.append("before_than=").append(beforeThan).append("&");
        }
        if (afterThan != null) {
            queryBuilder.append("after_than=").append(afterThan).append("&");
        }
        if (eventType != null) {
            queryBuilder.append("event_type=").append(eventType).append("&");
        }

        // Add pageable parameters to the query with default values if they are not present
        int page = pageable.getPageNumber() >= 0 ? pageable.getPageNumber() : Constants.DEFAULT_PAGE;
        int size = pageable.getPageSize() > 0 && pageable.getPageSize() <= Constants.MAX_PAGE_SIZE ? pageable.getPageSize() : Constants.DEFAULT_PAGE_SIZE;

        queryBuilder.append("page=").append(page).append("&");
        queryBuilder.append("size=").append(size).append("&");

        // Remove the trailing "&" if the query is not empty
        if (!queryBuilder.isEmpty()) {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }

        return queryBuilder.toString();
    }

    // Method to extract the JWT token from the Authorization header
    public static String ExtractJwtToken(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.split(" ")[1];
        }
        return null;
    }

    public static String ExtractSubFromJWT(String authorizationHeader) {
        String jwtToken = ExtractJwtToken(authorizationHeader);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        return (String) claims.get(Constants.SUB_CLAIM);
    }

    public static String BuildConfirmationMessage(String token) {
        String queryParam = TOKEN_QUERY_PARAM + "=" + token;
        String confirmationLink = BuildEndpoint(ANGULAR_HOST, ANGULAR_PORT, CONFIRMATION_MAIL_ANGULAR_ENDPOINT, queryParam);

        return "Thank you for registering with BiletZone!\n\n" +
                "To confirm your email address and activate your account, please click on the following link:\n\n" +
                confirmationLink + "\n\n" +
                "If you did not register or do not recognize this activity, please ignore this email.\n\n" +
                "Best regards,\n" +
                "[Your Website Team]";
    }

    public static String buildAdminRegistrationMessage(String adminGmail, String generatedPassword) {
        return "Hello,\n\n" +
                "This gmail address has been registered as an admin on [BiletZone]!\n\n" +
                "Your account details:\n" +
                "Email: " + adminGmail + "\n" +
                "Password: " + generatedPassword + "\n\n" +
                "If you did not register or do not recognize this activity, please ignore this email.\n\n" +
                "Best regards,\n" +
                "[Team BiletZone]";
    }

    public static String buildNotificationMail(String userEmail, List<EventDto> favoriteEvents) {
        StringBuilder message = new StringBuilder();

        message.append("Hello ").append(userEmail).append(",\n\n");

        message.append("Here are your upcoming favorite events within the next month:\n");

        for (EventDto event : favoriteEvents) {
            appendEventDetails(message, event);
        }

        // Closing message
        message.append("Thank you for using our service!\n");
        return message.toString();
    }
    private static void appendEventDetails(StringBuilder message, EventDto event) {
        message.append("Event: ").append(event.name()).append("\n");
        message.append("Description: ").append(event.description()).append("\n");
        message.append("Date: ").append(event.date()).append("\n");
        message.append("Location: ").append(event.location()).append("\n");

        // Link to the event
        String eventLink = "http://localhost:4200/events/" + event.id();
        message.append("Event Link: ").append(eventLink).append("\n\n");
    }

    public static String BuildNewConfirmationMessage(String token, String pass) {
        String queryParam = TOKEN_QUERY_PARAM + "=" + token;
        String confirmationLink = BuildEndpoint(ANGULAR_HOST, ANGULAR_PORT, CONFIRMATION_MAIL_ANGULAR_ENDPOINT, queryParam);

        return "Thank you for registering with [Your Website]!\n\n" +
                "To confirm your email address and activate your account, please click on the following link:\n\n" +
                confirmationLink + "\n\n" +
                "\n Also, your password is \'" + pass + "\' . Don't forget to change it as soon as possible!" +
                "If you did not register or do not recognize this activity, please ignore this email.\n\n" +
                "Best regards,\n" +
                "[Your Website Team]";
    }
}
