package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.domain.search.FailedSearchItem;
import com.gpsolutions.lukashevich.dtos.AddAmenitiesRequest;
import com.gpsolutions.lukashevich.dtos.HotelFullDto;
import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.entities.Amenity;
import com.gpsolutions.lukashevich.entities.Hotel;
import com.gpsolutions.lukashevich.entities.HotelAmenityItem;
import com.gpsolutions.lukashevich.exceptions.NotFoundException;
import com.gpsolutions.lukashevich.mappers.HotelMapper;
import com.gpsolutions.lukashevich.repositories.HotelAmenityItemRepository;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final AmenityService amenityService;

    private final HotelMapper hotelMapper;

    private final HotelRepository hotelRepository;
    private final HotelAmenityItemRepository hotelAmenityItemRepository;

    public List<HotelShortenedDto> findAllHotels() {
        return hotelRepository.findAll().stream().map(hotelMapper::toShortenedDto).toList();
    }

    public HotelFullDto getHotelFullData(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(List.of(new FailedSearchItem(
                        "id",
                        id.toString(),
                        "Unknown hotel id."
                ))));
        return hotelMapper.toDto(hotel);
    }

    @Transactional
    public HotelShortenedDto addHotel(HotelFullDto hotel) {
        Hotel entity = hotelMapper.toEntityWithoutAmenities(hotel);
        entity = hotelRepository.save(entity);
        entity = addAmenities(entity, hotel.getAmenities());
        return hotelMapper.toShortenedDto(entity);
    }

    @Transactional
    public void addAmenities(Long id, AddAmenitiesRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(List.of(new FailedSearchItem(
                                "id",
                                id.toString(),
                                "Unknown hotel id."
                        ))));
        addAmenities(hotel, request.getAmenities());
    }

    @Transactional
    private Hotel addAmenities(Hotel hotel, List<String> amenityNames) {
        if (amenityNames == null) {
            hotel.setAmenities(new ArrayList<>());
            return hotelRepository.save(hotel);
        }
        Set<String> existingAmenities = hotel.getAmenities().stream().map(x -> x.getAmenity().getName()).collect(Collectors.toSet());
        amenityNames = amenityNames.stream().filter(x -> !existingAmenities.contains(x)).toList();
        Set<Amenity> amenities = amenityService.findOrCreateAmenitiesByName(amenityNames);
        List<HotelAmenityItem> pairs = amenities.stream()
                .map(x -> new HotelAmenityItem(hotel, x))
                .toList();
        pairs = new ArrayList<>(hotelAmenityItemRepository.saveAll(pairs));
        hotel.getAmenities().addAll(pairs);
        return hotelRepository.save(hotel);
    }
}