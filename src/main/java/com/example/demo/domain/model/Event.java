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
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="eventId")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="datetime")
    private Timestamp datetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PlaceID", nullable=false)
    private Place place;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<UserInEvent> usersInEvent;




}
