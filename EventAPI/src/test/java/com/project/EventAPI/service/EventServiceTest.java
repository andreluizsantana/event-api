package com.project.EventAPI.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.project.EventAPI.dto.mapper.EventMapper;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.dto.update.EventUpdateDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.enums.Status;
import com.project.EventAPI.exception.DateEventInvalidException;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private EventMapper eventMapper;

  @InjectMocks
  private EventService eventService;

  private EventRequestDTO requestValido() {
    return new EventRequestDTO("Festa", "Descrição", LocalDateTime.now(),
        LocalDateTime.now().plusDays(1), Status.PENDENTE, null);
  }

  private Evento evento() {
    Evento evento = new Evento();
    evento.setTitulo("Festa");
    evento.setDescricaoEvento("Descrição");
    evento.setPrevisaoInicio(LocalDateTime.now());
    evento.setPrevisaoFim(LocalDateTime.now().plusDays(1));
    evento.setStatus(Status.PENDENTE);
    return evento;
  }

  @Test
  void deveListarEventosPaginados() {
    Pageable pageable = PageRequest.of(0, 10);
    Evento evento = evento();
    EventResponseDTO response = new EventResponseDTO(1L, "Festa", "Descrição", evento.getPrevisaoInicio(),
        evento.getPrevisaoFim(), Status.PENDENTE, null);
    when(eventRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(evento)));
    when(eventMapper.entityToDto(evento)).thenReturn(response);

    Page<EventResponseDTO> resultado = eventService.listarEventos(pageable);

    assertThat(resultado.getContent()).containsExactly(response);
  }

  @Test
  void deveBuscarEventoPorId() {
    Evento evento = evento();
    EventResponseDTO response = new EventResponseDTO(1L, "Festa", "Descrição", null, null, Status.PENDENTE, null);
    when(eventRepository.findById(1L)).thenReturn(Optional.of(evento));
    when(eventMapper.entityToDto(evento)).thenReturn(response);

    assertThat(eventService.buscarID(1L)).isEqualTo(response);
  }

  @Test
  void deveLancarExcecaoQuandoEventoNaoEncontrado() {
    when(eventRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> eventService.buscarID(99L))
        .isInstanceOf(EventNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void deveSalvarEventoValido() {
    EventRequestDTO dto = requestValido();
    Evento evento = evento();
    EventResponseDTO response = new EventResponseDTO(1L, "Festa", "Descrição", null, null, Status.PENDENTE, null);
    when(eventMapper.dtoToEntity(dto)).thenReturn(evento);
    when(eventRepository.save(evento)).thenReturn(evento);
    when(eventMapper.entityToDto(evento)).thenReturn(response);

    assertThat(eventService.salvarEvento(dto)).isEqualTo(response);
    verify(eventRepository).save(evento);
  }

  @Test
  void naoDeveSalvarEventoComDataInvalida() {
    EventRequestDTO dto = new EventRequestDTO("Festa", "Descrição", LocalDateTime.now().plusDays(2),
        LocalDateTime.now(), Status.PENDENTE, null);

    assertThatThrownBy(() -> eventService.salvarEvento(dto))
        .isInstanceOf(DateEventInvalidException.class);

    verify(eventRepository, never()).save(any());
  }

  @Test
  void naoDeveSalvarLoteComEventoInvalido() {
    EventRequestDTO invalido = new EventRequestDTO("Festa", "Descrição", LocalDateTime.now().plusDays(2),
        LocalDateTime.now(), Status.PENDENTE, null);

    assertThatThrownBy(() -> eventService.salvarEventosLote(List.of(invalido)))
        .isInstanceOf(DateEventInvalidException.class);

    verify(eventRepository, never()).saveAll(anyList());
  }

  @Test
  void deveAtualizarEventoExistente() {
    Evento existente = evento();
    EventUpdateDTO dto = new EventUpdateDTO("Novo título", null, null, null, null, null);
    EventResponseDTO response = new EventResponseDTO(1L, "Novo título", "Descrição", null, null, Status.PENDENTE, null);
    when(eventRepository.findById(1L)).thenReturn(Optional.of(existente));
    when(eventRepository.save(existente)).thenReturn(existente);
    when(eventMapper.entityToDto(existente)).thenReturn(response);

    EventResponseDTO resultado = eventService.atualizarEvento(1L, dto);

    assertThat(resultado).isEqualTo(response);
    verify(eventMapper).updateEntityFromDto(dto, existente);
  }

  @Test
  void deveLancarExcecaoAoAtualizarEventoInexistente() {
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> eventService.atualizarEvento(1L, new EventUpdateDTO(null, null, null, null, null, null)))
        .isInstanceOf(EventNotFoundException.class);
  }

  @Test
  void deveDeletarEventoExistente() {
    when(eventRepository.existsById(1L)).thenReturn(true);

    eventService.deletarEvento(1L);

    verify(eventRepository).deleteById(1L);
  }

  @Test
  void naoDeveDeletarEventoInexistente() {
    when(eventRepository.existsById(1L)).thenReturn(false);

    assertThatThrownBy(() -> eventService.deletarEvento(1L))
        .isInstanceOf(EventNotFoundException.class);

    verify(eventRepository, never()).deleteById(1L);
  }
}
