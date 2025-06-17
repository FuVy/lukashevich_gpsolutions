package com.gpsolutions.lukashevich.dtos;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class SearchDto {
    String name;
    String brand;
    String city;
    String country;
    String amenities;
}