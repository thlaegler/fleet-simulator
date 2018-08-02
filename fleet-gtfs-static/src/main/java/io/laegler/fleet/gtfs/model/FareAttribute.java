package io.laegler.fleet.gtfs.model;

import static javax.persistence.EnumType.STRING;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fare_attributes")
public class FareAttribute {

  @Id
  @Column(name = "fare_id")
  private String fareId;

  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "currency_type")
  @Enumerated(STRING)
  private CurrencyType currencyType;

  @Column(name = "payment_method")
  private PaymentType paymentType;

  @Column(name = "transfer_type")
  private FareTransferType transferType;

  @Column(name = "transfer_duration")
  private Double transferDuration;

}
