package com.example.demo.domain.model.projections.UserStatistics;

public interface ShortUserRatings extends UserRatingsProjection {
    Integer getUserId();
    String getNickname();
    String getRank();
    Integer getTotalPoints();
    Double getWinRate();
}
