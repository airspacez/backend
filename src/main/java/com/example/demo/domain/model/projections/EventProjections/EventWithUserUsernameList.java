package com.example.demo.domain.model.projections.EventProjections;

import com.example.demo.domain.model.Place;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserProjection;

import java.sql.Timestamp;
import java.util.List;

public interface EventWithUserUsernameList extends EventProjection {
    Integer getId();
    String getName();
    String getDescription();
    Timestamp getDatetime();
    Place getPlace();

    Boolean getDistributed();

    String getState();
    List<UserProjection> getUsersInEvent();
}
