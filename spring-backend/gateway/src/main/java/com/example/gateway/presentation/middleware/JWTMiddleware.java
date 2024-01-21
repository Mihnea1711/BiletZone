package com.example.gateway.presentation.middleware;

import com.example.gateway.dtos.responses.CustomResponse;
import com.example.gateway.utils.Constants;
import com.example.gateway.utils.Endpoints;
import com.example.gateway.utils.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import reactor.core.publisher.Mono;

import static com.example.gateway.utils.Constants.EXCLUDED_ENDPOINTS;
import static com.example.gateway.utils.JWT.JWT_SECRET;
import static com.example.gateway.utils.Utils.ExtractJwtToken;

@Slf4j
@Component
public class JWTMiddleware implements HandlerInterceptor  {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        log.info("JWT Middleware - Request URI: {}", request.getRequestURI());
        String requestURI = request.getRequestURI();

        // Check if the request URI is in the list of excluded endpoints
        if (isExcludedEndpoint(requestURI)) {
            log.info("Access allowed without JWT validation for URI: {}", requestURI);
            return true; // Allow access without JWT validation
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && ExtractJwtToken(authorizationHeader) != null) {
            String jwt = ExtractJwtToken(authorizationHeader);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String role = (String) claims.get(Constants.ROLE_CLAIM);
            System.out.println("sunt aici" + role);

            // Check endpoints and set validations
            if (authorizeRequest(requestURI, jwt)) {
                log.info("Access granted for URI: {} with JWT: {}", requestURI, jwt);
                return true; // Proceed with the request
            } else {
                // Unauthorized access, return specific response
                setUnauthorizedResponse(response);
                log.warn("Access denied for URI: {} with JWT: {}", requestURI, jwt);
                return false;
            }
        }

        // Unauthorized access
        setUnauthorizedResponse(response);
        log.warn("Unauthorized access for URI: {}", requestURI);
        return false;
    }

    private boolean isExcludedEndpoint(String requestURI) {
        // Check if the request URI matches any of the excluded endpoints
        return EXCLUDED_ENDPOINTS.stream().anyMatch(requestURI::startsWith);
    }

    private void setUnauthorizedResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean authorizeRequest(String requestURI, String jwtToken) {
        // Enumerate your endpoints and perform the necessary validations
        if (requestURI.startsWith("/api/profiles")) {
            requestURI = "/api/profiles";
        }
        return switch (requestURI) {
            // examples
            case "/api/user" -> JWT.allowOnlyUser(jwtToken);
            case "/api/profiles" -> JWT.allowOnlyAdmin(jwtToken);
            case "/api/both" -> JWT.allowBothRoles(jwtToken);

            //case "/main/profiles" -> JWT.allowBothRoles(jwtToken);
            //case "/api/profiles" -> JWT.allowOnlyUser(jwtToken);
            //case "/api/users" -> JWT.allowBothRoles(jwtToken);


            // add ur endpoints here (endpoints that need validation) (if no validation required add them to EXCLUDED_ENDPOINTS list)
//            case Endpoints.GATEWAY_PREFIX + Endpoints.GET_FAVORITE_EVENTS -> JWT.allowOnlyUser(jwtToken);
//            case Endpoints.GATEWAY_PREFIX + Endpoints.ADD_EVENT_TO_FAVORITES -> JWT.allowOnlyUser(jwtToken);

            //            this is commented because has the same endpoint as the one above
            //            case Endpoints.GATEWAY_PREFIX + Endpoints.DELETE_EVENT_FROM_FAVORITES -> JWT.allowOnlyUser(jwtToken);

            default ->
                // If the endpoint is not explicitly handled, deny access
                    false;
        };
    }
}