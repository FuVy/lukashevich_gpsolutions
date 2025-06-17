package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.entities.Amenity;

import java.util.Collection;
import java.util.Set;

public interface AmenityService {

  Set<Amenity> findOrCreateAmenitiesByName(Collection<String> names);
}
