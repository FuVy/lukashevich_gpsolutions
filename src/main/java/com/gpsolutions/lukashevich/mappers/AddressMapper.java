package com.gpsolutions.lukashevich.mappers;

import com.gpsolutions.lukashevich.dtos.AddressDto;
import com.gpsolutions.lukashevich.entities.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressDto dto);
    AddressDto toDto(Address entity);
}
