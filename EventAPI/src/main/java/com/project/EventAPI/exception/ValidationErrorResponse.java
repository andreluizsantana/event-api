package com.project.EventAPI.exception;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ValidationErrorResponse(String mensagem, int status, LocalDateTime timestamp, Map<String, String> erros) {
  public ValidationErrorResponse(Map<String, String> erros) {
    this("Erro de validação", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), erros);
  }
}
