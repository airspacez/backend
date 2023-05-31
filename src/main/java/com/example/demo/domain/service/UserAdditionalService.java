package com.example.demo.domain.service;
import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserAdditionalProjection;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UsernameUserProjection;
import com.example.demo.domain.repository.ClubRoleRepository;
import com.example.demo.domain.repository.UserAdditionalRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

@Service
public class UserAdditionalService {
    @Autowired
    private final UserAdditionalRepository repository;
    @Autowired
    private final ClubRoleService clubRoleService;
    @Autowired
    private final ClubService clubService;
    @Autowired
    private final ClubStatusService clubStatusService;

    public UserAdditionalService(UserAdditionalRepository UserAdditionalRepository, ClubRoleRepository repositoryC, ClubRoleService serviceC, ClubService clubService, ClubStatusService clubStatusService) {
        this.repository = UserAdditionalRepository;

        clubRoleService = serviceC;
        this.clubService = clubService;
        this.clubStatusService = clubStatusService;
    }

    public Page<UserAdditional> getAll(Pageable pageable)
    {
        return repository.findAll(pageable);
    }

    public Page<UserAdditional> getAllBySearchString(String searchString, Pageable pageable)
    {
        return repository.findBySearchString(searchString, pageable);
    }

     public <T extends UserAdditionalProjection> Page<T> getAllBySearchString(String searchString, Pageable pageable, Class<T> type)
    {
        return repository.findBySearchString(searchString, pageable, type);
    }


    public <T extends UserAdditionalProjection> Optional<T> getByProjectionById(int id,  Class<T> type)
    {
        return repository.getUserByProjectionById(id, type);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(@NotNull UserAdditional UserAdditional) throws Exception {
        if (repository.findById(UserAdditional.getId()).isEmpty())
            repository.save(UserAdditional);
        else
            throw new Exception(UserAdditional.class.getName() +" class entity with id " + UserAdditional.getId() + " is already exist");
    }

    public String activateToken(String mail){
        String token = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())).toUpperCase();
        repository.updateTokenByEmail(token, mail);
        return token;
    }

    public Optional<UserAdditional> getByID(Integer id)
    {
        return repository.findById(id);
    }

    public Optional<UserAdditional> getByEmail(String email)
    {
        return repository.findByUserEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, NoSuchElementException.class})
    public void deleteDetachedUser(Integer id) throws Exception {
        Optional<UserAdditional> user = repository.findById(id);
        if(user.isEmpty()) throw new NoSuchElementException();
        if(user.get().getMembers().isEmpty())
            repository.deleteById(id);
        else
            throw new Exception(UserAdditional.class.getName() +" class entity with id " + id + " is already attached to Game entity");
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(@NotNull UserAdditional UserAdditional) throws Exception {
        if (repository.findById(UserAdditional.getId()).isPresent())
            repository.save(UserAdditional);
        else
            throw new Exception(UserAdditional.class.getName() +" class entity with id " + UserAdditional.getId() + " does not exist");
    }



    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void patch(int id, Map<String, Object> updates) throws Exception {
        var existingUser = repository.findById(id);
        if (existingUser.isPresent()) {

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "altNickname" -> existingUser.get().setAltNickname((String) value);
                    case "nickname" -> existingUser.get().setNickname((String) value);
                    case "name" -> existingUser.get().setName((String) value);
                    case "email" -> existingUser.get().setEmail((String) value);
                    case "isMale" -> existingUser.get().setIsMale((Boolean) value);
                    case "photoImagePath" -> existingUser.get().setPhotoImagePath((String) value);
                    case "username" -> existingUser.get().setUsername((String) value);
                    case "phoneNumber" -> existingUser.get().setPhoneNumber((String) value);
                    case "isArchived" -> existingUser.get().setIsArchived((Boolean) value);
                    case "birthdayDate" -> {if (value!=null) { existingUser.get().setBirthdayDate(Date.valueOf((String)value));} else {existingUser.get().setBirthdayDate(null);}}
                    case "club" -> {
                        var club = clubService.GetByID((Integer) value);
                        club.ifPresent(club1 -> existingUser.get().setClub(club1));
                    }
                    case "status" -> {
                        var status = clubStatusService.GetByID((Integer) value);
                        status.ifPresent(status1 -> existingUser.get().setStatus(status1));
                    }
                    case "role" -> {
                        var role = clubRoleService.GetByID((Integer) value);
                        role.ifPresent(role1 -> existingUser.get().setRole(role1));
                    }
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            repository.save(existingUser.get());
        }
        else
            throw new Exception(UserAdditional.class.getName() +" class entity with id " + id + " does not exist");
    }





}
