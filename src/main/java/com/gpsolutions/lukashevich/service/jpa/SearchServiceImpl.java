package com.gpsolutions.lukashevich.service.jpa;

import com.gpsolutions.lukashevich.dto.HotelShortenedDto;
import com.gpsolutions.lukashevich.dto.SearchDto;
import com.gpsolutions.lukashevich.mapper.HotelMapper;
import com.gpsolutions.lukashevich.repository.HotelRepository;
import com.gpsolutions.lukashevich.repository.specification.HotelSpecificationMapper;
import com.gpsolutions.lukashevich.service.SearchService;
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
