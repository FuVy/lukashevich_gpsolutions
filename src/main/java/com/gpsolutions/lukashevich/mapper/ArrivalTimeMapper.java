package com.gpsolutions.lukashevich.mapper;

import com.gpsolutions.lukashevich.dto.ArrivalTimeDto;
import com.gpsolutions.lukashevich.entity.ArrivalTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArrivalTimeMapper {
    ArrivalTime toEntity(ArrivalTimeDto dto);
    ArrivalTimeDto toDto(ArrivalTime entity);
}
