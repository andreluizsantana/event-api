package com.project.EventAPI.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.EventAPI.dto.mapper.ItemEventoMapper;
import com.project.EventAPI.dto.request.ItemEventoRequestDTO;
import com.project.EventAPI.dto.response.ItemEventoResponseDTO;
import com.project.EventAPI.dto.update.ItemEventoUpdateDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.entity.ItemEvento;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.exception.ResourceNotFoundException;
import com.project.EventAPI.repository.EventRepository;
import com.project.EventAPI.repository.ItemEventoRepository;

@ExtendWith(MockitoExtension.class)
class ItemEventoServiceTest {

  @Mock
  private ItemEventoRepository itemEventoRepository;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private ItemEventoMapper itemEventoMapper;

  @InjectMocks
  private ItemEventoService itemEventoService;

  private final Evento evento = new Evento();
  private final ItemEvento item = new ItemEvento();

  @Test
  void deveSalvarItemVinculadoAoEvento() {
    ItemEventoRequestDTO dto = new ItemEventoRequestDTO("Som", new BigDecimal("500"));
    ItemEventoResponseDTO response = new ItemEventoResponseDTO(1L, "Som", new BigDecimal("500"), 1L);
    when(eventRepository.findById(1L)).thenReturn(Optional.of(evento));
    when(itemEventoMapper.toEntity(dto)).thenReturn(item);
    when(itemEventoRepository.save(item)).thenReturn(item);
    when(itemEventoMapper.toResponse(item)).thenReturn(response);

    assertThat(itemEventoService.salvar(1L, dto)).isEqualTo(response);
    assertThat(item.getEvento()).isEqualTo(evento);
  }

  @Test
  void deveFalharAoSalvarEmEventoInexistente() {
    when(eventRepository.findById(9L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> itemEventoService.salvar(9L, new ItemEventoRequestDTO("Som", null)))
        .isInstanceOf(EventNotFoundException.class);
  }

  @Test
  void deveAtualizarItem() {
    ItemEventoUpdateDTO dto = new ItemEventoUpdateDTO("Som e Luz", null);
    ItemEventoResponseDTO response = new ItemEventoResponseDTO(1L, "Som e Luz", null, 1L);
    when(itemEventoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(item));
    when(itemEventoRepository.save(item)).thenReturn(item);
    when(itemEventoMapper.toResponse(item)).thenReturn(response);

    assertThat(itemEventoService.atualizar(1L, 1L, dto)).isEqualTo(response);
    verify(itemEventoMapper).updateEntity(dto, item);
  }

  @Test
  void deveFalharQuandoItemNaoPertenceAoEvento() {
    when(itemEventoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> itemEventoService.buscar(1L, 1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deveDeletarItem() {
    when(itemEventoRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(item));

    itemEventoService.deletar(1L, 1L);

    verify(itemEventoRepository).delete(item);
  }
}
