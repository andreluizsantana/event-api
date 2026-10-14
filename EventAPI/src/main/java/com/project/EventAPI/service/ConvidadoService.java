package com.project.EventAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@Transactional
public class ConvidadoService {

  private final ConvidadoRepository convidadoRepository;
  private final EventRepository eventRepository;
  private final ConvidadoMapper convidadoMapper;

  public ConvidadoService(ConvidadoRepository convidadoRepository, EventRepository eventRepository,
      ConvidadoMapper convidadoMapper) {
    this.convidadoRepository = convidadoRepository;
    this.eventRepository = eventRepository;
    this.convidadoMapper = convidadoMapper;
  }

  @Transactional(readOnly = true)
  public Page<ConvidadoResponseDTO> listar(Long eventoId, Pageable pageable) {
    garantirEventoExiste(eventoId);
    return convidadoRepository.findByEventoId(eventoId, pageable).map(convidadoMapper::toResponse);
  }

  @Transactional(readOnly = true)
  public ConvidadoResponseDTO buscar(Long eventoId, Long id) {
    return convidadoMapper.toResponse(buscarConvidado(eventoId, id));
  }

  public ConvidadoResponseDTO salvar(Long eventoId, ConvidadoRequestDTO dto) {
    Evento evento = buscarEvento(eventoId);
    Convidado convidado = convidadoMapper.toEntity(dto);
    convidado.setEvento(evento);
    return convidadoMapper.toResponse(convidadoRepository.save(convidado));
  }

  public ConvidadoResponseDTO atualizar(Long eventoId, Long id, ConvidadoUpdateDTO dto) {
    Convidado convidado = buscarConvidado(eventoId, id);
    convidadoMapper.updateEntity(dto, convidado);
    return convidadoMapper.toResponse(convidadoRepository.save(convidado));
  }

  public void deletar(Long eventoId, Long id) {
    convidadoRepository.delete(buscarConvidado(eventoId, id));
  }

  private Evento buscarEvento(Long eventoId) {
    return eventRepository.findById(eventoId).orElseThrow(() -> new EventNotFoundException(eventoId));
  }

  private void garantirEventoExiste(Long eventoId) {
    if (!eventRepository.existsById(eventoId)) {
      throw new EventNotFoundException(eventoId);
    }
  }

  private Convidado buscarConvidado(Long eventoId, Long id) {
    return convidadoRepository.findByIdAndEventoId(id, eventoId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Convidado não encontrado com ID: " + id + " para o evento ID: " + eventoId));
  }
}
