package com.example.demo.domain.service;

import com.example.demo.domain.model.Place;
import com.example.demo.domain.model.UserAdditional;
import com.example.demo.domain.repository.CityRepository;
import com.example.demo.domain.repository.PlaceRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlaceService {
    @Autowired
    private final PlaceRepository repository;

    @Autowired
    private final CityService cityService;

    public PlaceService(PlaceRepository PlaceRepository, CityRepository cityRepository, CityService cityService) {
        this.repository = PlaceRepository;
        this.cityService = cityService;
    }

    public List<Place> getAll()
    {
        return repository.findAll();
    }

    public Page<Place> getAll(Pageable pageable)
    {
        return repository.findAll(pageable);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void add(@NotNull Place Place) throws Exception {
            repository.save(Place);
    }

    public Optional<Place> getByID(Integer id)
    {
        return repository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, NoSuchElementException.class})
    public void deleteDetachedPlaceById(Integer id) throws Exception {
        Optional<Place> place = repository.findById(id);
        if(place.isEmpty()) throw new NoSuchElementException();
        if(place.get().getGamesInThisPlace().isEmpty())
            repository.deleteById(id);
        else
            throw new Exception(Place.class.getName() +" class entity with id " + id + " is already attached to Game entity");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void delete(Integer id) {
            repository.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void update(@NotNull Place Place) throws Exception {
        if (repository.findById(Place.getId()).isPresent())
            repository.save(Place);
        else
            throw new Exception(Place.class.getName() +" class entity with id " + Place.getId() + " does not exist");
    }


    @Transactional
    public Boolean entityHasAnyGamesById(int id)
    {
        return repository.hasAnyGames(id);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void patch(int id, Map<String, Object> updates) throws Exception {
        var existingEntity = repository.findById(id);
        if (existingEntity.isPresent()) {

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "description" -> existingEntity.get().setDescription((String)value);
                    case "address" -> existingEntity.get().setAddress((String)value);
                    case "city" -> {
                            var city = cityService.getByID((Integer) value);
                            city.ifPresent(club1 -> existingEntity.get().setCity(club1));
                        }
                    case "isArchived" -> existingEntity.get().setIsArchived((Boolean) value);
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            repository.save(existingEntity.get());
        }
        else
            throw new Exception(UserAdditional.class.getName() +" class entity with id " + id + " does not exist");
    }


}
