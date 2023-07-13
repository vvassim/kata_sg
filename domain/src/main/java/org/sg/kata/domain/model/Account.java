package org.sg.kata.domain.model;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
  private final Set<AccountTransaction> transactions = new LinkedHashSet<>();
  private Long id;
  private AccountBalance balance;
  private BigDecimal overdraft;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    return obj != null && this.getClass() == obj.getClass() && ((Account) obj).id.equals(this.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
