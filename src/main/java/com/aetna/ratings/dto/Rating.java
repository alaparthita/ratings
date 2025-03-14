package com.aetna.ratings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    int ratingId;
    int userId;
    int movieId;
    double rating;
    long timestamp;
}
