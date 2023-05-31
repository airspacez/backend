package com.example.demo.domain.model.projections.ClubProjections;

import com.example.demo.domain.model.City;

public interface ClubWithCityProjection extends ClubProjection {
    Integer getId();
    String getClubName();
    City getCity();
}
