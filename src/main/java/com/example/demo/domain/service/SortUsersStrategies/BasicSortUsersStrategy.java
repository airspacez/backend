package com.example.demo.domain.service.SortUsersStrategies;

import AnaliticsService.domain.StrategyTypes.SortType;
import AnaliticsService.interfaces.SortUsersStrategy;
import com.example.demo.domain.model.StatisticsOfUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(SortType.BASIC)
public class BasicSortUsersStrategy implements SortUsersStrategy {
    @Override
    public List<StatisticsOfUser> sort(List<StatisticsOfUser> unsorted) {
         unsorted.sort((StatisticsOfUser o1, StatisticsOfUser o2)->(int)(o2.getRating()-o1.getRating()));
         return unsorted;
    }
}
