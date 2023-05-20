package com.example.demo.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_ratings_view")
public class UserStatistics {
    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserId;

    @Column(name="Rank")
    private Integer rank;

    @Column(name = "TotalPoints")
    private Integer totalPoints;

    @Column(name = "ExtraPoints")
    private Integer extraPoints;

    @Column(name = "TotalPenalty")
    private Integer totalPenalty;

    @Column(name = "Nickname")
    private String Nickname;

    @Column(name = "Wins")
    private Integer wins;

    @Column(name = "TotalGames")
    private Integer totalGames;

    @Column(name = "MafiaWins")
    private Integer mafiaWins;

    @Column(name = "CitizensWins")
    private Integer citizensWins;

    @Column(name = "MafiaWGames")
    private Integer mafiaWGames;

    @Column(name = "CitizensGames")
    private Integer citizensGames;

    @Column(name = "WinRate")
    private Double winRate;

    @OneToOne
    @JoinColumn(name = "userId")
    private UserAdditional userAdditional;

}