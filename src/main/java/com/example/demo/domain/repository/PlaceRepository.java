package com.example.demo.domain.repository;

import com.example.demo.domain.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place,Integer> {

    @Query("SELECT CASE WHEN p.GamesInThisPlace IS EMPTY THEN false ELSE true END FROM Place p WHERE p.id = :placeId")
    Boolean hasAnyGames(@Param("placeId") int placeId);
}
