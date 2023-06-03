package com.example.demo.domain.service.DistributionUsersStrategies;

import AnaliticsService.interfaces.DistributionUsersStrategy;
import AnaliticsService.interfaces.SortUsersStrategy;
import com.example.demo.domain.model.StatisticsOfUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public class DistributeStrategyFactory {
    private final Map<String, DistributionUsersStrategy> distributeUsersStrategyMap;

    public DistributeStrategyFactory(Map<String, SortUsersStrategy> sortUsersStrategyMap, Map<String, DistributionUsersStrategy> distributeUsersStrategyMap) {
        this.distributeUsersStrategyMap = distributeUsersStrategyMap;

    }


    public DistributionUsersStrategy getDistributionStrategy(String distributionStrategyType) {
        DistributionUsersStrategy sortUsersStrategy = distributeUsersStrategyMap.get(distributionStrategyType);
        if (sortUsersStrategy == null) {
            throw new RuntimeException("Unsupported strategy type");
        }

        return sortUsersStrategy;
    }

    public List<List<StatisticsOfUser>> execute(String distributionStrategyType, List<StatisticsOfUser> users) {
        DistributionUsersStrategy strategy = getDistributionStrategy(distributionStrategyType);
        return strategy.distribute(users);
    }


}
