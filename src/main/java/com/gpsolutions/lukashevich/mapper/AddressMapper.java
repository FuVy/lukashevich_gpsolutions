package com.gpsolutions.lukashevich.mapper;

import com.gpsolutions.lukashevich.dto.AddressDto;
import com.gpsolutions.lukashevich.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressDto dto);
    AddressDto toDto(Address entity);
}
