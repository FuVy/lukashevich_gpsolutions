package com.gpsolutions.lukashevich_test.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacts {
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "email", nullable = false)
    private String email;
}