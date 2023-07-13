package org.sg.kata.domain.model.enums;

import lombok.Getter;

@Getter
public enum AccountTransactionStatus {
  EXECUTED("EXECUTED"),
  FAILED("FAILED"),
  PENDING("PENDING");

  private final String value;

  AccountTransactionStatus(String val) {
    value = val;
  }
}
