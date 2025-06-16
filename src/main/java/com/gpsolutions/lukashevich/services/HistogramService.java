package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.repositories.HotelAmenityItemRepository;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import com.gpsolutions.lukashevich.utils.converters.PairsConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistogramService {
    private final HotelRepository hotelRepository;
    private final HotelAmenityItemRepository hotelAmenityItemRepository;

    public Map<String, Integer> getHistogramByParameter(String param) {
        if (param.equalsIgnoreCase("brand")) {
            return PairsConverter.toMap(hotelRepository.countHotelsByBrand());
        }
        if (param.equalsIgnoreCase("city")) {
            return PairsConverter.toMap(hotelRepository.countHotelsByCity());
        }
        if (param.equalsIgnoreCase("country")) {
            return PairsConverter.toMap(hotelRepository.countHotelsByCountry());
        }
        if (param.equalsIgnoreCase("amenities")) {
            return PairsConverter.toMap(hotelAmenityItemRepository.countHotelsByAmenity());
        }
        throw new IllegalArgumentException("Invalid or unknown parameter given.");
    }
}
