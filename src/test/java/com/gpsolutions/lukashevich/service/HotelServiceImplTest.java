package com.gpsolutions.lukashevich.service;

import com.gpsolutions.lukashevich.dto.*;
import com.gpsolutions.lukashevich.entity.*;
import com.gpsolutions.lukashevich.exception.NotFoundException;
import com.gpsolutions.lukashevich.mapper.HotelMapper;
import com.gpsolutions.lukashevich.repository.HotelAmenityItemRepository;
import com.gpsolutions.lukashevich.repository.HotelRepository;
import com.gpsolutions.lukashevich.service.jpa.AmenityServiceImpl;
import com.gpsolutions.lukashevich.service.jpa.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {
    @Mock
    private AmenityServiceImpl amenityService;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelAmenityItemRepository hotelAmenityItemRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel createTestHotel(Long id, String name, String brand, String description) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setBrand(brand);
        hotel.setDescription(description);
        hotel.setAddress(new Address(1, "Street", "City", "Country", "12345"));
        hotel.setContacts(new Contacts("1234567890", "test@example.com"));
        hotel.setArrivalTime(new ArrivalTime(LocalTime.of(14, 0), LocalTime.of(12, 0)));
        hotel.setAmenities(new ArrayList<>());
        return hotel;
    }

    private HotelShortenedDto createShortenedDto(Long id, String name, String description) {
        return new HotelShortenedDto(id, name, description, "1 Street, City, 12345, Country", "1234567890");
    }

    private HotelFullDto createFullDto(Long id, String name, String brand, String description, List<String> amenities) {
        return new HotelFullDto(
                id,
                name,
                description,
                brand,
                new AddressDto(1, "Street", "City", "Country", "12345"),
                new ContactsDto("1234567890", "test@example.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0)),
                amenities
        );
    }

    @Test
    void findAllHotels_shouldReturnListOfShortenedHotels() {
        Hotel hotel1 = createTestHotel(1L, "Hotel 1", "Brand 1", "Desc 1");
        Hotel hotel2 = createTestHotel(2L, "Hotel 2", "Brand 2", "Desc 2");
        List<Hotel> hotels = List.of(hotel1, hotel2);

        HotelShortenedDto dto1 = createShortenedDto(1L, "Hotel 1", "Desc 1");
        HotelShortenedDto dto2 = createShortenedDto(2L, "Hotel 2", "Desc 2");
        List<HotelShortenedDto> expectedDtos = List.of(dto1, dto2);

        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toShortenedDto(hotel1)).thenReturn(dto1);
        when(hotelMapper.toShortenedDto(hotel2)).thenReturn(dto2);

        List<HotelShortenedDto> result = hotelService.findAllHotels();

        assertThat(result).isEqualTo(expectedDtos);
        verify(hotelRepository).findAll();
        verify(hotelMapper, times(2)).toShortenedDto(any(Hotel.class));
    }

    @Test
    void getHotelFullData_validId_shouldReturnFullHotelDto() {
        Long hotelId = 1L;
        Hotel hotel = createTestHotel(hotelId, "Hotel 1", "Brand 1", "Desc 1");
        HotelFullDto expectedDto = createFullDto(hotelId, "Hotel 1", "Brand 1", "Desc 1", Collections.emptyList());

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDto(hotel)).thenReturn(expectedDto);

        HotelFullDto result = hotelService.getHotelFullData(hotelId);

        assertThat(result).isEqualTo(expectedDto);
        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper).toDto(hotel);
    }

    @Test
    void getHotelFullData_invalidId_shouldThrowNotFoundException() {
        Long hotelId = 99L;
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            hotelService.getHotelFullData(hotelId);
        });

        assertThat(thrown.getFailedItems().get(0).getField()).isEqualTo("id");
        assertThat(thrown.getFailedItems().get(0).getValue()).isEqualTo(hotelId.toString());
        assertThat(thrown.getFailedItems().get(0).getMessage()).isEqualTo("Unknown hotel id.");
        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper, never()).toDto(any(Hotel.class));
    }

    @Test
    void addHotel_shouldSaveHotelAndAmenities() {
        HotelFullDto inputDto = createFullDto(null, "New Hotel", "New Brand", "New Desc", List.of("Pool", "Gym"));
        Hotel hotelEntityWithoutAmenities = createTestHotel(null, "New Hotel", "New Brand", "New Desc");
        Hotel savedHotelEntity = createTestHotel(1L, "New Hotel", "New Brand", "New Desc");
        Hotel savedHotelWithAmenities = createTestHotel(1L, "New Hotel", "New Brand", "New Desc");
        savedHotelWithAmenities.setAmenities(new ArrayList<>());
        savedHotelWithAmenities.getAmenities().add(new HotelAmenityItem(savedHotelWithAmenities, new Amenity("Pool")));
        savedHotelWithAmenities.getAmenities().add(new HotelAmenityItem(savedHotelWithAmenities, new Amenity("Gym")));

        HotelShortenedDto expectedShortenedDto = createShortenedDto(1L, "New Hotel", "New Desc");

        Amenity poolAmenity = new Amenity("Pool");
        poolAmenity.setId(1L);
        Amenity gymAmenity = new Amenity("Gym");
        gymAmenity.setId(2L);
        Set<Amenity> createdAmenities = new HashSet<>(List.of(poolAmenity, gymAmenity));

        HotelAmenityItem hotelPool = new HotelAmenityItem(savedHotelEntity, poolAmenity);
        HotelAmenityItem hotelGym = new HotelAmenityItem(savedHotelEntity, gymAmenity);

        when(hotelMapper.toEntityWithoutAmenities(inputDto)).thenReturn(hotelEntityWithoutAmenities);
        when(hotelRepository.save(hotelEntityWithoutAmenities)).thenReturn(savedHotelEntity);
        when(amenityService.findOrCreateAmenitiesByName(List.of("Pool", "Gym"))).thenReturn(createdAmenities);
        when(hotelAmenityItemRepository.saveAll(anyList())).thenReturn(List.of(hotelPool, hotelGym));
        when(hotelRepository.save(savedHotelEntity)).thenReturn(savedHotelWithAmenities);
        when(hotelMapper.toShortenedDto(savedHotelWithAmenities)).thenReturn(expectedShortenedDto);


        HotelShortenedDto result = hotelService.addHotel(inputDto);

        assertThat(result).isEqualTo(expectedShortenedDto);
        verify(hotelMapper).toEntityWithoutAmenities(inputDto);
        verify(hotelRepository).save(hotelEntityWithoutAmenities);
        verify(amenityService).findOrCreateAmenitiesByName(List.of("Pool", "Gym"));
        verify(hotelAmenityItemRepository).saveAll(anyList());
        verify(hotelRepository, times(2)).save(any(Hotel.class));
        verify(hotelMapper).toShortenedDto(savedHotelWithAmenities);
    }

    @Test
    void addHotel_withNullAmenities_shouldSaveHotelWithoutAmenities() {
        HotelFullDto inputDto = createFullDto(null, "New Hotel", "New Brand", "New Desc", null);
        Hotel hotelEntityWithoutAmenities = createTestHotel(null, "New Hotel", "New Brand", "New Desc");
        Hotel savedHotelEntity = createTestHotel(1L, "New Hotel", "New Brand", "New Desc");
        HotelShortenedDto expectedShortenedDto = createShortenedDto(1L, "New Hotel", "New Desc");

        when(hotelMapper.toEntityWithoutAmenities(inputDto)).thenReturn(hotelEntityWithoutAmenities);
        when(hotelRepository.save(hotelEntityWithoutAmenities)).thenReturn(savedHotelEntity);
        when(hotelRepository.save(savedHotelEntity)).thenReturn(savedHotelEntity);
        when(hotelMapper.toShortenedDto(savedHotelEntity)).thenReturn(expectedShortenedDto);

        HotelShortenedDto result = hotelService.addHotel(inputDto);

        assertThat(result).isEqualTo(expectedShortenedDto);
        verify(hotelMapper).toEntityWithoutAmenities(inputDto);
        verify(hotelRepository, times(2)).save(any(Hotel.class));
        verify(amenityService, never()).findOrCreateAmenitiesByName(anySet());
        verify(hotelAmenityItemRepository, never()).saveAll(anyList());
        verify(hotelMapper).toShortenedDto(savedHotelEntity);
    }


    @Test
    void addAmenities_publicMethod_validRequest_shouldAddAmenities() {
        Long hotelId = 1L;
        AddAmenitiesRequest request = new AddAmenitiesRequest(List.of("Spa", "Restaurant"));
        Hotel hotel = createTestHotel(hotelId, "Existing Hotel", "Brand X", "Desc X");
        hotel.setAmenities(new ArrayList<>());

        Amenity spaAmenity = new Amenity("Spa");
        spaAmenity.setId(1L);
        Amenity restaurantAmenity = new Amenity("Restaurant");
        restaurantAmenity.setId(2L);
        Set<Amenity> foundAndCreatedAmenities = new HashSet<>(List.of(spaAmenity, restaurantAmenity));

        HotelAmenityItem hotelSpa = new HotelAmenityItem(hotel, spaAmenity);
        HotelAmenityItem hotelRestaurant = new HotelAmenityItem(hotel, restaurantAmenity);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityService.findOrCreateAmenitiesByName(List.of("Spa", "Restaurant"))).thenReturn(foundAndCreatedAmenities);
        when(hotelAmenityItemRepository.saveAll(anyList())).thenReturn(List.of(hotelSpa, hotelRestaurant));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        hotelService.addAmenities(hotelId, request);

        assertThat(hotel.getAmenities()).hasSize(2);
        assertThat(hotel.getAmenities()).extracting(item -> item.getAmenity().getName()).containsExactlyInAnyOrder("Spa", "Restaurant");
        verify(hotelRepository).findById(hotelId);
        verify(amenityService).findOrCreateAmenitiesByName(List.of("Spa", "Restaurant"));
        verify(hotelAmenityItemRepository).saveAll(anyList());
        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_publicMethod_hotelNotFound_shouldThrowNotFoundException() {
        Long hotelId = 999L;
        AddAmenitiesRequest request = new AddAmenitiesRequest(List.of("Spa"));

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            hotelService.addAmenities(hotelId, request);
        });

        assertThat(thrown.getFailedItems().get(0).getField()).isEqualTo("id");
        assertThat(thrown.getFailedItems().get(0).getValue()).isEqualTo(hotelId.toString());
        assertThat(thrown.getFailedItems().get(0).getMessage()).isEqualTo("Unknown hotel id.");
        verify(hotelRepository).findById(hotelId);
        verify(amenityService, never()).findOrCreateAmenitiesByName(anySet());
        verify(hotelAmenityItemRepository, never()).saveAll(anyList());
        verify(hotelRepository, never()).save(any(Hotel.class));
    }
}