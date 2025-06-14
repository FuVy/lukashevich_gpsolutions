package com.gpsolutions.lukashevich.mappers;

import com.gpsolutions.lukashevich.dtos.ArrivalTimeDto;
import com.gpsolutions.lukashevich.entities.ArrivalTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArrivalTimeMapper {
    ArrivalTime toEntity(ArrivalTimeDto dto);
    ArrivalTimeDto toDto(ArrivalTime entity);
}
