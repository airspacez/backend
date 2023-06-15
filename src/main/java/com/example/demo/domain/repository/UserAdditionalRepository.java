package com.example.demo.domain.repository;

import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserAdditionalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Modifying
    @Transactional
    @Query("UPDATE UserAdditional u SET u.token = :token WHERE u.UserEmail = :UserEmail")
    void updateTokenByEmail(@Param("token") String token, @Param("UserEmail") String UserEmail);

    @Transactional
    @Query("SELECT u.token FROM UserAdditional u WHERE u.UserEmail = :UserEmail")
    String getToken(@Param("UserEmail") String UserEmail);

    @Transactional
    @Query("SELECT u FROM UserAdditional u WHERE u.UserEmail = :UserEmail")
    Optional<UserAdditional> findByUserEmail(@Param("UserEmail") String UserEmail);

    @Modifying
    @Transactional
    @Query("UPDATE UserAdditional u SET u.password = :password WHERE u.UserEmail = :UserEmail")
    void changePasswordByUserEmail(@Param("password") String password, @Param("UserEmail") String UserEmail);

    @Transactional
    @Query("SELECT u FROM UserAdditional u WHERE u.Id = :id")
    <T extends  UserAdditionalProjection> Optional<T> getUserByProjectionById(@Param("id") Integer id, Class<T> projectionType);


    @Transactional
    @Query("SELECT u FROM UserAdditional u WHERE u.Role.Id = :id")
    <T extends UserAdditionalProjection> List<T> findUsersByRoleId(@Param("id") int id, Class<T> projectionType);

}