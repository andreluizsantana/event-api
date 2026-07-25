package com.project.EventAPI.service;

public class DateEventInvalidException extends Exception {
  
      public DateEventInvalidException(String message) {
          super(message);
      }
      
      public DateEventInvalidException(String message, Throwable cause) {
          super(message, cause);
      }

}
