package com.gpsolutions.lukashevich.repositories.specification;


import com.gpsolutions.lukashevich.dtos.SearchDto;
import com.gpsolutions.lukashevich.entities.Amenity;
import com.gpsolutions.lukashevich.entities.Hotel;
import com.gpsolutions.lukashevich.entities.HotelAmenityItem;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class HotelSpecificationMapper {

    public Specification<Hotel> getHotelSpecification(SearchDto searchDto) {
        return byName(searchDto.getName())
                .or(byBrand(searchDto.getBrand()))
                .or(byCity(searchDto.getCity()))
                .or(byCountry(searchDto.getCountry()))
                .or(byAmenities(searchDto.getAmenities()));
    }

    private static Specification<Hotel> byName(String name) {
        return getByFieldLike(root -> root.get("name"), name);
    }

    private static Specification<Hotel> byBrand(String brand) {
        return getByFieldLike(root -> root.get("brand"), brand);
    }

    private static Specification<Hotel> byCity(String city) {
        return getByFieldLike(root -> root.get("address").get("city"), city);
    }

    private static Specification<Hotel> byCountry(String country) {
        return getByFieldLike(root -> root.get("address").get("country"), country);
    }

    private static Specification<Hotel> byAmenities(String amenities) {
        if (amenities == null) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        }
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            List<String> requiredAmenityNames =
                    Arrays.stream(amenities.split(","))
                            .map(String::trim)
                            .filter(StringUtils::hasText)
                            .map(String::toLowerCase)
                            .toList();
            Join<Hotel, HotelAmenityItem> hotelAmenityJoin = root.join("amenities", JoinType.INNER);
            Join<HotelAmenityItem, Amenity> amenityJoin = hotelAmenityJoin.join("amenity", JoinType.INNER);
            predicates.add(criteriaBuilder.lower(amenityJoin.get("name")).in(requiredAmenityNames));
            query.groupBy(root.get("id"));
            query.having(criteriaBuilder.equal(criteriaBuilder.countDistinct(amenityJoin.get("id")), requiredAmenityNames.size()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<Hotel> getByFieldLike(Function<Root<Hotel>, Path<String>> path, String valueDto) {
        return Optional.ofNullable(valueDto)
                .map(String::toLowerCase)
                .map(filterValue -> getSpecification(path, filterValue))
                .orElseGet(HotelSpecificationMapper::falseSpecification);
    }

    private static Specification<Hotel> falseSpecification() {
        return (r, cq, cb) -> cb.or();
    }

    private static Specification<Hotel> getSpecification(Function<Root<Hotel>, Path<String>> getPath, Object value) {
        return (root, query, cb) -> cb.like(getPath.apply(root), "%" + value + "%");
    }
}