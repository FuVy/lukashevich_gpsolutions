package com.gpsolutions.lukashevich.repositories;

import com.gpsolutions.lukashevich.entities.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Set<Amenity> findAllByNameIn(Collection<String> names);
}