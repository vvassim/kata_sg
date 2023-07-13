package org.sg.kata.domain.ports;

import java.math.BigDecimal;
import org.sg.kata.domain.model.Account;

public interface AccountInMemoryDataPort {
  Account createAccount(BigDecimal overdraft);
}
