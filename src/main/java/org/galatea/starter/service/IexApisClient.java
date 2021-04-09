package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A Feign Declarative REST Client to access endpoints from the Free and Open IEX API to get market
 * data. See https://iextrading.com/developer/docs/
 */
@FeignClient(name = "IEXApis", url = "${spring.rest.iexApisBasePath}")
public interface IexApisClient {

  /**
   * Get the historical price for the stock symbol passed in. See https://iextrading.com/developer/docs/#historical-prices.
   *
   * @param symbol stock symbols to get historical price for.
   * @return a list of the prices for the symbol passed in.
   */
  @GetMapping("/stock/{symbol}/chart/{range}?token=${spring.iexToken}")
  List<IexHistoricalPrice> getHistoricalPricesByRange(@PathVariable("symbol") String symbol,
      @PathVariable("range") String range);

  /**
   * Get the historical price for the stock symbol passed in. See https://iextrading.com/developer/docs/#historical-prices.
   *
   * @param symbol stock symbols to get historical price for.
   * @return a list of the prices for the symbol passed in.
   */
  @GetMapping("/stock/{symbol}/chart/date/{date}?chartByDay=true&token=${spring.iexToken}")
  List<IexHistoricalPrice> getHistoricalPricesByDate(@PathVariable("symbol") String symbol,
      @PathVariable("date") String date);

}
