package org.galatea.starter.domain;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IexHistoricalPrice {
  private String symbol;
  private String date;
  private Integer volume;
  private BigDecimal close;
  private BigDecimal low;
  private BigDecimal high;
  private BigDecimal open;
}
