package com.example.demo.domain.service.DistributionUsersStrategies;

import AnaliticsService.domain.StrategyTypes.DistributionType;
import AnaliticsService.interfaces.DistributionUsersStrategy;
import com.example.demo.domain.model.StatisticsOfUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(DistributionType.BASIC)
public class BasicDistributionStrategy implements DistributionUsersStrategy {


    @Override
    public List<List<StatisticsOfUser>> distribute(List<StatisticsOfUser> undistributed) {
        int size = undistributed.size();
        List<List<StatisticsOfUser>> dividedList = new ArrayList<>();

        if (size < 10) {
            // Если количество элементов меньше 10, возвращаем исходный список без изменений
            dividedList.add(undistributed);
        } else {
            if (size % 10 == 0) {
                // Если количество элементов кратно 10, разделяем на списки по 10 элементов
                for (int i = 0; i < size; i += 10) {
                    dividedList.add(undistributed.subList(i, i + 10));
                }
            } else {
                // Если количество элементов не кратно 10, разделяем примерно поровну с минимум 10 и максимум 15 элементами
                int numLists = size / 10;
                int remainingElements = size % 10;
                int sublistSize = Math.min(Math.max(10, size / numLists), 15);

                int startIndex = 0;
                for (int i = 0; i < numLists; i++) {
                    int endIndex = startIndex + sublistSize;
                    dividedList.add(undistributed.subList(startIndex, endIndex));
                    startIndex = endIndex;
                }

                dividedList.add(undistributed.subList(startIndex, size));
            }
        }

        return dividedList;
    }
}