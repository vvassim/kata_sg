package org.sg.kata.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatementLine {

  private AccountTransaction accountTransaction;
  private AccountBalance accountBalance;
}
