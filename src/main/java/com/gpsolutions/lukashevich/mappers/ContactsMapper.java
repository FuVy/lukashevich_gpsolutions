package com.gpsolutions.lukashevich.mappers;

import com.gpsolutions.lukashevich.dtos.ContactsDto;
import com.gpsolutions.lukashevich.entities.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {
    Contacts toEntity(ContactsDto dto);
    ContactsDto toDto(Contacts entity);
}
