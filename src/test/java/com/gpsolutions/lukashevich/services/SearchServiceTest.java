package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.dtos.HotelShortenedDto;
import com.gpsolutions.lukashevich.entities.*;
import com.gpsolutions.lukashevich.mappers.HotelMapper;
import com.gpsolutions.lukashevich.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private SearchService searchService;

    private Hotel createTestHotel(Long id, String name, String brand, String city, String country, String... amenities) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setBrand(brand);
        hotel.setDescription("Description for " + name);
        hotel.setAddress(new Address(1, "Street", city, country, "12345"));
        hotel.setContacts(new Contacts("123", "email@example.com"));
        hotel.setArrivalTime(new ArrivalTime(LocalTime.of(14, 0), LocalTime.of(12, 0)));

        List<HotelAmenityItem> amenityItems = new ArrayList<>();
        for (String amenityName : amenities) {
            Amenity amenity = new Amenity(amenityName);
            HotelAmenityItem hotelAmenityItem = new HotelAmenityItem();
            hotelAmenityItem.setHotel(hotel);
            hotelAmenityItem.setAmenity(amenity);
            amenityItems.add(hotelAmenityItem);
        }
        hotel.setAmenities(amenityItems);
        return hotel;
    }

    private HotelShortenedDto createShortenedDto(Long id, String name, String address, String phone) {
        return new HotelShortenedDto(id, name, "Description for " + name, address, phone);
    }

    @Test
    void searchHotels_noParameters_shouldReturnAllHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "City 1", "Country 1");
        Hotel hotel2 = createTestHotel(2L, "Hotel 2", "Brand 2", "City 2", "Country 2");
        List<Hotel> hotels = List.of(hotel1, hotel2);

        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "1 Street, City 1, 12345, Country 1", "123");
        HotelShortenedDto dto2 = createShortenedDto(2L, "Hotel 2", "1 Street, City 2, 12345, Country 2", "123");
        List<HotelShortenedDto> expectedDtos = List.of(dto1, dto2);

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);
        when(hotelMapper.toShortenedDto(hotel2)).thenReturn(dto2);

        List<HotelShortenedDto> result = searchService.searchHotels(null, null, null, null, null);

        assertThat(result).isEqualTo(expectedDtos);
        verify(hotelRepository).findAll(any(Specification.class));
        verify(hotelMapper).toShortenedDto(hotel1);
        verify(hotelMapper).toShortenedDto(hotel2);
    }

    @Test
    void searchHotels_byName_shouldReturnFilteredHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "City 1", "Country 1");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "1 Street, City 1, 12345, Country 1", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels("1", null, null, null, null);

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
        verify(hotelMapper).toShortenedDto(hotel1);
    }

    @Test
    void searchHotels_byBrand_shouldReturnFilteredHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Hilton", "City 1", "Country 1");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "1 Street, City 1, 12345, Country 1", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels(null, "hilton", null, null, null);

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_byCity_shouldReturnFilteredHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "Minsk", "Country 1");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "1 Street, Minsk, 12345, Country 1", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels(null, null, "minsk", null, null);

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_byCountry_shouldReturnFilteredHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "City 1", "Belarus");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "1 Street, City 1, 12345, Belarus", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels(null, null, null, "belarus", null);

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_byAmenities_shouldReturnFilteredHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "City 1", "Country 1", "am1", "am2");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "1 Street, City 1, 12345, Country 1", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels(null, null, null, null, "am1,am2");

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_multipleParameters_shouldReturnFilteredHotels() {
        Hotel hotel1 = createTestHotel(1L, "1", "2", "3", "4", "am1", "am2");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "1", "1 Street, 3, 12345, 4", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels("1", "2", "3", "4", "spa");

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_noMatchingHotels_shouldReturnEmptyList() {
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<HotelShortenedDto> result = searchService.searchHotels("NonExistent", null, null, null, null);

        assertThat(result).isEmpty();
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_amenitiesCaseInsensitiveAndTrimmed() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "City 1", "Country 1", " am1 ", "  am2 ");
        List<Hotel> hotels = List.of(hotel1);
        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel G", "1 Street, City G, 12345, Country G", "123");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);

        List<HotelShortenedDto> result = searchService.searchHotels(null, null, null, null, " AM1 , Am2 ");

        assertThat(result).isEqualTo(List.of(dto1));
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void searchHotels_amenitiesPartialMatchRequiresAll() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "City 1", "Country 1", "Gym", "Sauna");
        List<Hotel> hotels = List.of(hotel1);

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(any(Hotel.class))).thenAnswer(invocation -> {
            Hotel hotel = invocation.getArgument(0);
            return createShortenedDto(hotel.getId(), hotel.getName(), hotel.getAddress().getHouseNumber() + " " + hotel.getAddress().getStreet() + ", " + hotel.getAddress().getCity() + ", " + hotel.getAddress().getPostCode() + ", " + hotel.getAddress().getCountry(), hotel.getContacts().getPhone());
        });

        List<HotelShortenedDto> result = searchService.searchHotels(null, null, null, null, "Gym,Sauna");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Hotel 1");
    }
}