package com.gpsolutions.lukashevich.repositories;

import com.gpsolutions.lukashevich.entities.Address;
import com.gpsolutions.lukashevich.entities.ArrivalTime;
import com.gpsolutions.lukashevich.entities.Contacts;
import com.gpsolutions.lukashevich.entities.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HotelRepositoryTest {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();
        hotelRepository.flush();
        entityManager.clear();
        entityManager.flush();

        Hotel hotel1 = new Hotel(null, "Hilton Minsk", "Desc1", "Hilton",
                new Address(1, "Street1", "Minsk", "Belarus", "P1"),
                new Contacts("111", "a@a.com"), new ArrivalTime(LocalTime.of(10, 0), LocalTime.of(11, 0)), new ArrayList<>());
        Hotel hotel2 = new Hotel(null, "Marriott Warsaw", "Desc2", "Marriott",
                new Address(2, "Street2", "Warsaw", "Poland", "P2"),
                new Contacts("222", "b@b.com"), new ArrivalTime(LocalTime.of(12, 0), LocalTime.of(13, 0)), new ArrayList<>());
        Hotel hotel3 = new Hotel(null, "Hilton London", "Desc3", "Hilton",
                new Address(3, "Street3", "London", "UK", "P3"),
                new Contacts("333", "c@c.com"), new ArrivalTime(LocalTime.of(14, 0), LocalTime.of(15, 0)), new ArrayList<>());
        Hotel hotel4 = new Hotel(null, "Hilton Paris", "Desc4", "Hilton",
                new Address(4, "Street4", "Paris", "France", "P4"),
                new Contacts("444", "d@d.com"), new ArrivalTime(LocalTime.of(16, 0), LocalTime.of(17, 0)), new ArrayList<>());
        Hotel hotel5 = new Hotel(null, "Radisson Minsk", "Desc5", "Radisson",
                new Address(5, "Street5", "Minsk", "Belarus", "P5"),
                new Contacts("555", "e@e.com"), new ArrivalTime(LocalTime.of(18, 0), LocalTime.of(19, 0)), new ArrayList<>());

        hotelRepository.saveAll(List.of(hotel1, hotel2, hotel3, hotel4, hotel5));

        entityManager.flush();
    }

    @Test
    void countHotelsByBrand_shouldReturnCorrectCounts() {
        List<Pair<String, Long>> resultPairs = hotelRepository.countHotelsByBrand();
        Map<String, Long> resultMap = resultPairs.stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));


        assertThat(resultMap).isNotNull().hasSize(3);
        assertThat(resultMap).containsEntry("Hilton", 3L);
        assertThat(resultMap).containsEntry("Marriott", 1L);
        assertThat(resultMap).containsEntry("Radisson", 1L);
    }

    @Test
    void countHotelsByCity_shouldReturnCorrectCounts() {
        List<Pair<String, Long>> resultPairs = hotelRepository.countHotelsByCity();
        Map<String, Long> resultMap = resultPairs.stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        assertThat(resultMap).isNotNull().hasSize(4);
        assertThat(resultMap).containsEntry("Minsk", 2L);
        assertThat(resultMap).containsEntry("Warsaw", 1L);
        assertThat(resultMap).containsEntry("London", 1L);
        assertThat(resultMap).containsEntry("Paris", 1L);
    }

    @Test
    void countHotelsByCountry_shouldReturnCorrectCounts() {
        List<Pair<String, Long>> resultPairs = hotelRepository.countHotelsByCountry();
        Map<String, Long> resultMap = resultPairs.stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        assertThat(resultMap).isNotNull().hasSize(4);
        assertThat(resultMap).containsEntry("Belarus", 2L);
        assertThat(resultMap).containsEntry("Poland", 1L);
        assertThat(resultMap).containsEntry("UK", 1L);
        assertThat(resultMap).containsEntry("France", 1L);
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Hotel> foundHotel = hotelRepository.findById(99L);
        assertThat(foundHotel).isEmpty();
    }

    @Test
    void save_shouldPersistHotel() {
        Hotel newHotel = new Hotel(null, "New Inn", "New Desc", "Inn Brand",
                new Address(10, "New Street", "New City", "New Country", "N1"),
                new Contacts("999", "new@example.com"), new ArrivalTime(LocalTime.of(9, 0), LocalTime.of(10, 0)), null);

        Hotel savedHotel = hotelRepository.save(newHotel);
        entityManager.flush();
        entityManager.clear();

        assertThat(savedHotel).isNotNull();
        assertThat(savedHotel.getId()).isNotNull();
        Hotel found = entityManager.find(Hotel.class, savedHotel.getId());
        assertThat(found.getName()).isEqualTo("New Inn");
    }
}