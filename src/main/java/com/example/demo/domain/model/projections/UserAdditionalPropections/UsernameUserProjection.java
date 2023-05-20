package com.example.demo.domain.model.projections.UserAdditionalPropections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




public interface UsernameUserProjection extends UserAdditionalProjection {
    Integer getId();
    String getAltNickname();
    String getName();
    String getNickname();
    Boolean getIsMale();
}
