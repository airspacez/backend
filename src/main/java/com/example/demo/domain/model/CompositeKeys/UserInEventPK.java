package com.example.demo.domain.model.CompositeKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInEventPK implements Serializable {

    @Column(name="userId")
    private Integer userId;

    @Column(name="eventId")
    private Integer eventId;
}
