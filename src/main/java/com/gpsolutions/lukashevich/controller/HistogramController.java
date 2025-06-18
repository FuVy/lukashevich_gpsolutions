package com.gpsolutions.lukashevich.controller;

import com.gpsolutions.lukashevich.service.HistogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property-view/histogram")
@RequiredArgsConstructor
public class HistogramController {
    private final HistogramService histogramService;

    @Operation(summary = "Generate histogram by parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Brand Histogram", value = "{\"Hilton\": 10, \"Sigma\": 5}"),
                                    @ExampleObject(name = "Amenity Histogram", value = "{\"Spa\": 3, \"Pool\": 7}")
                            })),
            @ApiResponse(responseCode = "400",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"Invalid or unknown parameter given.\"")))
    })
    @GetMapping("/{param}")
    public ResponseEntity<?> generateHistogram(
            @Parameter(description = "The parameter to generate the histogram for (brand/city/country/amenities).",
                    examples = {
                            @ExampleObject(name = "Brand Parameter", value = "brand"),
                            @ExampleObject(name = "City Parameter", value = "city"),
                            @ExampleObject(name = "Country Parameter", value = "country"),
                            @ExampleObject(name = "Amenities Parameter", value = "amenities")
                    })
            @PathVariable("param")
            String param) {
        return ResponseEntity.ok(histogramService.getHistogramByParameter(param));
    }
}
