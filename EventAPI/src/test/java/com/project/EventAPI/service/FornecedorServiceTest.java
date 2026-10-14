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
import com.project.EventAPI.dto.mapper.FornecedorMapper;
import com.project.EventAPI.dto.request.FornecedorRequestDTO;
import com.project.EventAPI.dto.response.FornecedorResponseDTO;
import com.project.EventAPI.dto.update.FornecedorUpdateDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.entity.Fornecedor;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.exception.ResourceNotFoundException;
import com.project.EventAPI.repository.EventRepository;
import com.project.EventAPI.repository.FornecedorRepository;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

  @Mock
  private FornecedorRepository fornecedorRepository;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private FornecedorMapper fornecedorMapper;

  @InjectMocks
  private FornecedorService fornecedorService;

  private final Evento evento = new Evento();
  private final Fornecedor fornecedor = new Fornecedor();

  @Test
  void deveSalvarFornecedorVinculadoAoEvento() {
    FornecedorRequestDTO dto = new FornecedorRequestDTO("Buffet", "Catering", new BigDecimal("1000"));
    FornecedorResponseDTO response = new FornecedorResponseDTO(1L, "Buffet", "Catering", new BigDecimal("1000"), 1L);
    when(eventRepository.findById(1L)).thenReturn(Optional.of(evento));
    when(fornecedorMapper.toEntity(dto)).thenReturn(fornecedor);
    when(fornecedorRepository.save(fornecedor)).thenReturn(fornecedor);
    when(fornecedorMapper.toResponse(fornecedor)).thenReturn(response);

    assertThat(fornecedorService.salvar(1L, dto)).isEqualTo(response);
    assertThat(fornecedor.getEvento()).isEqualTo(evento);
  }

  @Test
  void deveFalharAoSalvarEmEventoInexistente() {
    when(eventRepository.findById(9L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> fornecedorService.salvar(9L, new FornecedorRequestDTO("X", null, null)))
        .isInstanceOf(EventNotFoundException.class);
  }

  @Test
  void deveAtualizarFornecedor() {
    FornecedorUpdateDTO dto = new FornecedorUpdateDTO("Buffet Novo", null, null);
    FornecedorResponseDTO response = new FornecedorResponseDTO(1L, "Buffet Novo", null, null, 1L);
    when(fornecedorRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(fornecedor));
    when(fornecedorRepository.save(fornecedor)).thenReturn(fornecedor);
    when(fornecedorMapper.toResponse(fornecedor)).thenReturn(response);

    assertThat(fornecedorService.atualizar(1L, 1L, dto)).isEqualTo(response);
    verify(fornecedorMapper).updateEntity(dto, fornecedor);
  }

  @Test
  void deveFalharQuandoFornecedorNaoPertenceAoEvento() {
    when(fornecedorRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> fornecedorService.buscar(1L, 1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deveDeletarFornecedor() {
    when(fornecedorRepository.findByIdAndEventoId(1L, 1L)).thenReturn(Optional.of(fornecedor));

    fornecedorService.deletar(1L, 1L);

    verify(fornecedorRepository).delete(fornecedor);
  }
}
