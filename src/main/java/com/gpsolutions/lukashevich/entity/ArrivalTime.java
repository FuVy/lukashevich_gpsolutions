package com.gpsolutions.lukashevich.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {
    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;
}