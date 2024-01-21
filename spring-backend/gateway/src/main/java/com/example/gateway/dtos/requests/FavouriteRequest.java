package com.example.gateway.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FavouriteRequest {
    @NotNull(message = "isFavourite cannot be null")
    @NotEmpty(message = "isFavourite cannot be empty")
    private Boolean isFavourite;
}
