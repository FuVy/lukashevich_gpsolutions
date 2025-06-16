package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.repositories.HotelAmenityItemRepository;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistogramServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelAmenityItemRepository hotelAmenityItemRepository;

    @InjectMocks
    private HistogramService histogramService;

    @Test
    void getHistogramByParameter_brand_shouldReturnBrandHistogram() {
        List<Pair<String, Integer>> brandPairs = List.of(
                Pair.of("Hilton", 5),
                Pair.of("Sigma", 3)
        );
        when(hotelRepository.countHotelsByBrand()).thenReturn(brandPairs);

        Map<String, Integer> result = histogramService.getHistogramByParameter("brand");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Hilton", 5);
        assertThat(result).containsEntry("Sigma", 3);
        verify(hotelRepository).countHotelsByBrand();
        verify(hotelRepository, never()).countHotelsByCity();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }

    @Test
    void getHistogramByParameter_city_shouldReturnCityHistogram() {
        List<Pair<String, Integer>> cityPairs = List.of(
                Pair.of("Minsk", 10),
                Pair.of("Warsaw", 7)
        );
        when(hotelRepository.countHotelsByCity()).thenReturn(cityPairs);

        Map<String, Integer> result = histogramService.getHistogramByParameter("city");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Minsk", 10);
        assertThat(result).containsEntry("Warsaw", 7);
        verify(hotelRepository).countHotelsByCity();
        verify(hotelRepository, never()).countHotelsByBrand();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }

    @Test
    void getHistogramByParameter_country_shouldReturnCountryHistogram() {
        List<Pair<String, Integer>> countryPairs = List.of(
                Pair.of("Belarus", 15),
                Pair.of("Poland", 12)
        );
        when(hotelRepository.countHotelsByCountry()).thenReturn(countryPairs);

        Map<String, Integer> result = histogramService.getHistogramByParameter("country");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Belarus", 15);
        assertThat(result).containsEntry("Poland", 12);
        verify(hotelRepository).countHotelsByCountry();
        verify(hotelRepository, never()).countHotelsByCity();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }

    @Test
    void getHistogramByParameter_amenities_shouldReturnAmenitiesHistogram() {
        List<Pair<String, Integer>> amenityPairs = List.of(
                Pair.of("Free WiFi", 20),
                Pair.of("Pool", 8)
        );
        when(hotelAmenityItemRepository.countHotelsByAmenity()).thenReturn(amenityPairs);

        Map<String, Integer> result = histogramService.getHistogramByParameter("amenities");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Free WiFi", 20);
        assertThat(result).containsEntry("Pool", 8);
        verify(hotelAmenityItemRepository).countHotelsByAmenity();
        verify(hotelRepository, never()).countHotelsByBrand();
    }

    @Test
    void getHistogramByParameter_invalidParam_shouldThrowIllegalArgumentException() {
        String invalidParam = "unknown";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            histogramService.getHistogramByParameter(invalidParam);
        });

        assertThat(thrown.getMessage()).isEqualTo("Invalid or unknown parameter given.");
        verify(hotelRepository, never()).countHotelsByBrand();
        verify(hotelRepository, never()).countHotelsByCity();
        verify(hotelRepository, never()).countHotelsByCountry();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }
}