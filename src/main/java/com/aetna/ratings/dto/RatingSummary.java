package com.aetna.ratings.dto;

import lombok.Data;

@Data
public class RatingSummary {
     private int movieId;
     private double rating;
 
     public RatingSummary(int movieId, double rating) {
         this.movieId = movieId;
         this.rating = rating;
     }
}