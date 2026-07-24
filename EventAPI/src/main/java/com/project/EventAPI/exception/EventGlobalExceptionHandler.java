package com.project.EventAPI.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventGlobalExceptionHandler {

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEventNotFound(EventNotFoundException e) {
    ErrorResponse erro = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException e) {
    Map<String, String> erros = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(fieldError -> erros.put(fieldError.getField(), fieldError.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(erros));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
    ErrorResponse erro = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
    ErrorResponse erro =
        new ErrorResponse("Erro interno no servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
    ErrorResponse erro = new ErrorResponse("Violação de integridade de dados. Verifique campos únicos ou obrigatórios.",
        HttpStatus.CONFLICT.value());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
  }
}
