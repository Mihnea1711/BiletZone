//package com.example.gateway.business.services;
//
//import com.example.gateway.dtos.UserDto;
//import com.example.gateway.dtos.UserInfoDetails;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UserService userService;
//
//    @Autowired
//    public CustomUserDetailsService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserDto user = userService.findUserByEmail(email);
//        return new UserInfoDetails(user);
//    }
//}
