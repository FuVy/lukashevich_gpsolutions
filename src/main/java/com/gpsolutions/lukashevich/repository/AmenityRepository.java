package com.gpsolutions.lukashevich.repository;

import com.gpsolutions.lukashevich.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Set<Amenity> findAllByNameIn(Collection<String> names);
}