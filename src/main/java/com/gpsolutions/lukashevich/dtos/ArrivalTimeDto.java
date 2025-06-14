package com.gpsolutions.lukashevich.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalTime;

@Value
public class ArrivalTimeDto {
    @NotNull(message = "Check in time can't be null.")
    LocalTime checkIn;
    LocalTime checkOut;
}