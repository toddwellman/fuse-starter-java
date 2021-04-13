package org.galatea.starter.domain.rpsy;

import java.time.LocalDate;
import java.util.List;
import org.galatea.starter.domain.HistoricalPriceEntry;
import org.springframework.data.repository.CrudRepository;

public interface HistoricalPriceRpsy extends CrudRepository<HistoricalPriceEntry, String> {

  List<HistoricalPriceEntry> findBySymbolIgnoreCase(String symbol);

  List<HistoricalPriceEntry> findBySymbolIgnoreCaseAndDateBetween(String symbol, LocalDate start, LocalDate end);
}
