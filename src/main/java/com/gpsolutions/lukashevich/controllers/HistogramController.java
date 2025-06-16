package com.gpsolutions.lukashevich.controllers;

import com.gpsolutions.lukashevich.services.HistogramService;
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

    @GetMapping("/{param}")
    public ResponseEntity<?> generateHistogram(@PathVariable("param") String param) {
        return ResponseEntity.ok(histogramService.getHistogramByParameter(param));
    }
}
