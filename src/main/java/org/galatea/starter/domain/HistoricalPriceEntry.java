package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE) // For spring and jackson
@Builder
@Data
@Entity
public class HistoricalPriceEntry {

  @Id
  @JsonIgnore
  private String id;

  @NonNull
  private String symbol;
  @NonNull
  private LocalDate date;
  @NonNull
  private Integer volume;
  @NonNull
  private BigDecimal close;
  @NonNull
  private BigDecimal low;
  @NonNull
  private BigDecimal high;
  @NonNull
  private BigDecimal open;
}
