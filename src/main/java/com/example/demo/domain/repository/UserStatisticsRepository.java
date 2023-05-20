package com.example.demo.domain.repository;

import com.example.demo.domain.model.UserStatistics;
import com.example.demo.domain.model.projections.UserStatistics.ShortUserRatings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {

    @Query("SELECT s FROM UserStatistics s JOIN s.userAdditional u WHERE " +
            "(:searchString IS NULL OR :searchString = '') OR " +
            "(u.Name LIKE %:searchString% OR " +
            "u.Nickname LIKE %:searchString% OR " +
            "u.AltNickname LIKE %:searchString%)")
     Page<ShortUserRatings> findPlayerRatings(@Param("searchString") String searchString,
                                                 Pageable pageable);
}
