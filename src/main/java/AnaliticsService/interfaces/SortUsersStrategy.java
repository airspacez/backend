package AnaliticsService.interfaces;

import com.example.demo.domain.model.StatisticsOfUser;
import com.example.demo.domain.model.UserInEvent;

import java.util.List;

public interface SortUsersStrategy {
     List<StatisticsOfUser> sort(List<StatisticsOfUser> unsorted);
}
