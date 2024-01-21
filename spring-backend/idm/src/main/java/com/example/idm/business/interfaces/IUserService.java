package com.example.idm.business.interfaces;

import com.example.idm.dtos.UserDto;
import com.example.idm.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    String createUser(UserDto userDto);

    List<User> getAllUsers();

    List<UserDto> getUserForNotifications();

    Optional<UserDto> getUserById(UUID id);

    Optional<UserDto> getUserByEmail(String email);

    void updateUser(UUID id, User user);

    void deleteUser(String id);

    String loginUser(String email, String password);

    UserDto confirmEmail(String token);
}
