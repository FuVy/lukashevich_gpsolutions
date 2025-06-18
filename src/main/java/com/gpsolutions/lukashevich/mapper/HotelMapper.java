package com.gpsolutions.lukashevich.mapper;

import com.gpsolutions.lukashevich.dto.HotelFullDto;
import com.gpsolutions.lukashevich.dto.HotelShortenedDto;
import com.gpsolutions.lukashevich.entity.Address;
import com.gpsolutions.lukashevich.entity.Hotel;
import com.gpsolutions.lukashevich.entity.HotelAmenityItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
            AddressMapper.class,
            ContactsMapper.class,
            ArrivalTimeMapper.class
        })
public interface HotelMapper {
    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "mapAmenitiesToDto")
    HotelFullDto toDto(Hotel hotel);

    @Mapping(target = "address", source = "address", qualifiedByName = "mapAddressToString")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortenedDto toShortenedDto(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntityWithoutAmenities(HotelFullDto dto);

    @Named("mapAmenitiesToDto")
    default List<String> mapAmenitiesToDto(List<HotelAmenityItem> amenities) {
        if (amenities == null) {
            return null;
        }
        return amenities.stream()
                .map(x -> x.getAmenity().getName())
                .collect(Collectors.toList());
    }

    @Named("mapAddressToString")
    default String mapAddressToString(Address address) {
        if (address == null) {
            return null;
        }
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }
}
