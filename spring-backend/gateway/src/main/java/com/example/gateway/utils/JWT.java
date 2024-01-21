package com.example.gateway.utils;

import com.example.gateway.dtos.EventDto;
import com.example.gateway.dtos.responses.CustomResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Locale;

import static com.example.gateway.utils.Utils.ExtractJwtToken;

@Slf4j
public class JWT {
    public static final SecretKey JWT_SECRET = new SecretKeySpec(Constants.JWT_SECRET_STRING.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public static Boolean allowOnlyUser(String token) {
        return checkRole(token, Constants.USER_ROLE);
    }

    public static Boolean allowOnlyAdmin(String token) {
        return checkRole(token, Constants.ADMIN_ROLE);
    }

    public static Boolean allowBothRoles(String token) {
        return Boolean.TRUE.equals(checkRole(token, Constants.USER_ROLE)) || Boolean.TRUE.equals(checkRole(token, Constants.ADMIN_ROLE));
    }

    private static Boolean checkRole(String token, String requiredRole) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = (String) claims.get(Constants.ROLE_CLAIM);

            System.out.println("aici 1" + requiredRole.toLowerCase(Locale.ROOT));
            System.out.println("aici 2" + role.toLowerCase(Locale.ROOT));

            return requiredRole.toLowerCase(Locale.ROOT).equals(role.toLowerCase(Locale.ROOT));
        } catch (SignatureException e) {
            log.error("JWT SignatureException: {}", e.getMessage());
            return false;
        }
    }

    public static Mono<ResponseEntity<CustomResponse<List<EventDto>>>> authorizeUserAndBuildResponse(String authorizationHeader) {
        String jwtToken = ExtractJwtToken(authorizationHeader);

        if (JWT.allowOnlyUser(jwtToken)) {
            return Mono.empty();  // User is authorized, proceed with the operation
        } else {
            String message = "Access forbidden";
            CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                    message,
                    null
            );
            return Mono.just(ResponseEntity.status(403).body(customResponse));
        }
    }

    public static Mono<ResponseEntity<CustomResponse<List<EventDto>>>> authorizeAdminAndBuildResponse(String authorizationHeader) {
        String jwtToken = ExtractJwtToken(authorizationHeader);

        if (JWT.allowOnlyAdmin(jwtToken)) {
            return Mono.empty();  // Admin is authorized, proceed with the operation
        } else {
            String message = "Access forbidden";
            CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                    message,
                    null
            );
            return Mono.just(ResponseEntity.status(403).body(customResponse));
        }
    }

    public static Mono<ResponseEntity<CustomResponse<List<EventDto>>>> authorizeBothAndBuildResponse(String authorizationHeader) {
        String jwtToken = ExtractJwtToken(authorizationHeader);

        if (JWT.allowBothRoles(jwtToken)) {
            return Mono.empty();  // Admin is authorized, proceed with the operation
        } else {
            String message = "Access forbidden";
            CustomResponse<List<EventDto>> customResponse = new CustomResponse<>(
                    message,
                    null
            );
            return Mono.just(ResponseEntity.status(403).body(customResponse));
        }
    }
}
