package org.sg.kata.domain.usecases;

import java.math.BigDecimal;

import org.sg.kata.domain.exceptions.InsufficientBalanceException;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.model.AccountBalance;

public interface WithdrawalUseCase {
  AccountBalance withdrawal(Account account, BigDecimal amount) throws InsufficientBalanceException;
}
