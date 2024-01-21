package com.example.database.models;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "purchased_tickets")
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    @NotNull(message = "Ticket ID cannot be null")
    private Ticket ticket;


    @JoinColumn(name = "useruuid", referencedColumnName = "useruuid")
    @NotEmpty(message = "User UUID cannot be null")
    private String userUUID;

    @Column(name = "quantity")
    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    // Parameterized constructor (if needed)
    public PurchasedTicket(Ticket ticket, String userUUID, int quantity) {
        this.ticket = ticket;
        this.userUUID = userUUID;
        this.quantity = quantity;
    }


}