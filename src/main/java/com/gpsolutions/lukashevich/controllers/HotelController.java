package com.gpsolutions.lukashevich.controllers;

import com.gpsolutions.lukashevich.dtos.AddAmenitiesRequest;
import com.gpsolutions.lukashevich.dtos.HotelFullDto;
import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.services.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/property-view/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<?> returnAllHotels() {
        return ResponseEntity.ok(hotelService.findAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelFullDto> returnHotelFullInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hotelService.getHotelFullData(id));
    }

    @PostMapping
    public ResponseEntity<HotelShortenedDto> addHotel(
            @NotNull(message = "Request can't be null.")
            @Valid
            @RequestBody
            HotelFullDto hotel
    ) {
        return ResponseEntity.ok(hotelService.addHotel(hotel));
    }

    @PostMapping("/{id}/amenities")
    public ResponseEntity<?> addAmenities(
            @PathVariable("id")
            Long id,
            @NotNull(message = "Request can't be null.")
            @Valid
            @RequestBody
            AddAmenitiesRequest request
    ) {
        hotelService.addAmenities(id, request);
        return ResponseEntity.noContent().build();
    }
}
