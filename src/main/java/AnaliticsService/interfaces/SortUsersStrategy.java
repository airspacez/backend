package AnaliticsService.interfaces;

import java.util.List;

public interface SortUsersStrategy<T> {
     List<T> sort(List<T> unsorted);
}
