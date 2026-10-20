package com.project.EventAPI.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

class EventGlobalExceptionHandlerTest {

  private final EventGlobalExceptionHandler handler = new EventGlobalExceptionHandler();

  @Test
  void deveTratarEventoNaoEncontrado() {
    ResponseEntity<ErrorResponse> resposta = handler.handleEventNotFound(new EventNotFoundException(1L));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(resposta.getBody().mensagem()).contains("1");
  }

  @Test
  void deveTratarDataInvalida() {
    ResponseEntity<ErrorResponse> resposta =
        handler.handleDateEventInvalid(new DateEventInvalidException("data inválida"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(resposta.getBody().mensagem()).isEqualTo("data inválida");
  }

  @Test
  void deveTratarIllegalArgument() {
    ResponseEntity<ErrorResponse> resposta =
        handler.handleIllegalArgument(new IllegalArgumentException("argumento"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void deveTratarViolacaoDeIntegridade() {
    ResponseEntity<ErrorResponse> resposta =
        handler.handleDataIntegrityViolation(new DataIntegrityViolationException("duplicado"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void deveTratarEventoComDependentesComoConflito() {
    ResponseEntity<ErrorResponse> resposta = handler.handleConflict(new EventHasDependenciesException(1L));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(resposta.getBody().mensagem()).contains("convidados");
  }

  @Test
  void deveTratarConcorrenciaOtimistaComoConflito() {
    ResponseEntity<ErrorResponse> resposta = handler.handleConflict(
        new ObjectOptimisticLockingFailureException("Evento", 1L));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(resposta.getBody().mensagem()).contains("outra requisição");
  }

  @Test
  void naoDeveExporDetalhesNaExcecaoGenerica() {
    ResponseEntity<ErrorResponse> resposta =
        handler.handleGenericException(new RuntimeException("stacktrace sensível"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(resposta.getBody().mensagem()).doesNotContain("stacktrace sensível");
  }
}
