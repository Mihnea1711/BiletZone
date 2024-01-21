package com.example.database.persistence.interfaces;

import com.example.database.models.PurchasedTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPurchasedTicketRepository extends JpaRepository<PurchasedTicket, Long> {
    List<PurchasedTicket> findByUserUUID(String userUUID);
}
