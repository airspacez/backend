package com.example.demo.domain.model.projections.EventProjections;

import com.example.demo.domain.model.Place;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserProjection;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UsernameUserProjection;

import java.sql.Timestamp;
import java.util.List;

public interface EventWithUserUsernameList extends EventProjection {
    Integer getId();
    String getName();
    String getDescription();
    Timestamp getDatetime();
    Place getPlace();
    List<UserProjection> getUsersInEvent();
}
