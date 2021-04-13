package org.galatea.starter.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.galatea.starter.domain.HistoricalPriceEntry;
import org.galatea.starter.domain.IexHistoricalPrice;

import org.galatea.starter.domain.rpsy.HistoricalPriceRpsy;
import org.springframework.stereotype.Service;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexApisService {

  @NonNull
  private IexApisClient iexApisClient;

  @NonNull
  private HistoricalPriceRpsy historicalPriceRpsy;

  private DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
  private DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Get the historical price for the Symbol that is passed in over the range.
   *
   * @param symbol the symbols to get a historical price for.
   * @param range the range to get the hist price for
   * @param date the date to get the hist price for
   * @return a list of historical price objects for the Symbol that is passed in.
   */
  public List<IexHistoricalPrice> getHistoricalPriceForSymbol(final String symbol, final String range, final String date) {
    if (StringUtils.isBlank(symbol)) {
      return Collections.emptyList();
    }

    LocalDate startDate;
    LocalDate endDate;
    if (!StringUtils.isBlank(date)) {
      startDate = LocalDate.parse(date, inputFormatter);
      endDate = LocalDate.parse(date, inputFormatter);
    } else {
      startDate = findRangeStart(range);
      endDate = LocalDate.now();
    }

    List<HistoricalPriceEntry> storedPrices = historicalPriceRpsy.findBySymbolIgnoreCaseAndDateBetween(
        symbol, startDate, endDate);

    if (storedPrices.isEmpty() || storedPrices.size() < findDaysBetween(startDate, endDate) - 1) {
      List<IexHistoricalPrice> historicalPrices = null;
      if (!StringUtils.isBlank(date)) {
        historicalPrices = iexApisClient.getHistoricalPricesByDate(symbol, date);
      } else {
        historicalPrices = iexApisClient.getHistoricalPricesByRange(symbol, range);
      }
      if (historicalPrices != null) {
        List<HistoricalPriceEntry> historicalPriceEntries = historicalPrices.stream()
            .map(p -> buildHistoricalPriceEntry(p))
            .collect(Collectors.toList());
        historicalPriceRpsy.saveAll(historicalPriceEntries);
      }

      return historicalPrices;
    } else {
      return storedPrices.stream()
          .map(p -> new IexHistoricalPrice(p.getSymbol(), p.getDate().format(outputFormatter), p.getVolume(), p.getClose(), p.getLow(), p.getHigh(), p.getOpen()))
          .collect(Collectors.toList());
    }
  }

  private LocalDate findRangeStart(String range) {
    if (Character.toUpperCase(range.charAt(1)) == 'Y') {
      return LocalDate.now().minusYears(Character.getNumericValue(range.charAt(0)));
    } else if (Character.toUpperCase(range.charAt(1)) == 'M' && range.length() == 2) {
      return LocalDate.now().minusMonths(Character.getNumericValue(range.charAt(0)));
    } else if (Character.toUpperCase(range.charAt(1)) == 'W') {
      return LocalDate.now().minusWeeks(Character.getNumericValue(range.charAt(0)));
    } else if (range.equalsIgnoreCase("ytd")) {
      return LocalDate.now().withDayOfYear(1);
    } else if (range.equalsIgnoreCase("max")) {
      return LocalDate.now().minusYears(15);
    } else {
      // doing a plus day to make it find no results stored and look up in the default case
      // going to be needed for the x minute interval requests
      return LocalDate.now().minusMonths(1);
    }
  }

  private long findDaysBetween (LocalDate start, LocalDate end) {
    Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    return start.datesUntil(end)
        .filter(d -> !weekend.contains(d.getDayOfWeek()))
        .count();
  }

  private HistoricalPriceEntry buildHistoricalPriceEntry (IexHistoricalPrice p) {
      return new HistoricalPriceEntry(p.getSymbol() + p.getDate(), p.getSymbol(),
          LocalDate.parse(p.getDate()), p.getVolume(), p.getClose(), p.getLow(), p.getHigh(),
          p.getOpen());
  }

}
