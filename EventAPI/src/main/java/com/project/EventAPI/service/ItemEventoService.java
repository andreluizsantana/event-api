package com.project.EventAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@Transactional
public class ItemEventoService {

  private final ItemEventoRepository itemEventoRepository;
  private final EventRepository eventRepository;
  private final ItemEventoMapper itemEventoMapper;

  public ItemEventoService(ItemEventoRepository itemEventoRepository, EventRepository eventRepository,
      ItemEventoMapper itemEventoMapper) {
    this.itemEventoRepository = itemEventoRepository;
    this.eventRepository = eventRepository;
    this.itemEventoMapper = itemEventoMapper;
  }

  @Transactional(readOnly = true)
  public Page<ItemEventoResponseDTO> listar(Long eventoId, Pageable pageable) {
    garantirEventoExiste(eventoId);
    return itemEventoRepository.findByEventoId(eventoId, pageable).map(itemEventoMapper::toResponse);
  }

  @Transactional(readOnly = true)
  public ItemEventoResponseDTO buscar(Long eventoId, Long id) {
    return itemEventoMapper.toResponse(buscarItem(eventoId, id));
  }

  public ItemEventoResponseDTO salvar(Long eventoId, ItemEventoRequestDTO dto) {
    Evento evento = buscarEvento(eventoId);
    ItemEvento itemEvento = itemEventoMapper.toEntity(dto);
    itemEvento.setEvento(evento);
    return itemEventoMapper.toResponse(itemEventoRepository.save(itemEvento));
  }

  public ItemEventoResponseDTO atualizar(Long eventoId, Long id, ItemEventoUpdateDTO dto) {
    ItemEvento itemEvento = buscarItem(eventoId, id);
    itemEventoMapper.updateEntity(dto, itemEvento);
    return itemEventoMapper.toResponse(itemEventoRepository.save(itemEvento));
  }

  public void deletar(Long eventoId, Long id) {
    itemEventoRepository.delete(buscarItem(eventoId, id));
  }

  private Evento buscarEvento(Long eventoId) {
    return eventRepository.findById(eventoId).orElseThrow(() -> new EventNotFoundException(eventoId));
  }

  private void garantirEventoExiste(Long eventoId) {
    if (!eventRepository.existsById(eventoId)) {
      throw new EventNotFoundException(eventoId);
    }
  }

  private ItemEvento buscarItem(Long eventoId, Long id) {
    return itemEventoRepository.findByIdAndEventoId(id, eventoId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Item não encontrado com ID: " + id + " para o evento ID: " + eventoId));
  }
}
