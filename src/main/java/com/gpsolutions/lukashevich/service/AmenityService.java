package com.gpsolutions.lukashevich.service;

import com.gpsolutions.lukashevich.entity.Amenity;

import java.util.Collection;
import java.util.Set;

public interface AmenityService {

  Set<Amenity> findOrCreateAmenitiesByName(Collection<String> names);
}
