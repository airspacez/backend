package com.example.demo.domain.service;

import com.example.demo.domain.model.CompositeKeys.GameMemberPK;
import com.example.demo.domain.model.GameMember;
import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.repository.GameMemberRepository;
import com.example.demo.domain.repository.UserAdditionalRepository;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameMemberService {
    @Autowired
    private final GameMemberRepository repository;

    @Autowired
    private final UserAdditionalRepository userAdditionalRepository;

    public GameMemberService(GameMemberRepository GameMemberRepository, UserAdditionalRepository userAdditionalRepository) {
        this.repository = GameMemberRepository;
        this.userAdditionalRepository = userAdditionalRepository;
    }

    public List<GameMember> getAll()
    {
        return repository.findAll();
    }

    @Transactional
    public List<GameMember> getByGameID(@NotNull Integer id)
    {
        return repository.findAllById_GameId(id);
    }
    @Transactional
    public List<GameMember> getByUserID(@NotNull Integer id)
    {
        return repository.findAllById_UserId(id);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(@NotNull GameMember GameMember) throws Exception {
        if(repository.findById(GameMember.getId()).isPresent()) throw new Exception("Entity GameMember with id (UserId="+GameMember.getId().getUserId()+", GameId="+GameMember.getId().getUserId()+") is already exist");
        repository.save(GameMember);
    }

    public void update(@NotNull GameMember GameMember) throws Exception {
        if(repository.findById(GameMember.getId()).isEmpty()) throw new Exception("Entity GameMember with id (UserId="+GameMember.getId().getUserId()+", GameId="+GameMember.getId().getUserId()+") does not exist exist");

        repository.save(GameMember);
    }

    @Transactional
    public int countRecentGameDaysForUser(int id, Date number) {
        return repository.countGamesPlayedSinceDate(id, number);
    }

    @Transactional
    public GameMember save(@NotNull GameMember gameMember) {
        return repository.save(gameMember);
    }


    @Transactional
    public Optional<GameMember> getById(GameMemberPK gameMemberPK)
    {
       return repository.findById(gameMemberPK);
    }

    @Transactional
    public List<GameMember> getLastNGameMembersByUser(UserAdditional user, Pageable pageable)
    {
       var members = repository.findLastNGamesByUser(user, pageable);
        members.forEach(gm -> Hibernate.initialize(gm.getGame()));
        return members.getContent();
    }






    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NoSuchElementException.class})
    public void delete(@NotNull GameMember GameMember)
    {
        Optional<GameMember> member = repository.findById(GameMember.getId());
        if(member.isEmpty())
            throw new NoSuchElementException();
        else
            repository.delete(GameMember);
    }


}
