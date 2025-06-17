package com.gpsolutions.lukashevich.services.Jpa;

import com.gpsolutions.lukashevich.repositories.HotelAmenityItemRepository;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import com.gpsolutions.lukashevich.services.HistogramService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistogramServiceImpl implements HistogramService {
    private final HotelRepository hotelRepository;
    private final HotelAmenityItemRepository hotelAmenityItemRepository;

    private Map<String, Supplier<List<Pair<String, Long>>>> supplierMap;

    @PostConstruct
    public void init() {
        this.supplierMap = Map.of(
                "brand", hotelRepository::countHotelsByBrand,
                "city", hotelRepository::countHotelsByCity,
                "country", hotelRepository::countHotelsByCountry,
                "amenities", hotelAmenityItemRepository::countHotelsByAmenity);
    }


    @Transactional(readOnly = true)
    public Map<String, Long> getHistogramByParameter(String param) {
        return Optional.ofNullable(param)
                .map(supplierMap::get)
                .map(Supplier::get)
                .map(HistogramServiceImpl::toMap)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or unknown parameter given."));
    }

    public static Map<String, Long> toMap(List<Pair<String, Long>> pairs) {
        return pairs.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }
}
