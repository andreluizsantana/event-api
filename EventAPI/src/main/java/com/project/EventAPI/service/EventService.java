package com.project.EventAPI.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.EventAPI.dto.mapper.EventMapper;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.dto.update.EventUpdateDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.exception.DateEventInvalidException;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.exception.EventHasDependenciesException;
import com.project.EventAPI.repository.ConvidadoRepository;
import com.project.EventAPI.repository.FornecedorRepository;
import com.project.EventAPI.repository.ItemEventoRepository;
import com.project.EventAPI.repository.EventRepository;

@Service
@Transactional
public class EventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;
  private final ConvidadoRepository convidadoRepository;
  private final FornecedorRepository fornecedorRepository;
  private final ItemEventoRepository itemEventoRepository;

  public EventService(EventRepository eventRepository, EventMapper eventMapper,
      ConvidadoRepository convidadoRepository, FornecedorRepository fornecedorRepository,
      ItemEventoRepository itemEventoRepository) {
    this.eventRepository = eventRepository;
    this.eventMapper = eventMapper;
    this.convidadoRepository = convidadoRepository;
    this.fornecedorRepository = fornecedorRepository;
    this.itemEventoRepository = itemEventoRepository;
  }

  public void validaEvento(EventRequestDTO eventRequestDTO) {
    validaPeriodo(eventRequestDTO.previsaoInicio(), eventRequestDTO.previsaoFim(),
        eventRequestDTO.descricaoEvento());
  }

  private void validaPeriodo(LocalDateTime inicio, LocalDateTime fim, String descricaoEvento) {
    if (inicio != null && fim != null && inicio.isAfter(fim)) {
      throw new DateEventInvalidException(
          "Data de início (%s) não pode ser posterior à data de fim (%s) para o evento '%s'".formatted(
              inicio, fim, descricaoEvento));
    }
  }

  @Transactional(readOnly = true)
  public Page<EventResponseDTO> listarEventos(Pageable pageable) {
    Page<Evento> eventos = eventRepository.findAll(pageable);
    return eventos.map(eventMapper::entityToDto);
  }

  @Transactional(readOnly = true)
  public EventResponseDTO buscarID(Long id) {
    Evento evento = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    return eventMapper.entityToDto(evento);
  }

  public EventResponseDTO salvarEvento(EventRequestDTO dto) {
    validaEvento(dto);
    Evento evento = eventMapper.dtoToEntity(dto);
    Evento eventoSalvo = eventRepository.save(evento);
    return eventMapper.entityToDto(eventoSalvo);
  }

  @Transactional
  public EventResponseDTO atualizarEvento(Long id, EventUpdateDTO dto) {
    Evento eventoExistente = eventRepository.findById(id)
        .orElseThrow(() -> new EventNotFoundException(id));
    eventMapper.updateEntityFromDto(dto, eventoExistente);
    validaPeriodo(eventoExistente.getPrevisaoInicio(), eventoExistente.getPrevisaoFim(),
        eventoExistente.getDescricaoEvento());
    Evento eventoSalvo = eventRepository.save(eventoExistente);
    return eventMapper.entityToDto(eventoSalvo);
  }

  public List<EventResponseDTO> salvarEventosLote(List<EventRequestDTO> eventosDto) {
    eventosDto.forEach(this::validaEvento);
    List<Evento> eventos = eventosDto.stream().map(eventMapper::dtoToEntity).toList();
    List<Evento> eventosSalvos = eventRepository.saveAll(eventos);
    return eventosSalvos.stream().map(eventMapper::entityToDto).toList();
  }

  public void deletarEvento(Long id) {
    if (!eventRepository.existsById(id)) {
      throw new EventNotFoundException(id);
    }
    if (convidadoRepository.existsByEventoId(id)
        || fornecedorRepository.existsByEventoId(id)
        || itemEventoRepository.existsByEventoId(id)) {
      throw new EventHasDependenciesException(id);
    }
    eventRepository.deleteById(id);
  }
}
