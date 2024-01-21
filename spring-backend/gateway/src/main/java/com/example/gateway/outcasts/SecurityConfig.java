//package com.example.gateway.presentation.config;
//
//import com.example.gateway.business.services.CustomUserDetailsService;
////import com.example.gateway.presentation.filters.JwtTokenValidationFilter;
//import com.example.gateway.utils.Constants;
//import com.example.gateway.utils.Endpoints;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    private final CustomUserDetailsService _userDetailsService;
////    private final JwtTokenValidationFilter _jwtTokenValidationFilter;
//
//    @Autowired
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this._userDetailsService = customUserDetailsService;
////        this._jwtTokenValidationFilter = jwtTokenValidationFilter;
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(_userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // Enable CORS and disable CSRF
//        http
//            .cors(AbstractHttpConfigurer::disable)                      // Disable CORS
//            .csrf(AbstractHttpConfigurer::disable);                     // Disable CSRF
//
////        // Set session management to stateless
//        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
////
////        // Set unauthorized requests exception handler
////        http
////            .exceptionHandling(exceptionHandling ->
////                    exceptionHandling.authenticationEntryPoint(
////                    (request, response, ex) -> {
////                            System.out.println(ex.getMessage());
////                            response.sendError(
////                                HttpServletResponse.SC_UNAUTHORIZED,
////                                ex.getMessage()
////                            );
////                        }
////            ));
////
////        // Add custom filter for token validation
////        http.addFilterBefore(_jwtTokenValidationFilter, UsernamePasswordAuthenticationFilter.class);
//
////        // Configure access control based on roles using lambda-based authorizeRequests
//        http
//            .authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests
////                    .requestMatchers(Endpoints.GATEWAY_PREFIX + "/login").permitAll()           // don t care about auth
////                    .requestMatchers(Endpoints.GATEWAY_PREFIX + "/register").permitAll()        // don t care about auth
//                    .anyRequest().permitAll()
////                    .requestMatchers(Endpoints.GATEWAY_PREFIX + Endpoints.GET_EVENTS).hasAuthority(Constants.USER_ROLE) // Require USER role for /api/events
////                    .anyRequest().authenticated()             // Both roles can access
//            );
//
//        return http.build();
//    }
//
//    // Used by Spring Security if CORS is enabled.
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        // Allow all origins from http://localhost:4200
//        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:8080"));
//
//        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
//        config.setAllowedOrigins(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
//
//        // Allow all headers
//        config.addAllowedHeader("*");
//
//        // Allow credentials (e.g., cookies)
//        config.setAllowCredentials(true);
//
//        // Set the max age for pre-flight requests in seconds
//        config.setMaxAge(3600L);
//
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}
//
