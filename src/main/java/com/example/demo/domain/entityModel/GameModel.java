package com.example.demo.domain.entityModel;

import com.example.demo.domain.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GameModel {
    private Integer Id;

    private Place Place;

    private Date Date;

    private Integer Table;

    private Integer GameNumber;

    private Boolean isChecked;

    private GameType Type;

    private UserAdditional Lead;

    private GameResult Result;

    private List<GameMember> Members;

}
