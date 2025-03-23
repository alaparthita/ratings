package com.aetna.ratings.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.exception.ErrorDetails;
import com.aetna.ratings.exception.RatingsServiceException;
import com.aetna.ratings.exception.ResourceNotFoundException;
import com.aetna.ratings.service.RatingsService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "Ratings", description = "API for managing and retrieving movie ratings, providing both batch and individual rating lookups")
public class RatingsController {

    public RatingsService ratingsService;

    @Autowired
    public RatingsController(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @RequestMapping(value = "/movies", method = RequestMethod.POST)
    public ResponseEntity<?> getMovieRatings(
            @RequestBody
            @Parameter(
                description = "List of movie IDs to fetch ratings for",
                required = true
            ) List<Integer> movieIds) {
        if (movieIds == null || movieIds.isEmpty()) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), "Movie IDs list cannot be null or empty", ""), HttpStatus.BAD_REQUEST);
        }
        try {
            List<RatingSummary> ratings = ratingsService.getAllMoviesRating(movieIds);
            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), e.getMessage(), ""), HttpStatus.BAD_REQUEST);
        } catch (RatingsServiceException e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Error retrieving movies"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Error retrieving movies"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/movie/{movieId}", "/movie"})
    public ResponseEntity<?> getMovieRating(
            @PathVariable(value = "movieId", required = false)
            @Parameter(
                description = "ID of the movie to fetch rating for",
                required = true
            ) String movieIdStr) {
        if (movieIdStr == null || movieIdStr.trim().isEmpty()) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), "Movie ID is required", ""), HttpStatus.BAD_REQUEST);
        }
        
        try {
            int movieId = Integer.parseInt(movieIdStr);
            if (movieId <= 0) {
                return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), "Movie ID cannot be negative", ""), HttpStatus.BAD_REQUEST);
            }
            
            Optional<RatingSummary> ratingSummary = ratingsService.geMovieRating(movieId);
            if (ratingSummary.isPresent()) {
                return new ResponseEntity<>(ratingSummary.get(), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("Movie rating not found for ID: " + movieId);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), "Failed to convert value '" + movieIdStr + "' to required type 'Integer'", ""), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), e.getMessage(), ""), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_FOUND.value(), e.getMessage(), "Movie ID: " + movieIdStr), HttpStatus.NOT_FOUND);
        } catch (RatingsServiceException e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Movie ID: " + movieIdStr), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Movie ID: " + movieIdStr), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String message = "Invalid request body format";
        if (e.getMessage() != null && e.getMessage().contains("Cannot deserialize value of type `java.lang.Integer`")) {
            message = "Invalid movie ID format. All IDs must be integers.";
        }
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST.value(), message, ""), HttpStatus.BAD_REQUEST);
    }
}