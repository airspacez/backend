package com.example.demo.domain.service;

import com.example.demo.domain.model.CompositeKeys.UserInEventPK;
import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.model.UserInEvent;
import com.example.demo.domain.model.projections.UserAdditionalPropections.UserProjection;
import com.example.demo.domain.repository.UserInEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserInEventService {

    @Autowired
    private final UserInEventRepository repository;

    @Autowired
    private final UserAdditionalService userAdditionalService;

    @Autowired
    private final EventService eventService;

    public UserInEventService(UserInEventRepository repository, UserAdditionalService userAdditionalService, EventService eventService) {
        this.repository = repository;
        this.userAdditionalService = userAdditionalService;
        this.eventService = eventService;
    }



    @Transactional
    public List<UserInEvent> getAll()
    {
        return repository.findAll();
    }

    @Transactional
    public Optional<UserInEvent> getById(UserInEventPK id)
    {
        return repository.findById(id);
    }



    @Transactional
    public List<UserAdditional> getUsersOfEventById(int id)
    {
        return repository.findUsersByEventId(id);
    }

    @Transactional
    public List<UserProjection> getUsersOfEventByIdByUsernameProjection(int id)
    {
        return repository.getUsersOfEventByIdByUsernameProjection(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void deleteById(Integer userId, Integer eventId)
    {
         repository.deleteById(new UserInEventPK(userId, eventId));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public UserInEvent save(UserInEvent uie)
    {
       return repository.save(uie);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void patch(Integer userId, Integer eventId, Map<String, Object> updates) throws Exception {
        var existingEntity = repository.findById(new UserInEventPK(userId, eventId));
        if (existingEntity.isPresent()) {

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {

                    case "appeared" -> existingEntity.get().setAppeared((Boolean) value);
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            repository.save(existingEntity.get());
        }
        else
            throw new Exception(UserInEvent.class.getName() +" class entity with id " + userId + " and " + eventId + " does not exist");
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void save(Integer userId, Integer eventId, Map<String, Object> updates) throws Exception {
        var newEntity = new UserInEvent();
            newEntity.setId(new UserInEventPK(userId, eventId));
            var user = userAdditionalService.getByID(userId);
            user.ifPresent(newEntity::setUser);
            var event = eventService.getById(eventId);
            event.ifPresent(newEntity::setEvent);
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {

                    case "appeared" -> newEntity.setAppeared((Boolean) value);
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            repository.save(newEntity);

    }




}
