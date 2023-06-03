package com.example.demo.domain.model.projections.UserAdditionalPropections;

import java.util.List;

public interface UserProjection {
    List<UsernameUserProjection> getUser();
    Boolean getAppeared();
    Integer getTableNumber();

    Integer getLeadId();


}
