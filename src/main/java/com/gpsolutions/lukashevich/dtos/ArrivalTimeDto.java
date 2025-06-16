package com.gpsolutions.lukashevich.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalTime;

@Value
public class ArrivalTimeDto {
    @NotNull(message = "Check in time can't be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime checkIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime checkOut;
}