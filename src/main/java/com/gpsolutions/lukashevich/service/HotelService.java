package com.gpsolutions.lukashevich.service;

import com.gpsolutions.lukashevich.dto.HotelFullDto;
import com.gpsolutions.lukashevich.dto.HotelShortenedDto;

import java.util.List;

public interface HotelService {

  List<HotelShortenedDto> findAllHotels();

  HotelFullDto getHotelFullData(Long id);

  HotelShortenedDto addHotel(HotelFullDto hotel);

  void addAmenities(Long id, List<String> amenities);
}
