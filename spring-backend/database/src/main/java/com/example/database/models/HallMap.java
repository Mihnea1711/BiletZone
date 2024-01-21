package com.example.database.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "hall_map")
@NoArgsConstructor
@AllArgsConstructor
public class HallMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_map_id")
    private Long id;

    @Basic
    @Column(name = "num_rows")
    @NotNull(message = "Rows cannot be null")
    @NotEmpty(message = "Rows cannot be empty")
    private int numRows;

    @Basic
    @Column(name = "num_columns")
    @NotNull(message = "Columns cannot be null")
    @NotEmpty(message = "Columns cannot be empty")
    private int numColumns;
}
