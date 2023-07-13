package org.sg.kata.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance {
  private Long id;
  private BigDecimal credit;
  private BigDecimal debit;

  public AccountBalance(AccountBalance accountBalance) {
    this.id = accountBalance.id;
    this.credit = new BigDecimal(accountBalance.getCredit().toString());
    this.debit = new BigDecimal(accountBalance.getDebit().toString());
  }
}
