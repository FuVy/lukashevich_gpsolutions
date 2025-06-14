package com.gpsolutions.lukashevich_test.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
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