package com.aetna.ratings.controller;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.exception.RatingsServiceException;
import com.aetna.ratings.service.RatingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingsController.class)
class RatingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingsService ratingsService;

    private List<Integer> movieIds;
    private RatingSummary ratingSummary;

    @BeforeEach
    void setUp() {
        movieIds = Arrays.asList(1, 2, 3);
        ratingSummary = new RatingSummary(1, 4.5);
    }

    @Test
    void testGetMovieRatings() throws Exception {
        when(ratingsService.getAllMoviesRating(movieIds)).thenReturn(Arrays.asList(ratingSummary));

        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1))
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }

    @Test
    void testGetMovieRatingsWithEmptyList() throws Exception {
        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Movie IDs list cannot be null or empty"));
    }

    @Test
    void testGetMovieRatingsWithServerError() throws Exception {
        when(ratingsService.getAllMoviesRating(movieIds)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Database error"));
    }

    @Test
    void testGetMovieRating() throws Exception {
        when(ratingsService.geMovieRating(1)).thenReturn(java.util.Optional.of(ratingSummary));

        mockMvc.perform(get("/api/v1/ratings/movie/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    void testGetMovieRatingNotFound() throws Exception {
        when(ratingsService.geMovieRating(1)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/ratings/movie/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie rating not found for ID: 1"));
    }

    @Test
    void testGetMovieRatingWithMissingId() throws Exception {
        mockMvc.perform(get("/api/v1/ratings/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Movie ID is required"));
    }

    @Test
    void testGetMovieRatingWithInvalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/v1/ratings/movie/invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value 'invalid' to required type 'Integer'"));
    }

    @Test
    void testGetMovieRatingsWithInvalidJsonFormat() throws Exception {
        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request body format"));
    }

    @Test
    void testGetMovieRatingsWithNonIntegerValues() throws Exception {
        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, \"two\", 3]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid movie ID format. All IDs must be integers."));
    }

    @Test
    void testGetMovieRatingsWithNegativeIds() throws Exception {
        List<Integer> negativeIds = Arrays.asList(1, -2, 3);
        when(ratingsService.getAllMoviesRating(negativeIds))
                .thenThrow(new IllegalArgumentException("Movie ID cannot be negative"));

        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, -2, 3]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Movie ID cannot be negative"));
    }

    @Test
    void testGetMovieRatingsWithNullValues() throws Exception {
        List<Integer> idsWithNull = Arrays.asList(1, 0, 3);
        when(ratingsService.getAllMoviesRating(idsWithNull)).thenReturn(Arrays.asList(ratingSummary));

        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, null, 3]"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMovieRatingsWithRatingsServiceException() throws Exception {
        when(ratingsService.getAllMoviesRating(movieIds))
                .thenThrow(new RatingsServiceException("Service error"));

        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Service error"));
    }

    @Test
    void testGetMovieRatingWithNegativeId() throws Exception {
        when(ratingsService.geMovieRating(-1))
                .thenThrow(new IllegalArgumentException("Movie ID cannot be negative"));

        mockMvc.perform(get("/api/v1/ratings/movie/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Movie ID cannot be negative"));
    }

    @Test
    void testGetMovieRatingWithRatingsServiceException() throws Exception {
        when(ratingsService.geMovieRating(1))
                .thenThrow(new RatingsServiceException("Service error"));

        mockMvc.perform(get("/api/v1/ratings/movie/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Service error"));
    }
}