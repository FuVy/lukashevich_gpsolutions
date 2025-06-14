package com.gpsolutions.lukashevich.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ShortenedHotelDto {
    Long id;
    @NotNull(message = "Name can't be null.")
    @NotEmpty(message = "Name can't be empty.")
    @NotBlank(message = "Name can't be empty.")
    String name;
    @NotEmpty(message = "Description can't be empty.")
    @NotBlank(message = "Description can't be empty.")
    String description;
    @NotNull(message = "Address can't be null.")
    @NotBlank(message = "Address can't be empty.")
    @NotEmpty(message = "Address can't be empty.")
    String address;
    @NotNull(message = "Phone can't be null.")
    String phone;
}