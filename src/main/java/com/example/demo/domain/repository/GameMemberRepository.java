package com.example.demo.domain.repository;

import com.example.demo.domain.model.CompositeKeys.GameMemberPK;
import com.example.demo.domain.model.GameMember;
import com.example.demo.domain.model.UserAdditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GameMemberRepository extends JpaRepository<GameMember, Integer> {

    @Transactional
    public Optional<GameMember> findById(GameMemberPK gameMemberPK);

    @Transactional
    public List<GameMember> findAllById_GameId(Integer gameId);

    @Transactional
    public List<GameMember> findAllById_UserId(Integer userId);

    @Transactional
    @Query("SELECT gm FROM GameMember gm WHERE gm.User = :user ORDER BY gm.Game.Date DESC")
    public Page<GameMember> findLastNGamesByUser(@Param("user") UserAdditional user, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT gm.Game.Date) FROM GameMember gm WHERE gm.User.Id = :id AND gm.Game.Date >= :startDate")
    int countGamesPlayedSinceDate(@Param("id") int id, @Param("startDate") Date startDate);
}