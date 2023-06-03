package com.example.demo.domain.service.SortUsersStrategies;

import AnaliticsService.interfaces.SortUsersStrategy;
import com.example.demo.domain.model.StatisticsOfUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SortStrategyFactory {

    private final Map<String, SortUsersStrategy> sortUsersStrategyMap;

    public SortStrategyFactory(Map<String, SortUsersStrategy> sortUsersStrategyMap) {
        this.sortUsersStrategyMap = sortUsersStrategyMap;
    }


    public SortUsersStrategy geUserSortStrategy(String sortStrategyType) {
        SortUsersStrategy sortUsersStrategy = sortUsersStrategyMap.get(sortStrategyType);
        if (sortUsersStrategy == null) {
            throw new RuntimeException("Unsupported strategy type");
        }

        return sortUsersStrategy;
    }

    public List<StatisticsOfUser> execute(String sortStrategyType, List<StatisticsOfUser> users) {
        SortUsersStrategy notificationService = geUserSortStrategy(sortStrategyType);
        return notificationService.sort(users);
    }


}
