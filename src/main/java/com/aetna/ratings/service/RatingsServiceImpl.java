package com.aetna.ratings.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.exception.RatingsServiceException;
import com.aetna.ratings.exception.ResourceNotFoundException;
import com.aetna.ratings.repository.RatingsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RatingsServiceImpl implements RatingsService {

    public RatingsRepository ratingsRepository;

    @Autowired
    public RatingsServiceImpl(RatingsRepository ratingsRepository) {
        this.ratingsRepository = ratingsRepository;
    }

    @Override
    public List<RatingSummary> getAllMoviesRating(List<Integer> movieIds) {
        if (movieIds == null || movieIds.isEmpty()) {
            throw new IllegalArgumentException("Movie IDs list cannot be null or empty");
        }
        
        log.info("Retrieving avg movie ratings for " + movieIds.size() + " movies");
        try {
            return ratingsRepository.getAvgRatingsForMoviesList(movieIds);
        } catch (RuntimeException e) {
            throw new RatingsServiceException("An error occurred while retrieving movie ratings for the provided list of movie IDs.", e);
        }
    }

    @Override
    public Optional<RatingSummary> geMovieRating(@PathVariable("movieId") Integer movieId) {
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID cannot be null");
        }
        
        if (movieId < 0) {
            throw new IllegalArgumentException("Movie ID cannot be negative");
        }

        try {
            Optional<RatingSummary> ratingSummary = ratingsRepository.getAvgRatingForMovie(movieId);
            log.info("Retrieving avg movie rating for movie ID: " + ratingSummary);
            if (!ratingSummary.isPresent()) {
                throw new ResourceNotFoundException("Movie rating not found for ID: " + movieId);
            }
            return ratingSummary;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RatingsServiceException("An error occurred while retrieving the movie rating for ID: " + movieId, e);
        }
    }
}
