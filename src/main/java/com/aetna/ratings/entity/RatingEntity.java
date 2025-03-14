package com.aetna.ratings.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ratings")
public class RatingEntity {

    @Id
    @Column
    int ratingId;

    @Column
    int userId;

    @Column
    int movieId;

    @Column
    double rating;

    @Column
    int timestamp;
}
