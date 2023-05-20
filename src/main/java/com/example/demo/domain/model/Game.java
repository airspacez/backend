package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="GameID")
    private Integer Id;


    @ManyToOne
    @JoinColumn(name="PlaceID", nullable=false)
    private Place Place;

    @Column(name="GameDate")
    private Date Date;

    @Column(name="GameTable")
    private Integer Table;

    @Column(name="GameNumber")
    private Integer GameNumber;

    @Column(name="GameCheck")
    private Boolean isChecked;

    @ManyToOne
    @JoinColumn(name="GameTypeID", nullable=false)
    private GameType Type;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"altNickname", "email", "birthday_date", "username", "password", "mobile", "birthdayDate", "phoneNumber", "isArchived", "club", "status", "hibernateLazyInitializer", "handler"})
    @JoinColumn(name="GameLeadID", nullable = false)
    private UserAdditional Lead;

    @ManyToOne
    @JoinColumn(name="GameResultID", nullable=false)
    private GameResult Result;

    @OneToMany(mappedBy = "Game", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", })
    private List<GameMember> Members;

}
