package com.example.demo.domain.model;

import com.example.demo.domain.model.CompositeKeys.GameMemberPK;
import com.example.demo.domain.model.CompositeKeys.UserInEventPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users_in_event")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInEvent {
    @EmbeddedId
    private UserInEventPK id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "eventId")
    private Event event;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "UserID")
    private UserAdditional user;

    @Column(name="appeared")
    private Boolean appeared;

    @Column(name="table_number")
    private Integer tableNumber;

    @Column(name="lead_id")
    private Integer leadId;

}