package com.gpsolutions.lukashevich.repository;

import com.gpsolutions.lukashevich.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("SELECT h.brand, COUNT(h) FROM Hotel h GROUP BY h.brand")
    List<Pair<String, Long>> countHotelsByBrand();

    @Query("SELECT h.address.city, COUNT(h) FROM Hotel h GROUP BY h.address.city")
    List<Pair<String, Long>> countHotelsByCity();

    @Query("SELECT h.address.country, COUNT(h) FROM Hotel h GROUP BY h.address.country")
    List<Pair<String, Long>> countHotelsByCountry();
}