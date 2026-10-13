package com.project.EventAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@Transactional
public class FornecedorService {

  private final FornecedorRepository fornecedorRepository;
  private final EventRepository eventRepository;
  private final FornecedorMapper fornecedorMapper;

  public FornecedorService(FornecedorRepository fornecedorRepository, EventRepository eventRepository,
      FornecedorMapper fornecedorMapper) {
    this.fornecedorRepository = fornecedorRepository;
    this.eventRepository = eventRepository;
    this.fornecedorMapper = fornecedorMapper;
  }

  @Transactional(readOnly = true)
  public Page<FornecedorResponseDTO> listar(Long eventoId, Pageable pageable) {
    garantirEventoExiste(eventoId);
    return fornecedorRepository.findByEventoId(eventoId, pageable).map(fornecedorMapper::toResponse);
  }

  @Transactional(readOnly = true)
  public FornecedorResponseDTO buscar(Long eventoId, Long id) {
    return fornecedorMapper.toResponse(buscarFornecedor(eventoId, id));
  }

  public FornecedorResponseDTO salvar(Long eventoId, FornecedorRequestDTO dto) {
    Evento evento = buscarEvento(eventoId);
    Fornecedor fornecedor = fornecedorMapper.toEntity(dto);
    fornecedor.setEvento(evento);
    return fornecedorMapper.toResponse(fornecedorRepository.save(fornecedor));
  }

  public FornecedorResponseDTO atualizar(Long eventoId, Long id, FornecedorUpdateDTO dto) {
    Fornecedor fornecedor = buscarFornecedor(eventoId, id);
    fornecedorMapper.updateEntity(dto, fornecedor);
    return fornecedorMapper.toResponse(fornecedorRepository.save(fornecedor));
  }

  public void deletar(Long eventoId, Long id) {
    fornecedorRepository.delete(buscarFornecedor(eventoId, id));
  }

  private Evento buscarEvento(Long eventoId) {
    return eventRepository.findById(eventoId).orElseThrow(() -> new EventNotFoundException(eventoId));
  }

  private void garantirEventoExiste(Long eventoId) {
    if (!eventRepository.existsById(eventoId)) {
      throw new EventNotFoundException(eventoId);
    }
  }

  private Fornecedor buscarFornecedor(Long eventoId, Long id) {
    return fornecedorRepository.findByIdAndEventoId(id, eventoId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Fornecedor não encontrado com ID: " + id + " para o evento ID: " + eventoId));
  }
}
