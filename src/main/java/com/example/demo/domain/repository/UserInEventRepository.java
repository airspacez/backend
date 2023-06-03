package com.example.demo.domain.repository;
import com.example.demo.domain.model.CompositeKeys.UserInEventPK;
import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.UserInEvent;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserProjection;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UsernameUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInEventRepository extends JpaRepository<UserInEvent, UserInEventPK> {

    @Query("SELECT uie.user FROM UserInEvent uie WHERE uie.id.eventId = :eventId")
    List<UserAdditional> findUsersByEventId(@Param("eventId") Integer eventId);
    @Transactional
    @Query("SELECT uie FROM UserInEvent uie WHERE uie.id.eventId = :eventId")
    List<UserProjection> getUsersOfEventByIdByUsernameProjection(@Param("eventId") Integer eventId);
    @Transactional
    @Query("SELECT uie FROM UserInEvent uie WHERE uie.id.eventId = :eventId AND uie.tableNumber = :tableNo")
    List<UserInEvent> getByEventIdAndTableNumber(@Param("eventId") Integer eventId, @Param("tableNo") Integer tableNo);
}
