package com.gpsolutions.lukashevich.controllers;

import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/property-view/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<HotelShortenedDto>> search(
            @RequestParam(value = "name", required = false)
            String name,
            @RequestParam(value = "brand", required = false)
            String brand,
            @RequestParam(value = "city", required = false)
            String city,
            @RequestParam(value = "country", required = false)
            String country,
            @RequestParam(value = "amenities", required = false)
            String amenities
    ) {
        return ResponseEntity.ok(searchService.searchHotels(name, brand, city, country, amenities));
    }
}
