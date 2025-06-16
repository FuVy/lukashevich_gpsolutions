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
        List<Pair<String, Long>> brandPairs = List.of(
                Pair.of("Hilton", 5L),
                Pair.of("Sigma", 3L)
        );
        when(hotelRepository.countHotelsByBrand()).thenReturn(brandPairs);

        Map<String, Long> result = histogramService.getHistogramByParameter("brand");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Hilton", 5L);
        assertThat(result).containsEntry("Sigma", 3L);
        verify(hotelRepository).countHotelsByBrand();
        verify(hotelRepository, never()).countHotelsByCity();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }

    @Test
    void getHistogramByParameter_city_shouldReturnCityHistogram() {
        List<Pair<String, Long>> cityPairs = List.of(
                Pair.of("Minsk", 10L),
                Pair.of("Warsaw", 7L)
        );
        when(hotelRepository.countHotelsByCity()).thenReturn(cityPairs);

        Map<String, Long> result = histogramService.getHistogramByParameter("city");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Minsk", 10L);
        assertThat(result).containsEntry("Warsaw", 7L);
        verify(hotelRepository).countHotelsByCity();
        verify(hotelRepository, never()).countHotelsByBrand();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }

    @Test
    void getHistogramByParameter_country_shouldReturnCountryHistogram() {
        List<Pair<String, Long>> countryPairs = List.of(
                Pair.of("Belarus", 15L),
                Pair.of("Poland", 12L)
        );
        when(hotelRepository.countHotelsByCountry()).thenReturn(countryPairs);

        Map<String, Long> result = histogramService.getHistogramByParameter("country");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Belarus", 15L);
        assertThat(result).containsEntry("Poland", 12L);
        verify(hotelRepository).countHotelsByCountry();
        verify(hotelRepository, never()).countHotelsByCity();
        verify(hotelAmenityItemRepository, never()).countHotelsByAmenity();
    }

    @Test
    void getHistogramByParameter_amenities_shouldReturnAmenitiesHistogram() {
        List<Pair<String, Long>> amenityPairs = List.of(
                Pair.of("Free WiFi", 20L),
                Pair.of("Pool", 8L)
        );
        when(hotelAmenityItemRepository.countHotelsByAmenity()).thenReturn(amenityPairs);

        Map<String, Long> result = histogramService.getHistogramByParameter("amenities");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Free WiFi", 20L);
        assertThat(result).containsEntry("Pool", 8L);
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