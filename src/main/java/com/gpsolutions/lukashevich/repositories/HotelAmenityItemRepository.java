package com.gpsolutions.lukashevich.repositories;

import com.gpsolutions.lukashevich.entities.HotelAmenityItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.util.List;

public interface HotelAmenityItemRepository extends JpaRepository<HotelAmenityItem, Long> {
    @Query("SELECT pair.amenity.name, COUNT(DISTINCT pair.hotel.id) FROM HotelAmenityItem pair GROUP BY pair.amenity.name")
    List<Pair<String, Long>> countHotelsByAmenity();
}