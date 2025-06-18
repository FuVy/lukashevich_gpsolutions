package com.gpsolutions.lukashevich.mapper;

import com.gpsolutions.lukashevich.dto.ContactsDto;
import com.gpsolutions.lukashevich.entity.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {
    Contacts toEntity(ContactsDto dto);
    ContactsDto toDto(Contacts entity);
}
