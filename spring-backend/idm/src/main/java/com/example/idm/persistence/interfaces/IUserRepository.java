package com.example.idm.persistence.interfaces;

import com.example.idm.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmationToken(String confirmationToken);

    void deleteByUuid(String uuid);

    @Query("SELECT u FROM User u WHERE u.isNotificationsEnabled = true")
    List<User> findAllUsersWithNotificationsEnabled();
}
