package org.sg.kata.application.adapters;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sg.kata.domain.model.StatementLine;
import org.sg.kata.domain.ports.StatementPrinterLogPort;

@Slf4j
public class StatementPrinterLogAdapter implements StatementPrinterLogPort {

  public final String HEADER = "Date | type | Amount | Balance  | Status";

  @Override
  public void printStatementLines(List<StatementLine> statementLines) {
    printHeader();
    statementLines.forEach(this::print);
  }

  public void print(StatementLine statementLine) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dateTime =
        statementLine
            .getAccountTransaction()
            .getTransactionDateTime()
            .toLocalDateTime()
            .format(formatter);
    BigDecimal amount = statementLine.getAccountTransaction().getAmount();
    String status = statementLine.getAccountTransaction().getStatus().getValue();
    String type = statementLine.getAccountTransaction().getType().getValue();
    BigDecimal balance =
        statementLine
            .getAccountBalance()
            .getDebit()
            .subtract(statementLine.getAccountBalance().getCredit());
    log.info("{} | {} | {} | {} | {}", dateTime, type, amount, balance, status);
  }

  public void printHeader() {
    log.info(HEADER);
  }
}
