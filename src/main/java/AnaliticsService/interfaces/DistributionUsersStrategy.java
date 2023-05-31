package AnaliticsService.interfaces;

import java.util.List;

public interface DistributionUsersStrategy<T> {
    List<List<T>> distribute(List<T> undistributed);
}
