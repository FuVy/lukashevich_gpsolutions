package com.gpsolutions.lukashevich.services.Jpa;

import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.dtos.SearchDto;
import com.gpsolutions.lukashevich.mappers.HotelMapper;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import com.gpsolutions.lukashevich.repositories.specification.HotelSpecificationMapper;
import com.gpsolutions.lukashevich.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final HotelSpecificationMapper specificationMapper;

    @Transactional(readOnly = true)
    public List<HotelShortenedDto> searchHotels(SearchDto searchDto) {
        return Optional.ofNullable(searchDto)
                .map(specificationMapper::getHotelSpecification)
                .map(hotelRepository::findAll)
                .stream()
                .flatMap(Collection::stream)
                .map(hotelMapper::toShortenedDto)
                .toList();
    }
}
