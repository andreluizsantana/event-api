package com.project.EventAPI.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import com.project.EventAPI.dto.request.ConvidadoRequestDTO;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.ConvidadoResponseDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.enums.Status;
import com.project.EventAPI.exception.DateEventInvalidException;
import com.project.EventAPI.support.AbstractIntegrationTest;

class EventCrudIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private EventService eventService;

  @Autowired
  private ConvidadoService convidadoService;

  private EventResponseDTO criarEvento() {
    EventRequestDTO dto = new EventRequestDTO("Casamento", "Casamento da Ana", LocalDateTime.now(),
        LocalDateTime.now().plusDays(30), Status.AGENDADO, null);
    return eventService.salvarEvento(dto);
  }

  @Test
  void deveCriarEAtualizarEvento() {
    EventResponseDTO criado = criarEvento();
    assertThat(criado.id()).isNotNull();

    var atualizado = eventService.buscarID(criado.id());
    assertThat(atualizado.titulo()).isEqualTo("Casamento");
    assertThat(atualizado.status()).isEqualTo(Status.AGENDADO);
  }

  @Test
  void deveRejeitarEventoComDatasInvertidas() {
    EventRequestDTO dto = new EventRequestDTO("Inválido", "Desc", LocalDateTime.now().plusDays(5),
        LocalDateTime.now(), Status.PENDENTE, null);

    assertThatThrownBy(() -> eventService.salvarEvento(dto))
        .isInstanceOf(DateEventInvalidException.class);
  }

  @Test
  void deveGerenciarConvidadosDoEvento() {
    Long eventoId = criarEvento().id();

    ConvidadoResponseDTO convidado = convidadoService.salvar(eventoId,
        new ConvidadoRequestDTO("Ana", "ana@teste.com", "11999999999", null));

    assertThat(convidado.id()).isNotNull();
    assertThat(convidado.eventoId()).isEqualTo(eventoId);
    assertThat(convidado.confirmado()).isFalse();

    var pagina = convidadoService.listar(eventoId, PageRequest.of(0, 10));
    assertThat(pagina.getContent()).extracting(ConvidadoResponseDTO::nome).contains("Ana");
  }
}
