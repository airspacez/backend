package AnaliticsService.interfaces;

import java.util.List;

public interface UserAbsencionComputeStrategy<T> {
    List<T> getAbsencionUsersByNDays(int days);
}
