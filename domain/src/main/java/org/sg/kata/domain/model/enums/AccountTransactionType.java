package org.sg.kata.domain.model.enums;

import lombok.Getter;

@Getter
public enum AccountTransactionType {
  WITHDRAW("WITHDRAW"),
  DEPOSIT("DEPOSIT");

  private final String value;

  AccountTransactionType(String val) {
    value = val;
  }
}
