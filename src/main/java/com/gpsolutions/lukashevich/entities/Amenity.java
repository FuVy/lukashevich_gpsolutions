package com.gpsolutions.lukashevich.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@Table(name = "amenities")
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "amenity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<HotelAmenityItem> amenities = new HashSet<>();

    public Amenity(String name) {
        this.name = name;
    }
}