package org.sg.kata.application.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg.kata.domain.model.StatementLine;

@ExtendWith(MockitoExtension.class)
class StatementPrinterLogAdapterTest {

  @Spy @InjectMocks StatementPrinterLogAdapter statementPrinterLogAdapter;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  StatementLine statementLine;

  @Test
  void printStatementLines_whenCalled_thenCallPrint() {
    // given
    List<StatementLine> statementLines = List.of(statementLine, statementLine);
    statementPrinterLogAdapter.printStatementLines(statementLines);
    verify(statementPrinterLogAdapter, times(2)).print(any(StatementLine.class));
    verify(statementPrinterLogAdapter, times(1)).printHeader();
  }
}
