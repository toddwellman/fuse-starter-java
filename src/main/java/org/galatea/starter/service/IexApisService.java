package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.galatea.starter.domain.IexHistoricalPrice;

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
    if (!StringUtils.isBlank(date)) {
      return iexApisClient.getHistoricalPricesByDate(symbol, date);
    } else {
      return iexApisClient.getHistoricalPricesByRange(symbol, range);
    }
  }

}
