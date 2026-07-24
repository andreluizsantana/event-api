package com.project.EventAPI.exception;

public class EventNotFoundException extends RuntimeException {

  public EventNotFoundException(Long id) {
    super("Evento não encontrado com ID: " + id);
  }

  public EventNotFoundException(String message) {
    super(message);
  }
}
