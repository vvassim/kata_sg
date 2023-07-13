package org.sg.kata.application;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.sg.kata.application.adapters.AccountInMemoryDataAdapter;
import org.sg.kata.application.adapters.StatementLineInMemoryDataAdapter;
import org.sg.kata.application.adapters.StatementPrinterLogAdapter;
import org.sg.kata.application.cli.AccountCli;
import org.sg.kata.domain.model.Account;
import org.sg.kata.domain.ports.AccountInMemoryDataPort;
import org.sg.kata.domain.ports.StatementLineInMemoryDataPort;
import org.sg.kata.domain.ports.StatementPrinterLogPort;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    AccountInMemoryDataPort accountInMemoryDataAdapter = new AccountInMemoryDataAdapter();
    StatementPrinterLogPort statementPrinterLogAdapter = new StatementPrinterLogAdapter();
    StatementLineInMemoryDataPort statementLineInMemoryDataAdapter =
        new StatementLineInMemoryDataAdapter();
    AccountCli accountCli =
        new AccountCli(
            statementLineInMemoryDataAdapter,
            accountInMemoryDataAdapter,
            statementPrinterLogAdapter);

    // single thread environment
    BigDecimal overdraft = new BigDecimal(100);
    Account account = accountCli.createAccount(overdraft);
    accountCli.deposit(account, new BigDecimal(100));
    accountCli.withdrawal(account, new BigDecimal(200));
    accountCli.withdrawal(account, new BigDecimal(300));
    accountCli.deposit(account, new BigDecimal(100));
    accountCli.deposit(account, new BigDecimal(500));
    accountCli.printAllAccountStatements(account);

    // multi thread environment
    BigDecimal overdraftMt = new BigDecimal(0);
    Account accountMt = accountCli.createAccount(overdraftMt);
    accountCli.deposit(accountMt, new BigDecimal(200));
    CountDownLatch latch = new CountDownLatch(2);
    Runnable withdrawOperation =
        () -> {
          accountCli.withdrawal(accountMt, new BigDecimal(200));
          latch.countDown();
        };
    ExecutorService executor = Executors.newCachedThreadPool();
    executor.execute(withdrawOperation);
    executor.execute(withdrawOperation);
    executor.shutdown();
    latch.await();
    accountCli.printAllAccountStatements(accountMt);
  }
}
