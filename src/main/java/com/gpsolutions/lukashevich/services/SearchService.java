package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.entities.Amenity;
import com.gpsolutions.lukashevich.entities.Hotel;
import com.gpsolutions.lukashevich.entities.HotelAmenityItem;
import com.gpsolutions.lukashevich.mappers.HotelMapper;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public List<HotelShortenedDto> searchHotels(String name, String brand, String city, String country, String amenities) {
        Specification<Hotel> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(brand)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(city)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(country)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(amenities)) {
                List<String> requiredAmenityNames = Arrays.stream(amenities.split(","))
                        .map(String::trim)
                        .filter(StringUtils::hasText)
                        .map(String::toLowerCase)
                        .toList();
                Join<Hotel, HotelAmenityItem> hotelAmenityJoin = root.join("amenities", JoinType.INNER);
                Join<HotelAmenityItem, Amenity> amenityJoin = hotelAmenityJoin.join("amenity", JoinType.INNER);
                predicates.add(criteriaBuilder.lower(amenityJoin.get("name")).in(requiredAmenityNames));
                query.groupBy(root.get("id"));
                query.having(criteriaBuilder.equal(
                        criteriaBuilder.countDistinct(amenityJoin.get("id")),
                        requiredAmenityNames.size()
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Hotel> hotels = hotelRepository.findAll(spec);
        return hotels.stream().map(hotelMapper::toShortenedDto).toList();
    }
}
