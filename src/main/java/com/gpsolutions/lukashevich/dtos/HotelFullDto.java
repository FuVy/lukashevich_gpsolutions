package com.gpsolutions.lukashevich.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class HotelFullDto {
    Long id;
    @NotNull(message = "Name can't be null.")
    @NotEmpty(message = "Name can't be empty.")
    @NotBlank(message = "Name can't be empty.")
    String name;
    @NotEmpty(message = "Description can't be empty.")
    @NotBlank(message = "Description can't be empty.")
    String description;
    @NotNull(message = "Brand can't be null.")
    @NotEmpty(message = "Brand can't be empty.")
    @NotBlank(message = "Brand can't be empty.")
    String brand;
    @NotNull(message = "Address can't be null.")
    AddressDto address;
    @NotNull(message = "Contacts can't be null.")
    ContactsDto contacts;
    @NotNull(message = "Arrival data can't be null.")
    ArrivalTimeDto arrivalTime;
    List<@NotNull(message = "Amenity name can't be null.")
                @NotEmpty(message = "Amenity name can't be empty.")
                @NotBlank(message = "Amenity name can't be empty.")
                String> amenities;
}