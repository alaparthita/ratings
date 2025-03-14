package com.aetna.ratings.service;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.exception.RatingsServiceException;
import com.aetna.ratings.repository.RatingsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingsServiceImplTest {

    @Mock
    private RatingsRepository ratingsRepository;

    @InjectMocks
    private RatingsServiceImpl ratingsService;

    private List<Integer> movieIds;

    @Mock
    private RatingSummary ratingSummary;

    @BeforeEach
    void setUp() {
        movieIds = Arrays.asList(1, 2, 3);
        ratingSummary = mock(RatingSummary.class);
        when(ratingSummary.getMovieId()).thenReturn(1);
        when(ratingSummary.getRating()).thenReturn(4.5);
    }

    @Test
    void testGetAllMoviesRating() {
        when(ratingsRepository.getAvgRatingsForMoviesList(movieIds)).thenReturn(Arrays.asList(ratingSummary));

        List<RatingSummary> result = ratingsService.getAllMoviesRating(movieIds);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMovieId());
        assertEquals(4.5, result.get(0).getRating());
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void testGetAllMoviesRatingThrowsException() {
        when(ratingsRepository.getAvgRatingsForMoviesList(movieIds)).thenThrow(new RuntimeException("Database error"));
        
        assertThrows(RatingsServiceException.class, () -> ratingsService.getAllMoviesRating(movieIds));
    }

    @Test
    void testGetMovieRating() {
        when(ratingsRepository.getAvgRatingForMovie(1)).thenReturn(Optional.of(ratingSummary));

        Optional<RatingSummary> result = ratingsService.geMovieRating(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getMovieId());
        assertEquals(4.5, result.get().getRating());
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void testGetMovieRatingThrowsException() {
        when(ratingsRepository.getAvgRatingForMovie(1)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RatingsServiceException.class, () -> ratingsService.geMovieRating(1));
    }
}