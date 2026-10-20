package com.project.EventAPI.exception;

public class EventHasDependenciesException extends RuntimeException {

  public EventHasDependenciesException(Long id) {
    super("Não é possível excluir o evento " + id + " enquanto houver convidados, fornecedores ou itens vinculados.");
  }
}
