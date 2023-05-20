package com.example.demo.domain.service;

import com.example.demo.domain.model.Game;
import com.example.demo.domain.model.UserStatistics;
import com.example.demo.domain.model.projections.UserStatistics.ShortUserRatings;
import com.example.demo.domain.repository.GameRepository;
import com.example.demo.domain.repository.UserStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStatisticsService {
    @Autowired
    private final UserStatisticsRepository repository;
    public UserStatisticsService(UserStatisticsRepository repository) {
        this.repository = repository;
    }

    public Optional<UserStatistics> getByID(Integer id)
    {
        return repository.findById(id);
    }

    public Page<ShortUserRatings> getAllShort(String searchString, Pageable pageable)
    {
        return repository.findPlayerRatings(searchString, pageable);
    }
}
