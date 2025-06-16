package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.entities.Amenity;
import com.gpsolutions.lukashevich.repositories.AmenityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityService {
    private final AmenityRepository amenityRepository;

    @Transactional
    public Set<Amenity> findOrCreateAmenitiesByName(Collection<String> names) {
        if (names == null) {
            return new HashSet<>();
        }
        Set<Amenity> entities = amenityRepository.findAllByNameIn(names);
        if (entities.size() == names.size()) {
            return entities;
        }

        Set<String> existingNames = entities.stream().map(Amenity::getName).collect(Collectors.toSet());
        List<Amenity> toAdd = names.stream()
                .filter(x -> !existingNames.contains(x))
                .map(x -> new Amenity(x.trim()))
                .toList();
        toAdd = amenityRepository.saveAll(toAdd);
        entities.addAll(toAdd);
        return entities;
    }
}
