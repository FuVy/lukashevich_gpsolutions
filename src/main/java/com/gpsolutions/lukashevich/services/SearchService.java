package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.dtos.SearchDto;

import java.util.List;

public interface SearchService {

  List<HotelShortenedDto> searchHotels(SearchDto searchDto);
}
