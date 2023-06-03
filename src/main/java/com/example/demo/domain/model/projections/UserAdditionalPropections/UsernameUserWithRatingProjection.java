package com.example.demo.domain.model.projections.UserAdditionalPropections;

import com.example.demo.domain.model.StatisticsOfUser;
import com.example.demo.domain.model.projections.StatisticsOfUserRatingOnlyProjection;

public interface UsernameUserWithRatingProjection extends UserAdditionalProjection{
    Integer getId();
    String getAltNickname();
    String getName();
    String getNickname();
    Boolean getIsMale();
    StatisticsOfUserRatingOnlyProjection getStatistics();
}

