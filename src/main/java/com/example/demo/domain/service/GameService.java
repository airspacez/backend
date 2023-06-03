package com.example.demo.domain.service;


import com.example.demo.domain.entityModel.GameModel;
import com.example.demo.domain.entityModel.PageModel;
import com.example.demo.domain.model.Game;
import com.example.demo.domain.model.GameMember;
import com.example.demo.domain.repository.GameRepository;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class GameService {
    @Autowired
    private final GameRepository repository;

    @Autowired
    private final GameMemberService memberService;


    @Autowired
    private final AnaliticsService analiticsService;

    public GameService(GameRepository GameRepository, GameMemberService memberService, AnaliticsService analiticsService) {
        this.repository = GameRepository;
        this.memberService = memberService;
        this.analiticsService = analiticsService;
    }

    public Page<Game> getAll(Pageable pageable)
    {
        return repository.findAll(pageable);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(@NotNull Game Game) throws Exception {
                repository.save(Game);
    }
    public Optional<Game> getByID(Integer id)
    {
        return repository.findById(id);
    }

    public Optional<Game> getByIDWithMembers(Integer id)
    {
        var entity = repository.findById(id);
        entity.ifPresent(game -> Hibernate.initialize(game.getMembers()));
        return entity;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, NoSuchElementException.class})
    public void deleteDetachedGameById(Integer id) throws Exception {
        Optional<Game> game = repository.findById(id);
        if(game.isEmpty()) throw new NoSuchElementException();
        if(game.get().getMembers().isEmpty())
            repository.deleteById(id);
        else
            throw new Exception(Game.class.getName() +" class entity with id " + id + " is already attached to Member entity");
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NoSuchElementException.class})
    public void deleteById(Integer id)
    {
        Optional<Game> game = repository.findById(id);
        if(game.isEmpty())
            throw new NoSuchElementException();
        else
            repository.deleteById(id);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(@NotNull Game Game) throws Exception {
        if (Game.getId()!=null)
        {
        if (repository.findById(Game.getId()).isPresent())
            repository.save(Game);
        else
            throw new Exception(Game.class.getName() +" class entity with id " + Game.getId() + " does not exist");
        }
    }
    @Transactional
    public PageModel<GameModel> getAllGamesArhived(Pageable pageable)
    {
        var page = getAll(pageable);

        List<Game> games = page.getContent();
        List<GameModel> gm = new ArrayList<>();
        for (Game game:games) {
            GameModel model = new GameModel();
            model.setId(game.getId());
            model.setDate(game.getDate());
            model.setGameNumber(game.getGameNumber());
            model.setResult(game.getResult());
            model.setPlace(game.getPlace());
            model.setType(game.getType());
            model.setTable(game.getTable());

            gm.add(model);
        }
        return new PageModel<>(gm,page.getTotalPages());
    }

    @Transactional
    public PageModel<GameModel> getAllGamesArchivedByDate(Integer day, Integer month, Integer year, Integer typeId, Integer placeId, Integer gameResultId,  Pageable pageable)
    {
        var page = repository.findGamesByCriteria(day, month, year, typeId, placeId, gameResultId, pageable);

        List<Game> games = page.getContent();
        List<GameModel> gm = new ArrayList<>();
        for (Game game:games) {
            GameModel model = new GameModel();
            model.setId(game.getId());
            model.setDate(game.getDate());
            model.setGameNumber(game.getGameNumber());
            model.setResult(game.getResult());
            model.setPlace(game.getPlace());
            model.setType(game.getType());
            model.setTable(game.getTable());

            gm.add(model);
        }
        return new PageModel<>(gm,page.getTotalPages());
    }


    @Transactional
    public Game PatchItem(Integer id, Map<Object, Object> fields)
    {
        Optional<Game> game = repository.findById(id);
        if (game.isPresent())
        {
            fields.forEach((key,value) -> {
                Field field = ReflectionUtils.findField(Game.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, game.get(), value);
            });
            return game.get();
        }
        return null;
    }



    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void setGameAndMembersAndComputeRating(Game gameData, List<GameMember> members) throws Exception {
        var game = repository.save(gameData);
        for (var member:members) {
            memberService.save(member);
        }
        analiticsService.computeRatingOfUsersByGame(game);

    }


}
