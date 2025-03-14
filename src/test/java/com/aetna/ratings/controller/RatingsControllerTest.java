package com.aetna.ratings.controller;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.service.RatingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RatingsController.class)
class RatingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RatingsService ratingsService;

    private List<Integer> movieIds;
    private RatingSummary ratingSummary;

    @BeforeEach
    void setUp() {
        movieIds = Arrays.asList(1, 2, 3);
        ratingSummary = mock(RatingSummary.class);
        when(ratingSummary.getMovieId()).thenReturn(1);
        when(ratingSummary.getRating()).thenReturn(4.5);
    }

    //@Test
    void testGetMovieRatings() throws Exception {
        when(ratingsService.getAllMoviesRating(movieIds)).thenReturn(Arrays.asList(ratingSummary));

        mockMvc.perform(post("/api/v1/ratings/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1))
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }


    
}