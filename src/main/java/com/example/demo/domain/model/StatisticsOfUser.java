package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="statistics")
public class StatisticsOfUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userId")
    private Integer id;

    @Column(name="rating")
    private Float rating;

    @Column(name="average_points_by_game")
    private Float averagePointsByGame;

    @Column(name="games_count")
    private Integer gamesCount;

    @Column(name="games_as_citizen")
    private Integer gamesAsCitizen;

    @Column(name="games_as_mafia")
    private Integer gamesAsMafia;

    @Column(name="games_as_don")
    private Integer gamesAsDon;

    @Column(name="games_as_sheriff")
    private Integer gamesAsSheriff;

    @Column(name="wins_as_citizen")
    private Integer winsAsCitizen;

    @Column(name="wins_as_mafia")
    private Integer winsAsMafia;

    @Column(name="wins_as_don")
    private Integer winsAsDon;

    @Column(name="wins_as_sheriff")
    private Integer winsAsSheriff;

    @Column(name="extra_points")
    private Float extraPoints;

    @Column(name="compensation_points")
    private Float compensationPoints;

    @Column(name="penalty_points")
    private Float penaltyPoints;

    @Column(name="2_black_count")
    private Integer twoBlackCount;

    @Column(name="3_black_count")
    private Integer threeBlackCount;

    @Column(name="3_red_count")
    private Integer threeRedCount;

    @JsonIgnore
    @OneToOne(mappedBy = "statistics")
    private UserAdditional user;

}

