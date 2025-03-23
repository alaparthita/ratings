package com.aetna.ratings.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.exception.RatingsServiceException;
import com.aetna.ratings.exception.ResourceNotFoundException;
import com.aetna.ratings.repository.RatingsRepository;

@ExtendWith(MockitoExtension.class)
class RatingsServiceImplTest {

    @Mock
    private RatingsRepository ratingsRepository;

    @InjectMocks
    private RatingsServiceImpl ratingsService;

    @Test
    void testGetAllMoviesRating() {
        // Arrange
        List<Integer> movieIds = Arrays.asList(1, 2, 3);
        RatingSummary ratingSummary = mock(RatingSummary.class);
        when(ratingSummary.getMovieId()).thenReturn(1);
        when(ratingSummary.getRating()).thenReturn(4.5);
        when(ratingsRepository.getAvgRatingsForMoviesList(movieIds)).thenReturn(Arrays.asList(ratingSummary));

        // Act
        List<RatingSummary> result = ratingsService.getAllMoviesRating(movieIds);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMovieId());
        assertEquals(4.5, result.get(0).getRating());
    }

    @Test
    void testGetAllMoviesRatingThrowsException() {
        // Arrange
        List<Integer> movieIds = Arrays.asList(1, 2, 3);
        when(ratingsRepository.getAvgRatingsForMoviesList(movieIds)).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        assertThrows(RatingsServiceException.class, () -> ratingsService.getAllMoviesRating(movieIds));
    }

    @Test
    void testGetMovieRating() {
        // Arrange
        RatingSummary ratingSummary = mock(RatingSummary.class);
        when(ratingSummary.getMovieId()).thenReturn(1);
        when(ratingSummary.getRating()).thenReturn(4.5);
        when(ratingsRepository.getAvgRatingForMovie(1)).thenReturn(Optional.of(ratingSummary));

        // Act
        Optional<RatingSummary> result = ratingsService.geMovieRating(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getMovieId());
        assertEquals(4.5, result.get().getRating());
    }

    @Test
    void testGetMovieRatingThrowsException() {
        // Arrange
        when(ratingsRepository.getAvgRatingForMovie(1)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RatingsServiceException.class, () -> ratingsService.geMovieRating(1));
    }

    @Test
    void testGetAllMoviesRating_EmptyList() {
        assertThrows(IllegalArgumentException.class, () -> ratingsService.getAllMoviesRating(Collections.emptyList()));
    }

    @Test
    void testGetAllMoviesRating_NullList() {
        assertThrows(IllegalArgumentException.class, () -> ratingsService.getAllMoviesRating(null));
    }

    @Test
    void testGetAllMoviesRating_DuplicateMovieIds() {
        // Arrange
        List<Integer> duplicateMovieIds = Arrays.asList(1, 1, 2, 2);
        RatingSummary ratingSummary = mock(RatingSummary.class);
        when(ratingSummary.getMovieId()).thenReturn(1);
        when(ratingSummary.getRating()).thenReturn(4.5);
        when(ratingsRepository.getAvgRatingsForMoviesList(duplicateMovieIds)).thenReturn(Arrays.asList(ratingSummary));

        // Act
        List<RatingSummary> result = ratingsService.getAllMoviesRating(duplicateMovieIds);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMovieId());
        assertEquals(4.5, result.get(0).getRating());
    }

    @Test
    void testGetAllMoviesRating_NonExistentMovies() {
        // Arrange
        List<Integer> nonExistentMovieIds = Arrays.asList(999, 1000);
        when(ratingsRepository.getAvgRatingsForMoviesList(nonExistentMovieIds)).thenReturn(Collections.emptyList());

        // Act
        List<RatingSummary> result = ratingsService.getAllMoviesRating(nonExistentMovieIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMovieRating_NullMovieId() {
        assertThrows(IllegalArgumentException.class, () -> ratingsService.geMovieRating(null));
    }

    @Test
    void testGetMovieRating_NonExistentMovie() {
        // Arrange
        when(ratingsRepository.getAvgRatingForMovie(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> ratingsService.geMovieRating(999));
    }

    @Test
    void testGetMovieRating_NegativeMovieId() {
        assertThrows(IllegalArgumentException.class, () -> ratingsService.geMovieRating(-1));
    }
}