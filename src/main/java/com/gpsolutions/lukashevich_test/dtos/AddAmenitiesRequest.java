package com.gpsolutions.lukashevich_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class AddAmenitiesRequest {
    @NotNull(message = "Amenity list can't be null.")
    List<@NotNull(message = "Amenity name can't be null.")
            @NotEmpty(message = "Amenity name can't be empty.")
            @NotBlank(message = "Amenity name can't be empty.")
            String> amenities;
}