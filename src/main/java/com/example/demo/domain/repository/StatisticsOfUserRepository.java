package com.example.demo.domain.repository;

import com.example.demo.domain.model.StatisticsOfUser;
import com.example.demo.domain.model.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticsOfUserRepository extends JpaRepository<StatisticsOfUser, Integer> {

    @Query("SELECT su FROM UserInEvent uie JOIN uie.user user JOIN user.statistics su WHERE uie.event.id = :eventId AND uie.appeared = true")
    List<StatisticsOfUser> findStatisticsByEventId(@Param("eventId") Integer eventId);
}
