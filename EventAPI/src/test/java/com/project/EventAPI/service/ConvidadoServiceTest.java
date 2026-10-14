package com.project.EventAPI.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.project.EventAPI.dto.mapper.ConvidadoMapper;
import com.project.EventAPI.dto.request.ConvidadoRequestDTO;
import com.project.EventAPI.dto.response.ConvidadoResponseDTO;
import com.project.EventAPI.dto.update.ConvidadoUpdateDTO;
import com.project.EventAPI.entity.Convidado;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.exception.ResourceNotFoundException;
import com.project.EventAPI.repository.ConvidadoRepository;
import com.project.EventAPI.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class ConvidadoServiceTest {

  @Mock
  private ConvidadoRepository convidadoRepository;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private ConvidadoMapper convidadoMapper;

  @InjectMocks
  private ConvidadoService convidadoService;

  private final Evento evento = new Evento();
  private final Convidado convidado = new Convidado();

  @Test
  void deveListarConvidadosDoEvento() {
    Pageable pageable = PageRequest.of(0, 10);
    ConvidadoResponseDTO response = new ConvidadoResponseDTO(1L, "Ana", null, null, false, 1L);
    when(eventRepository.existsById(1L)).thenReturn(true);
    when(convidadoRepository.findByEventoId(1L, pageable)).thenReturn(new PageImpl<>(List.of(convidado)));
    when(convidadoMapper.toResponse(convidado)).thenReturn(response);

    assertThat(convidadoService.listar(1L, pageable).getContent()).containsExactly(response);
  }

  @Test
  void deveFalharAoListarDeEventoInexistente() {
    when(eventRepository.existsById(9L)).thenReturn(false);

    assertThatThrownBy(() -> convidadoService.listar(9L, PageRequest.of(0, 10)))
        .isInstanceOf(EventNotFoundException.class);
  }

  @Test
  void deveBuscarConvidado() {
    ConvidadoResponseDTO response = new ConvidadoResponseDTO(1L, "Ana", null, null, false, 1L);
    when(convidadoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(convidado));
    when(convidadoMapper.toResponse(convidado)).thenReturn(response);

    assertThat(convidadoService.buscar(1L, 1L)).isEqualTo(response);
  }

  @Test
  void deveFalharQuandoConvidadoNaoPertenceAoEvento() {
    when(convidadoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> convidadoService.buscar(1L, 1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deveSalvarConvidadoVinculadoAoEvento() {
    ConvidadoRequestDTO dto = new ConvidadoRequestDTO("Ana", null, null, null);
    ConvidadoResponseDTO response = new ConvidadoResponseDTO(1L, "Ana", null, null, false, 1L);
    when(eventRepository.findById(1L)).thenReturn(Optional.of(evento));
    when(convidadoMapper.toEntity(dto)).thenReturn(convidado);
    when(convidadoRepository.save(convidado)).thenReturn(convidado);
    when(convidadoMapper.toResponse(convidado)).thenReturn(response);

    assertThat(convidadoService.salvar(1L, dto)).isEqualTo(response);
    assertThat(convidado.getEvento()).isEqualTo(evento);
  }

  @Test
  void deveAtualizarConvidado() {
    ConvidadoUpdateDTO dto = new ConvidadoUpdateDTO("Ana Maria", null, null, null);
    ConvidadoResponseDTO response = new ConvidadoResponseDTO(1L, "Ana Maria", null, null, false, 1L);
    when(convidadoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(convidado));
    when(convidadoRepository.save(convidado)).thenReturn(convidado);
    when(convidadoMapper.toResponse(convidado)).thenReturn(response);

    assertThat(convidadoService.atualizar(1L, 1L, dto)).isEqualTo(response);
    verify(convidadoMapper).updateEntity(dto, convidado);
  }

  @Test
  void deveDeletarConvidado() {
    when(convidadoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(convidado));

    convidadoService.deletar(1L, 1L);

    verify(convidadoRepository).delete(convidado);
  }
}
