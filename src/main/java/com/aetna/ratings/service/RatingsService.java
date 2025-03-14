package com.aetna.ratings.service;

import com.aetna.ratings.dto.RatingSummary;

import java.util.List;
import java.util.Optional;

public interface RatingsService {
    List<RatingSummary> getAllMoviesRating(List<Integer> movieIds);
    Optional<RatingSummary> geMovieRating(Integer movieId);
}
