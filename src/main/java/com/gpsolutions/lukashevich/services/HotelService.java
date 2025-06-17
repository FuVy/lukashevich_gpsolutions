package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.dtos.AddAmenitiesRequest;
import com.gpsolutions.lukashevich.dtos.HotelFullDto;
import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;

import java.util.List;

public interface HotelService {

  List<HotelShortenedDto> findAllHotels();

  HotelFullDto getHotelFullData(Long id);

  HotelShortenedDto addHotel(HotelFullDto hotel);

  void addAmenities(Long id, AddAmenitiesRequest request);
}
