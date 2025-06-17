package com.gpsolutions.lukashevich.services.Jpa;

import com.gpsolutions.lukashevich.entities.Amenity;
import com.gpsolutions.lukashevich.repositories.AmenityRepository;
import com.gpsolutions.lukashevich.services.AmenityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {
    private final AmenityRepository amenityRepository;

     @Transactional
    public Set<Amenity> findOrCreateAmenitiesByName(Collection<String> names) {
        if (names == null) {
            return new HashSet<>();
        }

        Set<Amenity> existingEntities = amenityRepository.findAllByNameIn(names);
        if (existingEntities.size() == names.size()) {
            return existingEntities;
        }

        List<Amenity> created = amenityRepository.saveAll(getToAdd(names, existingEntities));
        existingEntities.addAll(created);
        return existingEntities;
    }

    private static List<Amenity> getToAdd(Collection<String> allNames, Set<Amenity> existingEntities) {
        Set<String> existingNames = existingEntities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());
        return allNames.stream()
                .filter(x -> !existingNames.contains(x))
                .map(String::trim)
                .map(Amenity::new)
                .toList();
    }
}
