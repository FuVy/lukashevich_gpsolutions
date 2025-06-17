package com.gpsolutions.lukashevich.controllers;

import com.gpsolutions.lukashevich.dtos.AddAmenitiesRequest;
import com.gpsolutions.lukashevich.dtos.HotelFullDto;
import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/property-view/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @Operation(summary = "Get all hotels")
    @ApiResponses(
            value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortenedDto[].class)))
    })
    @GetMapping
    public ResponseEntity<?> returnAllHotels() {
        return ResponseEntity.ok(hotelService.findAllHotels());
    }

    @Operation(summary = "Get full hotel information by ID")
    @ApiResponses(
            value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelFullDto.class))),
            @ApiResponse(responseCode = "404", description = "Unknown hotel id.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelFullDto> returnHotelFullInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hotelService.getHotelFullData(id));
    }

    @Operation(summary = "Add a new hotel")
    @ApiResponses(
            value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortenedDto.class))),
            @ApiResponse(responseCode = "400")
    })
    @PostMapping
    public ResponseEntity<HotelShortenedDto> addHotel(
            @NotNull(message = "Request can't be null.")
            @Valid
            @RequestBody
            HotelFullDto hotel
    ) {
        return ResponseEntity.ok(hotelService.addHotel(hotel));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add amenities to a hotel",
            description = "Adds a list of amenities to an existing hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404", description = "Unknown hotel id.")
    })
    @PostMapping("/{id}/amenities")
    public void addAmenities(
            @PathVariable("id")
            Long id,
            @NotNull(message = "Request can't be null.")
            @Valid
            @RequestBody
            AddAmenitiesRequest request
    ) {
        hotelService.addAmenities(id, request);
    }
}
