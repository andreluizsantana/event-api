package com.project.EventAPI.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventGlobalExceptionHandler {

  /*
   * @ExceptionHandler(EventNotFoundException.class) public ResponseEntity<ErrorResponse>
   * handleEventNotFound(EventNotFoundException e) { ErrorResponse erro = new
   * ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()); return
   * ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro); }
   */
}
