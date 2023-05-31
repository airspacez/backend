package com.example.demo.domain.service;

import com.example.demo.domain.model.Club;
import com.example.demo.domain.model.projections.ClubProjections.ClubWithCityProjection;
import com.example.demo.domain.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClubService {
    @Autowired
    private final ClubRepository repository;

    @Autowired
    private final CityService cityService;

    public ClubService(ClubRepository clubRepository, CityService cityService) {
        this.repository=clubRepository;
        this.cityService = cityService;
    }
    @Transactional
    public List<Club> getAll()
    {
        return repository.findAll();
    }
    @Transactional
    public Integer AddHero(Club club) {return repository.save(club).getId();}
    @Transactional
    public Optional<Club> GetByID(Integer id)
    {
        return repository.findById(id);
    }
    @Transactional
    public void Delete(Integer id)
    {
        repository.deleteById(id);
    }
    @Transactional
    public void Update(Club club)
    {
        repository.save(club);
    }

    @Transactional
    public  List<ClubWithCityProjection> getAllWithCity()
    {
        return repository.findAllWithCity();
    }

    @Transactional
    public  Optional<ClubWithCityProjection> getByIdWithCity(int id)
    {
        return repository.findByIdWithCity(id);
    }

    @Transactional
    public  Boolean isHasAnyUsersInClubById(int id)
    {
        return repository.hasAnyUsers(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void patch(int id, Map<String, Object> updates) throws Exception {
        var existingEntity = repository.findById(id);
        if (existingEntity.isPresent()) {

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "clubName" -> existingEntity.get().setClubName((String)value);
                    case "reportGroup" -> existingEntity.get().setReportGroup((String)value);
                    case "defaultClub" -> existingEntity.get().setIsDefaultClub((Boolean) value);
                    case "city" -> {
                        var city = cityService.getByID((Integer)value);
                        city.ifPresent(city1 -> existingEntity.get().setCity(city1));
                    }
                    default -> {
                    }
                    // Игнорирование неизвестных полей
                }
            }
            repository.save(existingEntity.get());
        }
        else
            throw new Exception(Club.class.getName() +" class entity with id " + id + " does not exist");
    }

}
