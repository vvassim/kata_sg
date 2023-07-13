package org.sg.kata.application.adapters;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.ports.AccountInMemoryDataPort;

public class AccountInMemoryDataAdapter implements AccountInMemoryDataPort {

  private final Map<Long, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public Account createAccount(BigDecimal overdraft) {
    long id = (accounts.size() + 1);
    Account account = Account.builder().id((long) accounts.size()).overdraft(overdraft).build();
    accounts.put(id, account);
    return account;
  }
}
