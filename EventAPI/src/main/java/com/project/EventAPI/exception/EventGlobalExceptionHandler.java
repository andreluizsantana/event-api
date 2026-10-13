package com.project.EventAPI.exception;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventGlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(EventGlobalExceptionHandler.class);

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEventNotFound(EventNotFoundException e) {
    ErrorResponse erro = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException e) {
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

  @ExceptionHandler(DateEventInvalidException.class)
  public ResponseEntity<ErrorResponse> handleDateEventInvalid(DateEventInvalidException e) {
    ErrorResponse erro = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
    log.warn("Data integrity violation: {}", e.getMostSpecificCause().getMessage());
    ErrorResponse erro = new ErrorResponse("Violação de integridade de dados. Verifique campos únicos ou obrigatórios.",
        HttpStatus.CONFLICT.value());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
    log.error("Unhandled exception", e);
    ErrorResponse erro = new ErrorResponse("Erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
  }
}
