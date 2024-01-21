package com.example.idm.business.services;

import com.example.idm.business.interfaces.IUserService;
import com.example.idm.business.validation.Validation;
import com.example.idm.dtos.UserDto;
import com.example.idm.models.User;
import com.example.idm.persistence.interfaces.IUserRepository;
import com.example.idm.persistence.mappers.UserMapper;
import com.example.idm.utils.Cryptography;
import com.example.idm.utils.JWT;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements IUserService {
    private final IUserRepository _userRepository;
    private final UserMapper _userMapper;

    public UserService(IUserRepository userRepository, UserMapper userMapper) {
        this._userRepository = userRepository;
        this._userMapper = userMapper;
    }

    @Override
    public String createUser(UserDto userDto) {
        User user = this._userMapper.dtoToEntity(userDto);

        // Validate the user data
        Validation.validateUser(user);

        // Hash the password before saving
        String encryptedPassword = Cryptography.hashPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        // Save the user
        User addedUser = this._userRepository.save(user);
        // Validate and return the UUID of the newly created user
        if (addedUser.getUuid() != null) {
            // Log user creation
            log.info("User created: {}", user.getEmail());

            return addedUser.getUuid();
        } else {
            return "";
        }
    }

    @Override
    public List<User> getAllUsers() {
        return this._userRepository.findAll();
    }

    @Override
    public List<UserDto> getUserForNotifications() {
        return _userRepository.findAllUsersWithNotificationsEnabled().stream().map(_userMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(UUID id) {
        return _userRepository.findById(id).map(_userMapper::entityToDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return _userRepository.findByEmail(email).map(_userMapper::entityToDto);
    }

    @Override
    public void updateUser(UUID id, User user) {}

    @Transactional
    @Override
    public void deleteUser(String id) {
        this._userRepository.deleteByUuid(id);
    }

    @Override
    public String loginUser(String email, String password) {
        // Validate the user data
        Validation.validateCredentials(email, password);

        Optional<User> dbUser = this._userRepository.findByEmail(email);
        User user = dbUser.orElseThrow(() -> new EntityNotFoundException("User not found"));

//         uncomment here to toggle email confirmation
//        if (!user.isConfirmed()) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is not confirmed. Please check your email for the confirmation link.");
//        }

        // Check if the provided password matches the stored hashed password
        boolean isPasswordMatch = Cryptography.checkPassword(password, user.getPassword());

        if (isPasswordMatch) {
            // Passwords match, user is authenticated
            // For simplicity, we'll just log a success message
            log.info("User '{}' successfully logged in", email);
            return JWT.createToken(user);
        } else {
            // Passwords don't match, authentication failed
            // For simplicity, we'll just log a failure message
            log.warn("Authentication failed for user '{}'", email);
            // You might want to throw an exception or handle the failure in some other way
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public UserDto confirmEmail(String token) {
        try {
        Optional<User> userOptional = _userRepository.findByConfirmationToken(token);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setConfirmed(true);
                User updatedUser = this._userRepository.save(user);

                return this._userMapper.entityToDto(updatedUser);
            } else {
                log.warn("No user found with token {} to set confirmed status.", token);
                throw new EntityNotFoundException("No event found with the specified token.");
            }
        } catch (Exception ex) {
            log.error("An unexpected error occurred while updating favourite status for event with token {}.", token, ex);
            throw new RuntimeException("Failed to set confirmed status for the user.", ex);
        }
    }
}
