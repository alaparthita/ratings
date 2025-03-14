package com.aetna.ratings.repository;

import com.aetna.ratings.dto.RatingSummary;
import com.aetna.ratings.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingsRepository extends JpaRepository<RatingEntity, Integer> {

    @Query("SELECT new com.aetna.ratings.dto.RatingSummary(r.movieId, ROUND(AVG(r.rating), 1)) FROM RatingEntity r WHERE r.movieId = :movieId GROUP BY r.movieId")
    Optional<RatingSummary> getAvgRatingForMovie(@Param("movieId") int movieId);
    
    @Query("SELECT new com.aetna.ratings.dto.RatingSummary(r.movieId AS movieId, ROUND(AVG(r.rating), 1)) AS rating FROM RatingEntity r WHERE r.movieId IN :movieIds GROUP BY r.movieId")
    List<RatingSummary> getAvgRatingsForMoviesList(@Param("movieIds") List<Integer> movieIds);
    

}
