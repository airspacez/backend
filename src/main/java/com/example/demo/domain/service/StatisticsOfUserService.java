package com.example.demo.domain.service;


import com.example.demo.domain.model.StatisticsOfUser;
import com.example.demo.domain.model.UserStatistics;
import com.example.demo.domain.model.projections.UserStatistics.ShortUserRatings;
import com.example.demo.domain.repository.StatisticsOfUserRepository;
import com.example.demo.domain.repository.UserStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsOfUserService {
    @Autowired
    private final StatisticsOfUserRepository repository;
    public StatisticsOfUserService(StatisticsOfUserRepository repository) {
        this.repository = repository;
    }

    public Optional<StatisticsOfUser> getByID(Integer id)
    {
        return repository.findById(id);
    }

    @Transactional
    public List<StatisticsOfUser> getAllStatisticsOfUsersByEventId(Integer id)
    {
        return repository.findStatisticsByEventId(id);
    }
}
