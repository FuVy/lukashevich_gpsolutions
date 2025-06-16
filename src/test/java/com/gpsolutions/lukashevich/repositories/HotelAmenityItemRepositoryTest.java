package com.gpsolutions.lukashevich.repositories;

import com.gpsolutions.lukashevich.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HotelAmenityItemRepositoryTest {
    @Autowired
    private HotelAmenityItemRepository hotelAmenityItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Hotel hotel1;
    private Hotel hotel2;
    private Hotel hotel3;
    private Amenity amenityWifi;
    private Amenity amenityParking;
    private Amenity amenityPool;

    @BeforeEach
    void setUp() {
        entityManager.clear();
        entityManager.flush();

        hotel1 = new Hotel(null, "Hotel Alpha", "Desc1", "BrandA",
                new Address(1, "Street1", "City1", "Country1", "P1"),
                new Contacts("111", "a@a.com"), new ArrivalTime(LocalTime.of(10, 0), LocalTime.of(11, 0)), null);
        hotel2 = new Hotel(null, "Hotel Beta", "Desc2", "BrandB",
                new Address(2, "Street2", "City2", "Country2", "P2"),
                new Contacts("222", "b@b.com"), new ArrivalTime(LocalTime.of(12, 0), LocalTime.of(13, 0)), null);
        hotel3 = new Hotel(null, "Hotel Gamma", "Desc3", "BrandC",
                new Address(3, "Street3", "City3", "Country3", "P3"),
                new Contacts("333", "c@c.com"), new ArrivalTime(LocalTime.of(14, 0), LocalTime.of(15, 0)), null);


        amenityWifi = new Amenity("Free WiFi");
        amenityParking = new Amenity("Free parking");
        amenityPool = new Amenity("Pool");

        entityManager.persist(hotel1);
        entityManager.persist(hotel2);
        entityManager.persist(hotel3);
        entityManager.persist(amenityWifi);
        entityManager.persist(amenityParking);
        entityManager.persist(amenityPool);
        entityManager.flush();
    }

    @Test
    void countHotelsByAmenity_shouldReturnCorrectCounts() {
        entityManager.persist(new HotelAmenityItem(hotel1, amenityWifi));
        entityManager.persist(new HotelAmenityItem(hotel1, amenityParking));
        entityManager.persist(new HotelAmenityItem(hotel2, amenityWifi));
        entityManager.persist(new HotelAmenityItem(hotel2, amenityPool));
        entityManager.persist(new HotelAmenityItem(hotel3, amenityWifi));
        entityManager.flush();

        List<Pair<String, Long>> resultPairs = hotelAmenityItemRepository.countHotelsByAmenity();
        Map<String, Long> resultMap = resultPairs.stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        assertThat(resultMap).isNotNull().hasSize(3);
        assertThat(resultMap).containsEntry("Free WiFi", 3L);
        assertThat(resultMap).containsEntry("Free parking", 1L);
        assertThat(resultMap).containsEntry("Pool", 1L);
    }

    @Test
    void countHotelsByAmenity_shouldReturnEmptyList_whenNoAmenitiesAreAssigned() {
        List<Pair<String, Long>> result = hotelAmenityItemRepository.countHotelsByAmenity();

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void countHotelsByAmenity_shouldHandleSingleHotelSingleAmenity() {
        entityManager.persist(new HotelAmenityItem(hotel1, amenityWifi));
        entityManager.flush();

        List<Pair<String, Long>> resultPairs = hotelAmenityItemRepository.countHotelsByAmenity();
        Map<String, Long> resultMap = resultPairs.stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        assertThat(resultMap).isNotNull().hasSize(1);
        assertThat(resultMap).containsEntry("Free WiFi", 1L);
    }

    @Test
    void countHotelsByAmenity_shouldCountDistinctHotelsOnly() {
        entityManager.persist(new HotelAmenityItem(hotel1, amenityWifi));
        entityManager.persist(new HotelAmenityItem(hotel1, amenityWifi));
        entityManager.persist(new HotelAmenityItem(hotel2, amenityWifi));
        entityManager.flush();

        List<Pair<String, Long>> resultPairs = hotelAmenityItemRepository.countHotelsByAmenity();
        Map<String, Long> resultMap = resultPairs.stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        assertThat(resultMap).isNotNull().hasSize(1);
        assertThat(resultMap).containsEntry("Free WiFi", 2L);
    }

    @Test
    void saveHotelAmenityItem_shouldPersistCorrectly() {
        HotelAmenityItem newItem = new HotelAmenityItem(hotel1, amenityPool);
        HotelAmenityItem savedItem = hotelAmenityItemRepository.save(newItem);
        entityManager.flush();
        entityManager.clear();

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();

        HotelAmenityItem found = entityManager.find(HotelAmenityItem.class, savedItem.getId());
        assertThat(found).isNotNull();
        assertThat(found.getHotel().getName()).isEqualTo(hotel1.getName());
        assertThat(found.getAmenity().getName()).isEqualTo(amenityPool.getName());
    }
}