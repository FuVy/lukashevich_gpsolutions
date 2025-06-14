package com.gpsolutions.lukashevich.mappers;

import com.gpsolutions.lukashevich.dtos.HotelFullDto;
import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.entities.Address;
import com.gpsolutions.lukashevich.entities.Hotel;
import com.gpsolutions.lukashevich.entities.HotelAmenityItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
            AddressMapper.class,
            ContactsMapper.class,
            ArrivalTimeMapper.class
        })
public interface HotelMapper {
    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "mapAmenitiesToDto")
    HotelFullDto hotelEntityToDto(Hotel hotel);

    @Mapping(target = "address", source = "address", qualifiedByName = "mapAddressToString")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortenedDto toShortenedDto(Hotel hotel);

    @Named("mapAmenitiesToDto")
    default List<String> mapAmenitiesToDto(Set<HotelAmenityItem> amenities) {
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
