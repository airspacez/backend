package com.example.demo.domain.repository;

import com.example.demo.domain.model.Club;
import com.example.demo.domain.model.projections.ClubProjections.ClubWithCityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Integer> {

    @Query("SELECT c FROM Club c JOIN FETCH c.City")
    List<ClubWithCityProjection> findAllWithCity();

    @Query("SELECT c FROM Club c JOIN FETCH c.City WHERE c.id = :id")
    Optional<ClubWithCityProjection> findByIdWithCity(@Param("id") int id);

    @Query("SELECT CASE WHEN p.usersOfCLub IS EMPTY THEN false ELSE true END FROM Club p WHERE p.id = :clubId")
    Boolean hasAnyUsers(@Param("clubId") int clubId);

}
