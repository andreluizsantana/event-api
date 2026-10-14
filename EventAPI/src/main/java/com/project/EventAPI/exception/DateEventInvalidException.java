package com.project.EventAPI.exception;

public class DateEventInvalidException extends RuntimeException {

  public DateEventInvalidException(String message) {
    super(message);
  }

  public DateEventInvalidException(String message, Throwable cause) {
    super(message, cause);
  }
}
