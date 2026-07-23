package com.project.EventAPI.exception;

public class EventoNaoEncontradoException extends RuntimeException {
  public EventoNaoEncontradoException(Long id) {
    super("Evento não encontrado com ID: " + id);
  }
}
