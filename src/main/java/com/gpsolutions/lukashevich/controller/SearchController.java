package com.gpsolutions.lukashevich.controller;

import com.gpsolutions.lukashevich.dto.HotelShortenedDto;
import com.gpsolutions.lukashevich.dto.SearchDto;
import com.gpsolutions.lukashevich.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Search hotels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortenedDto[].class)))
    })
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
        return ResponseEntity.ok(searchService.searchHotels(new SearchDto(name, brand, city, country, amenities)));
    }
}
