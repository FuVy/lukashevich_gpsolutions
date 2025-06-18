package com.gpsolutions.lukashevich.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpsolutions.lukashevich.domain.search.FailedSearchItem;
import com.gpsolutions.lukashevich.dto.*;
import com.gpsolutions.lukashevich.exception.NotFoundException;
import com.gpsolutions.lukashevich.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(HotelController.class)
class HotelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void returnAllHotels_shouldReturnListOfHotels() throws Exception {
        HotelShortenedDto hotel1 = new HotelShortenedDto(
                1L,
                "DoubleTree by Hilton Minsk",
                "Description for Hotel 1",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        );
        HotelShortenedDto hotel2 = new HotelShortenedDto(
                2L,
                "Hotel Minsk",
                "Description for Hotel 2",
                "11 Nezavisimosti Ave, Minsk, Belarus",
                "+375 17 200-80-00"
        );
        List<HotelShortenedDto> expectedHotels = List.of(hotel1, hotel2);

        when(hotelService.findAllHotels()).thenReturn(expectedHotels);

        mockMvc.perform(get("/property-view/hotels")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$[0].description").value("Description for Hotel 1"))
                .andExpect(jsonPath("$[0].address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                .andExpect(jsonPath("$[0].phone").value("+375 17 309-80-00"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Hotel Minsk"));
    }

    @Test
    void returnHotelFullInfo_validId_shouldReturnHotelFullData() throws Exception {
        AddressDto address = new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        ContactsDto contacts = new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTimeDto arrivalTime = new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0));
        List<String> amenities = List.of("Free parking", "Free WiFi", "Non-smoking rooms");

        HotelFullDto expectedHotel = new HotelFullDto(
                1L,
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                "Hilton",
                address,
                contacts,
                arrivalTime,
                amenities
        );

        when(hotelService.getHotelFullData(1L)).thenReturn(expectedHotel);

        mockMvc.perform(get("/property-view/hotels/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.description").value("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms..."))
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.houseNumber").value(9))
                .andExpect(jsonPath("$.address.street").value("Pobediteley Avenue"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.address.country").value("Belarus"))
                .andExpect(jsonPath("$.address.postCode").value("220004"))
                .andExpect(jsonPath("$.contacts.phone").value("+375 17 309-80-00"))
                .andExpect(jsonPath("$.contacts.email").value("doubletreeminsk.info@hilton.com"))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.arrivalTime.checkOut").value("12:00"))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities.length()").value(3))
                .andExpect(jsonPath("$.amenities[0]").value("Free parking"))
                .andExpect(jsonPath("$.amenities[1]").value("Free WiFi"))
                .andExpect(jsonPath("$.amenities[2]").value("Non-smoking rooms"));
    }

    @Test
    void returnHotelFullInfo_invalidId_shouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException(List.of(new FailedSearchItem("id", "999", "Unknown hotel id."))))
                .when(hotelService).getHotelFullData(999L);

        mockMvc.perform(get("/property-view/hotels/{id}", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addHotel_validHotelData_shouldReturnShortenedHotelDto() throws Exception {
        AddressDto address = new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        ContactsDto contacts = new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTimeDto arrivalTime = new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0));
        List<String> amenities = List.of("Free parking");

        HotelFullDto requestHotel = new HotelFullDto(
                null,
                "New Hotel",
                "New Description",
                "New Brand",
                address,
                contacts,
                arrivalTime,
                amenities
        );

        HotelShortenedDto responseHotel = new HotelShortenedDto(
                3L,
                "New Hotel",
                "New Description",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        );

        when(hotelService.addHotel(any(HotelFullDto.class))).thenReturn(responseHotel);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestHotel))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("New Hotel"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"));
    }

    @Test
    void addHotel_invalidHotelData_shouldReturnBadRequest() throws Exception {
        HotelFullDto invalidHotel = new HotelFullDto(
                null,
                "",
                "New Description",
                "New Brand",
                new AddressDto(9, "Street", "City", "Country", "PostCode"),
                new ContactsDto("123", "email@example.com"),
                new ArrivalTimeDto(LocalTime.of(10, 0), LocalTime.of(11, 0)),
                List.of()
        );

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidHotel))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        invalidHotel = new HotelFullDto(
                null,
                "Valid Name",
                "New Description",
                "New Brand",
                null,
                new ContactsDto("123", "email@example.com"),
                new ArrivalTimeDto(LocalTime.of(10, 0), LocalTime.of(11, 0)),
                List.of()
        );

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidHotel))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAmenities_validRequest_shouldReturnNoContent() throws Exception {
        Long hotelId = 1L;
        AddAmenitiesRequest request = new AddAmenitiesRequest(List.of("Pool", "Gym"));

        doNothing().when(hotelService).addAmenities(hotelId, request);

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void addAmenities_hotelNotFound_shouldReturnNotFound() throws Exception {
        Long hotelId = 999L;
        AddAmenitiesRequest request = new AddAmenitiesRequest(List.of("Pool", "Gym"));

        doThrow(new NotFoundException(List.of(new FailedSearchItem("id", "999", "Unknown hotel id."))))
                .when(hotelService).addAmenities(hotelId, request);

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}