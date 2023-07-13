package org.sg.kata.domain.ports;

import java.util.List;
import org.sg.kata.domain.model.StatementLine;

public interface StatementPrinterLogPort {
  void printStatementLines(List<StatementLine> statementLines);

  void print(StatementLine statementLine);
}
