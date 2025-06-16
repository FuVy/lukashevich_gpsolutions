package com.gpsolutions.lukashevich.services;

import com.gpsolutions.lukashevich.entities.Amenity;
import com.gpsolutions.lukashevich.repositories.AmenityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmenityServiceTest {
    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private AmenityService amenityService;

    @Test
    void findOrCreateAmenitiesByName_shouldReturnEmptySet_whenNamesIsNull() {
        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(null);
        assertThat(result).isNotNull().isEmpty();
        verifyNoInteractions(amenityRepository);
    }

    @Test
    void findOrCreateAmenitiesByName_shouldReturnEmptySet_whenNamesIsEmpty() {
        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(Collections.emptyList());
        assertThat(result).isNotNull().isEmpty();
        verify(amenityRepository).findAllByNameIn(Collections.emptyList());
    }

    @Test
    void findOrCreateAmenitiesByName_shouldReturnExistingAmenities_whenAllExist() {
        Collection<String> names = List.of("Free WiFi", "Free parking");
        Amenity amenity1 = new Amenity("Free WiFi");
        amenity1.setId(1L);
        Amenity amenity2 = new Amenity("Free parking");
        amenity2.setId(2L);
        Set<Amenity> existingAmenities = new HashSet<>(List.of(amenity1, amenity2));

        when(amenityRepository.findAllByNameIn(names)).thenReturn(existingAmenities);

        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(names);

        assertThat(result).containsExactlyInAnyOrder(amenity1, amenity2);
        verify(amenityRepository).findAllByNameIn(names);
        verify(amenityRepository, never()).saveAll(anyCollection());
    }

    @Test
    void findOrCreateAmenitiesByName_shouldCreateNewAmenities_whenNoneExist() {
        Collection<String> names = List.of("New Amenity 1", "New Amenity 2");
        Amenity newAmenity1 = new Amenity("New Amenity 1");
        newAmenity1.setId(1L);
        Amenity newAmenity2 = new Amenity("New Amenity 2");
        newAmenity2.setId(2L);

        when(amenityRepository.findAllByNameIn(names)).thenReturn(new HashSet<>());
        when(amenityRepository.saveAll(anyList())).thenReturn(List.of(newAmenity1, newAmenity2));

        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(names);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Amenity::getName).containsExactlyInAnyOrder("New Amenity 1", "New Amenity 2");
        assertThat(result).extracting(Amenity::getId).containsExactlyInAnyOrder(1L, 2L);

        verify(amenityRepository).findAllByNameIn(names);
        verify(amenityRepository).saveAll(anyList());
    }

    @Test
    void findOrCreateAmenitiesByName_shouldMixExistingAndNewAmenities() {
        Collection<String> names = List.of("Existing Amenity", "New Amenity");
        Amenity existingAmenity = new Amenity("Existing Amenity");
        existingAmenity.setId(1L);
        Set<Amenity> foundAmenities = new HashSet<>(List.of(existingAmenity));

        when(amenityRepository.findAllByNameIn(names)).thenReturn(foundAmenities);
        when(amenityRepository.saveAll(anyList())).thenAnswer(x -> {
            List<Amenity> amenitiesToSave = x.getArgument(0);
            amenitiesToSave.get(0).setId(2L);
            return amenitiesToSave;
        });

        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(names);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Amenity::getName).containsExactlyInAnyOrder("Existing Amenity", "New Amenity");
        assertThat(result).extracting(Amenity::getId).containsExactlyInAnyOrder(1L, 2L);

        verify(amenityRepository).findAllByNameIn(names);
        Amenity expectedAmenity = new Amenity("New Amenity");
        expectedAmenity.setId(2L);
        verify(amenityRepository).saveAll(List.of(expectedAmenity));
    }

    @Test
    void findOrCreateAmenitiesByName_shouldHandleDuplicateNamesInInput() {
        Set<String> names = new HashSet<>(List.of("Amenity 1", "Amenity 2", "Amenity 3"));
        Amenity amenity1 = new Amenity("Amenity 1");
        amenity1.setId(1L);
        Amenity amenity2 = new Amenity("Amenity 2");
        amenity2.setId(2L);
        Amenity amenity3 = new Amenity("Amenity 3");
        amenity3.setId(3L);

        Set<Amenity> existingAmenities = new HashSet<>(List.of(amenity1, amenity2));

        when(amenityRepository.findAllByNameIn(names)).thenReturn(existingAmenities);
        when(amenityRepository.saveAll(anyCollection())).thenReturn(List.of(amenity3));
        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(names);

        assertThat(result).containsExactlyInAnyOrder(amenity1, amenity2, amenity3);
        assertThat(result).hasSize(3);

        verify(amenityRepository).findAllByNameIn(new HashSet<>(names));
        verify(amenityRepository).saveAll(anyCollection());
    }

    @Test
    void findOrCreateAmenitiesByName_shouldHandleNamesWithWhitespace() {
        Collection<String> names = List.of("  Amenity Trimmed ", "Another Amenity  ");
        Amenity amenity1 = new Amenity("Amenity Trimmed");
        amenity1.setId(1L);
        Amenity amenity2 = new Amenity("Another Amenity");
        amenity2.setId(2L);

        when(amenityRepository.findAllByNameIn(anyCollection())).thenReturn(new HashSet<>());
        when(amenityRepository.saveAll(anyList())).thenAnswer(x -> {
            List<Amenity> amenitiesToSave = x.getArgument(0);
            amenitiesToSave.get(0).setId(3L);
            amenitiesToSave.get(1).setId(4L);
            return amenitiesToSave;
        });

        Set<Amenity> result = amenityService.findOrCreateAmenitiesByName(names);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Amenity::getName).containsExactlyInAnyOrder("Amenity Trimmed", "Another Amenity");
        verify(amenityRepository).saveAll(anyList());
    }
}