package org.sg.kata.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import org.sg.kata.domain.model.enums.AccountTransactionStatus;
import org.sg.kata.domain.model.enums.AccountTransactionType;

@Data
@Builder
public class AccountTransaction {
  private Long id;
  private BigDecimal amount;
  private OffsetDateTime transactionDateTime;
  private AccountTransactionType type;
  private AccountTransactionStatus status;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    return ((AccountTransaction) obj).id.equals(this.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
