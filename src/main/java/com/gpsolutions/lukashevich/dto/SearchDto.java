package com.gpsolutions.lukashevich.dto;

import lombok.Value;

@Value
public class SearchDto {
    String name;
    String brand;
    String city;
    String country;
    String amenities;
}