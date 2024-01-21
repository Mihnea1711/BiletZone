package com.example.database.dtos;

import com.example.database.models.HallMap;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link HallMap}
 */
public record HallMapDto(
        Long id,
        @NotNull(message = "Rows cannot be null") @NotEmpty(message = "Rows cannot be empty") int rows,
        @NotNull(message = "Columns cannot be null") @NotEmpty(message = "Columns cannot be empty") int columns
    ) implements Serializable {
}