package com.example.demo.domain.service;

import com.example.demo.domain.model.Event;
import com.example.demo.domain.model.projections.EventProjections.EventProjection;
import com.example.demo.domain.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private final EventRepository repository;

    @Autowired
    private final PlaceService placeService;

    public EventService(EventRepository repository, PlaceService placeService) {
        this.repository = repository;
        this.placeService = placeService;
    }


    public List<Event> getAll()
    {
        return repository.findAll();
    }

    public <T extends EventProjection> Optional<T> getEventByIdThroughtProjection(int id, Class<T> projection)
    {
        return repository.getEventWithUsersById(id, projection);
    }

    @Transactional
    public Optional<Event> getById(int id)
    {
        return repository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void patch(int id, Map<String, Object> updates) throws Exception {
        var existingEntity = repository.findById(id);
        if (existingEntity.isPresent()) {

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "name" -> existingEntity.get().setName((String) value);
                    case "description" -> existingEntity.get().setDescription((String) value);
                    case "datetime" -> existingEntity.get().setDatetime(Timestamp.valueOf((String)value));
                    case "place" -> {
                        var place = placeService.getByID((Integer)value);
                        place.ifPresent(existingEntity.get()::setPlace);
                    }
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            repository.save(existingEntity.get());
        }
        else
            throw new Exception(Event.class.getName() +" class entity with id " + id + " does not exist");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Event save( Map<String, Object> updates) throws Exception {

        Event newEvent = new Event();
        newEvent.setDistributed(false);
        newEvent.setState("Не начато");
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "name" -> newEvent.setName((String) value);
                    case "description" -> newEvent.setDescription((String) value);
                    case "datetime" -> newEvent.setDatetime(Timestamp.valueOf((String)value));
                    case "place" -> {
                        var place = placeService.getByID((Integer)value);
                        place.ifPresent(newEvent::setPlace);
                    }

                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
           return repository.save(newEvent);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void deleteById(int id) throws Exception {
       repository.deleteById(id);
    }



}
