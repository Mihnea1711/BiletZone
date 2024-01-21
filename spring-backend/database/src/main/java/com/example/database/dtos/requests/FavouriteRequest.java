package com.example.database.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteRequest {
    @NotNull(message = "isFavourite cannot be null")
    private Boolean isFavourite;

    @Override
    public String toString() {
        return "FavouriteRequest{" +
                "isFavourite=" + isFavourite +
                '}';
    }
}