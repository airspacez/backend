package com.example.demo.domain.service;

import com.example.demo.domain.entityModel.PageModel;
import com.example.demo.domain.model.ClubRole;
import com.example.demo.domain.model.GameMember;
import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserAdditionalProjection;
import com.example.demo.domain.repository.ClubRoleRepository;
import com.example.demo.domain.repository.UserAdditionalRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserAdditionalService {
    @Autowired
    private final UserAdditionalRepository repository;
    @Autowired
    private final ClubRoleService ServiceC;

    public UserAdditionalService(UserAdditionalRepository UserAdditionalRepository, ClubRoleRepository repositoryC, ClubRoleService serviceC) {
        this.repository = UserAdditionalRepository;

        ServiceC = serviceC;
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(@NotNull UserAdditional UserAdditional) throws Exception {
        if (repository.findById(UserAdditional.getId()).isEmpty())
            repository.save(UserAdditional);
        else
            throw new Exception(UserAdditional.class.getName() +" class entity with id " + UserAdditional.getId() + " is already exist");
    }

    public Optional<UserAdditional> getByID(Integer id)
    {
        return repository.findById(id);
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



    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, })
    public void setRoles(@NotNull Integer userId,@NotNull List<Integer> rolesId) throws Exception {
        List<ClubRole> roles = new ArrayList<>();
        if (rolesId.isEmpty()) throw new Exception("No roles to set");
        Optional<UserAdditional> User = repository.findById(userId);

        if (User.isEmpty()) throw new Exception("User with id " + userId + "not found");

        for(Integer id:rolesId)
        {
            Optional<ClubRole> role = ServiceC.GetByID(id);
            if(role.isPresent()) {
                roles.add(role.get());
                role.get().getUsersWithThisRole().add(User.get());
            }
            else throw new Exception("No such role with id " + id + "in database");

        }
        User.get().setRoles(roles);
    }


}
