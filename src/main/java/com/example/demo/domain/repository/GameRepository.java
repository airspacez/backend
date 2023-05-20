package com.example.demo.domain.repository;

import com.example.demo.domain.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Transactional
public interface GameRepository extends JpaRepository<Game,Integer> {

        @Query("SELECT g FROM Game g WHERE " +
                "(:gameYear IS NULL OR YEAR(g.Date) = :gameYear) AND"  +
                "(:gameMonth IS NULL OR MONTH(g.Date) = :gameMonth) AND"  +
                "(:gameDay IS NULL OR DAY(g.Date) = :gameDay) AND"  +
                "(:placeId IS NULL OR g.Place.Id = :placeId) AND " +
                "(:typeId IS NULL OR g.Type.Id = :typeId) AND " +
                "(:gameResultId IS NULL OR g.Result.Id = :gameResultId)"

        )

        Page<Game> findGamesByCriteria(@Param("gameDay") Integer gameDay, @Param("gameMonth") Integer gameMonth, @Param("gameYear") Integer gameYear, @Param("typeId") Integer typeId,  @Param("placeId") Integer placeId, @Param("gameResultId") Integer gameResultId, Pageable pageable);

    }

