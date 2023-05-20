package com.example.demo.domain.repository;

import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserAdditionalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserAdditionalRepository extends JpaRepository<UserAdditional,Integer> {
    @Transactional
    UserAdditional findByUsername(String username);

    @Transactional
    @Query("SELECT u FROM UserAdditional u WHERE " +
            "(:searchString IS NULL OR :searchString = '') OR " +
            "(u.Name LIKE %:searchString% OR " +
            "u.Nickname LIKE %:searchString% OR " +
            "u.AltNickname LIKE %:searchString%)")
    Page<UserAdditional> findBySearchString(@Param("searchString") String searchString,
                                            Pageable pageable);

    @Transactional
    @Query("SELECT u FROM UserAdditional u WHERE " +
            "(:searchString IS NULL OR :searchString = '') OR " +
            "(u.Name LIKE %:searchString% OR " +
            "u.Nickname LIKE %:searchString% OR " +
            "u.AltNickname LIKE %:searchString%)")
    <T extends UserAdditionalProjection> Page<T> findBySearchString(@Param("searchString") String searchString,
                                                                    Pageable pageable, Class<T> projectionType);

}
