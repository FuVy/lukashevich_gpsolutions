package com.gpsolutions.lukashevich.controllers;

import com.gpsolutions.lukashevich.services.HistogramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistogramController.class)
class HistogramControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistogramService histogramService;

    @Test
    void generateHistogram_cityParam_shouldReturnCityHistogram() throws Exception {
        Map<String, Long> expectedHistogram = new HashMap<>();
        expectedHistogram.put("Minsk", 1L);
        expectedHistogram.put("Moscow", 2L);

        when(histogramService.getHistogramByParameter("city")).thenReturn(expectedHistogram);

        mockMvc.perform(get("/property-view/histogram/{param}", "city")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Minsk", is(1)))
                .andExpect(jsonPath("$.Moscow", is(2)));
    }

    @Test
    void generateHistogram_countryParam_shouldReturnCountryHistogram() throws Exception {
        Map<String, Long> expectedHistogram = new HashMap<>();
        expectedHistogram.put("Belarus", 1L);
        expectedHistogram.put("Poland", 5L);

        when(histogramService.getHistogramByParameter("country")).thenReturn(expectedHistogram);

        mockMvc.perform(get("/property-view/histogram/{param}", "country")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Belarus", is(1)))
                .andExpect(jsonPath("$.Poland", is(5)));
    }

    @Test
    void generateHistogram_brandParam_shouldReturnBrandHistogram() throws Exception {
        Map<String, Long> expectedHistogram = new HashMap<>();
        expectedHistogram.put("Hilton", 3L);
        expectedHistogram.put("Sigma", 1L);

        when(histogramService.getHistogramByParameter("brand")).thenReturn(expectedHistogram);

        mockMvc.perform(get("/property-view/histogram/{param}", "brand")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Hilton", is(3)))
                .andExpect(jsonPath("$.Sigma", is(1)));
    }

    @Test
    void generateHistogram_amenitiesParam_shouldReturnAmenitiesHistogram() throws Exception {
        Map<String, Long> expectedHistogram = new HashMap<>();
        expectedHistogram.put("Free WiFi", 10L);
        expectedHistogram.put("Free parking", 5L);

        when(histogramService.getHistogramByParameter("amenities")).thenReturn(expectedHistogram);

        mockMvc.perform(get("/property-view/histogram/{param}", "amenities")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['Free WiFi']", is(10)))
                .andExpect(jsonPath("$.['Free parking']", is(5)));
    }

    @Test
    void generateHistogram_unknownParam_shouldReturnEmptyHistogram() throws Exception {
        String unknownParam = "unknown";

        when(histogramService.getHistogramByParameter(unknownParam))
                .thenThrow(new IllegalArgumentException("Invalid or unknown parameter given."));

        mockMvc.perform(get("/property-view/histogram/{param}", unknownParam)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or unknown parameter given."));
    }
}