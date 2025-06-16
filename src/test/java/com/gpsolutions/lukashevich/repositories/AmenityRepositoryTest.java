package com.gpsolutions.lukashevich.repositories;

import com.gpsolutions.lukashevich.entities.Amenity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AmenityRepositoryTest {
    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.clear();
        entityManager.flush();
    }

    @Test
    void findAllByNameIn_shouldReturnExistingAmenities() {
        Amenity amenity1 = new Amenity("Free WiFi");
        Amenity amenity2 = new Amenity("Parking");
        Amenity amenity3 = new Amenity("Pool");

        entityManager.persist(amenity1);
        entityManager.persist(amenity2);
        entityManager.persist(amenity3);
        entityManager.flush();

        Set<String> namesToFind = new HashSet<>(List.of("Free WiFi", "Pool", "Non-existent Amenity"));
        Set<Amenity> foundAmenities = amenityRepository.findAllByNameIn(namesToFind);

        assertThat(foundAmenities).hasSize(2);
        assertThat(foundAmenities).extracting(Amenity::getName).containsExactlyInAnyOrder("Free WiFi", "Pool");
    }

    @Test
    void findAllByNameIn_shouldReturnEmptySet_whenNoAmenitiesMatch() {
        Amenity amenity1 = new Amenity("Gym");
        Amenity amenity2 = new Amenity("Spa");

        entityManager.persist(amenity1);
        entityManager.persist(amenity2);
        entityManager.flush();

        Set<String> namesToFind = new HashSet<>(List.of("Parking", "Pool"));
        Set<Amenity> foundAmenities = amenityRepository.findAllByNameIn(namesToFind);

        assertThat(foundAmenities).isEmpty();
    }

    @Test
    void findAllByNameIn_shouldReturnEmptySet_whenNamesCollectionIsEmpty() {
        Amenity amenity1 = new Amenity("Gym");
        entityManager.persist(amenity1);
        entityManager.flush();

        Set<String> namesToFind = Collections.emptySet();
        Set<Amenity> foundAmenities = amenityRepository.findAllByNameIn(namesToFind);

        assertThat(foundAmenities).isEmpty();
    }

    @Test
    void findAllByNameIn_shouldHandleCaseSensitivityBasedOnDatabaseCollation() {
        Amenity amenity1 = new Amenity("free wifi");
        Amenity amenity2 = new Amenity("Free WiFi");

        entityManager.persist(amenity1);
        entityManager.persist(amenity2);
        entityManager.flush();

        Set<String> namesToFind = new HashSet<>(List.of("Free WiFi"));
        Set<Amenity> foundAmenities = amenityRepository.findAllByNameIn(namesToFind);

        assertThat(foundAmenities).hasSize(1);
        assertThat(foundAmenities).extracting(Amenity::getName).containsExactlyInAnyOrder("Free WiFi");
    }
}