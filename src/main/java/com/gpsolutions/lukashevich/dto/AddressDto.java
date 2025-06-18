package com.gpsolutions.lukashevich.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class AddressDto {
    @NotNull(message = "House number can't be null.")
    Integer houseNumber;
    @NotNull(message = "Street can't be null.")
    @NotEmpty(message = "Street can't be empty.")
    @NotBlank(message = "Street can't be empty.")
    String street;
    @NotNull(message = "City can't be null.")
    @NotEmpty(message = "City can't be empty.")
    @NotBlank(message = "City can't be empty.")
    String city;
    @NotNull(message = "Country can't be null.")
    @NotEmpty(message = "Country can't be empty.")
    @NotBlank(message = "Country can't be empty.")
    String country;
    @NotNull(message = "Postcode can't be null.")
    @NotEmpty(message = "Postcode can't be empty.")
    @NotBlank(message = "Postcode can't be empty.")
    String postCode;
}