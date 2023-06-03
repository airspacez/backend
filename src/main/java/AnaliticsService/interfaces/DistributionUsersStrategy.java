package AnaliticsService.interfaces;

import com.example.demo.domain.model.StatisticsOfUser;
import com.example.demo.domain.model.UserStatistics;

import java.util.List;


public interface DistributionUsersStrategy {
    List<List<StatisticsOfUser>> distribute(List<StatisticsOfUser> undistributed);
}
