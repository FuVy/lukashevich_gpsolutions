package com.gpsolutions.lukashevich.service;

import com.gpsolutions.lukashevich.dto.HotelShortenedDto;
import com.gpsolutions.lukashevich.dto.SearchDto;

import java.util.List;

public interface SearchService {

  List<HotelShortenedDto> searchHotels(SearchDto searchDto);
}
