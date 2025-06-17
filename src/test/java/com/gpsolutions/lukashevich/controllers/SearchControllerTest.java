package com.gpsolutions.lukashevich.controllers;

import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.dtos.SearchDto;
import com.gpsolutions.lukashevich.services.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchService searchService;

    @Test
    void search_noParams_shouldReturnAllHotels() throws Exception {
        HotelShortenedDto hotel1 = new HotelShortenedDto(1L, "Hotel 1", "Desc A", "Address A", "8 800 555 35 35");
        HotelShortenedDto hotel2 = new HotelShortenedDto(2L, "Hotel 2", "Desc B", "Address B", "8 123 123 45 67");
        List<HotelShortenedDto> expectedHotels = List.of(hotel1, hotel2);

        when(searchService.searchHotels(any(SearchDto.class)))
                .thenReturn(expectedHotels);

        mockMvc.perform(get("/property-view/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hotel 1"))
                .andExpect(jsonPath("$[0].description").value("Desc A"))
                .andExpect(jsonPath("$[0].address").value("Address A"))
                .andExpect(jsonPath("$[0].phone").value("8 800 555 35 35"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Hotel 2"))
                .andExpect(jsonPath("$[1].description").value("Desc B"))
                .andExpect(jsonPath("$[1].address").value("Address B"))
                .andExpect(jsonPath("$[1].phone").value("8 123 123 45 67"));
    }

    @Test
    void search_withCityParam_shouldReturnFilteredHotels() throws Exception {
        HotelShortenedDto hotel1 = new HotelShortenedDto(1L, "Hotel 1", "Desc A", "Address A", "8 800 555 35 35");
        List<HotelShortenedDto> expectedHotels = List.of(hotel1);

        SearchDto searchDto = new SearchDto(null, null, "Minsk", null, null);

        when(searchService.searchHotels(eq(searchDto)))
                .thenReturn(expectedHotels);

        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hotel 1"));
    }

    @Test
    void search_withMultipleParams_shouldReturnFilteredHotels() throws Exception {
        HotelShortenedDto hotel1 = new HotelShortenedDto(1L, "Hotel 1", "Desc A", "Address A", "8 800 555 35 35");
        List<HotelShortenedDto> expectedHotels = List.of(hotel1);

        SearchDto searchDto = new SearchDto("Hotel 1", "Hilton", "Minsk", "Belarus", "Free WiFi");
        when(searchService.searchHotels(eq(searchDto)))
                .thenReturn(expectedHotels);

        mockMvc.perform(get("/property-view/search")
                        .param("name", "Hotel 1")
                        .param("brand", "Hilton")
                        .param("city", "Minsk")
                        .param("country", "Belarus")
                        .param("amenities", "Free WiFi")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hotel 1"))
                .andExpect(jsonPath("$[0].description").value("Desc A"));
    }

    @Test
    void search_noMatchingHotels_shouldReturnEmptyList() throws Exception {
        when(searchService.searchHotels(any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/property-view/search")
                        .param("city", "NonExistentCity")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}