package com.example.demo.domain.repository;

import com.example.demo.domain.model.Event;
import com.example.demo.domain.model.projections.EventProjections.EventProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Transactional
    @Query("SELECT e FROM Event e WHERE e.id = :eventId")
    <T extends EventProjection> Optional<T> getEventWithUsersById(@Param("eventId") Integer eventId, Class<T> projectionType);


}
