package AnaliticsService.domain.SortUsersStrategies;

import AnaliticsService.interfaces.SortUsersStrategy;
import com.example.demo.domain.model.UserAdditional;

import java.util.List;

public class BasicSortUsersStrategy implements SortUsersStrategy<UserAdditional> {

    @Override
    public List<UserAdditional> sort(List<UserAdditional> unsorted) {
        return null;
    }
}
