package com.example.database.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Basic
    @Column(name = "name")
    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Basic
    @Column(name = "price")
    @NotNull(message = "Price cannot be null")
    //@NotEmpty(message = "Price cannot be empty")
    private Double price;

    @Basic
    @Column(name = "quantity")
    @NotNull(message = "Quantity cannot be null")
    //@NotEmpty(message = "Quantity cannot be empty")
    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event", referencedColumnName = "event_id")
    private Event event;

    public Ticket(String name, Double price, int quantity, Event event) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.event = event;
    }
}