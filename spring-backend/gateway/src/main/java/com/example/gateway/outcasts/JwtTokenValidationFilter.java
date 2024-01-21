//package com.example.gateway.presentation.filters;
//
////import com.example.gateway.business.services.CustomUserDetailsService;
//import com.example.gateway.business.services.CustomUserDetailsService;
//import com.example.gateway.utils.Constants;
//import com.example.gateway.utils.JWT;
//import com.example.gateway.utils.Utils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class JwtTokenValidationFilter extends OncePerRequestFilter {
//
//    private final ObjectMapper mapper;
//    private final CustomUserDetailsService _customUserDetailsService;
//
//    @Autowired
//    public JwtTokenValidationFilter(ObjectMapper mapper, CustomUserDetailsService customUserDetailsService) {
//        this.mapper = mapper;
//        this._customUserDetailsService = customUserDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
//        Map<String, Object> errorDetails = new HashMap<>();
//
//        System.out.println(request.getRequestURI());
//
//        try {
//            // Extract token from request header
//            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//            System.out.println(authorizationHeader);
//
//            String token = Utils.ExtractJwtToken(authorizationHeader);
//
//            System.out.println("i am here for some reason");
//
//            System.out.println(token);
//
//            // Perform token validation based on your logic
//            if (token != null) {
//                Claims claims = extractClaimsFromToken(token);
//
//                if (claims == null) {
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//
//                System.out.println(claims.getSubject());
//                System.out.println(claims.get(Constants.ROLE_CLAIM));
//
////                String userUUID = claims.getSubject();
////                String authority = (String) claims.get(Constants.ROLE_CLAIM);
//                String email = (String) claims.get("email");
//
//                UserDetails userDetails = this._customUserDetailsService.loadUserByUsername(email);
//
//                System.out.println(userDetails.getUsername());
//                System.out.println(userDetails.getPassword());
//                System.out.println(userDetails.getAuthorities());
//
//                // Set authentication in the SecurityContext if the token is valid
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                System.out.println(authentication);
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception e){
//            errorDetails.put("message", "Authentication Error");
//            errorDetails.put("details", e.getMessage());
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//            mapper.writeValue(response.getWriter(), errorDetails);
//        }
//        // Continue with the filter chain
//        filterChain.doFilter(request, response);
//    }
//
//    private Claims extractClaimsFromToken(String token) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(JWT.JWT_SECRET)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            System.out.println(claims);
//
//            // Map roles to Spring Security authorities
//            return claims;
//        } catch (io.jsonwebtoken.security.SignatureException e) {
//            log.error("JWT SignatureException: {}", e.getMessage());
//            return null;
//        }
//    }
//}
