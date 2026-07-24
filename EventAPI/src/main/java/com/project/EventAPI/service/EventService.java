package com.project.EventAPI.service;

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
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.repository.EventRepository;

@Service
@Transactional
public class EventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  public EventService(EventRepository eventrepository, EventMapper eventmapper) {
    this.eventRepository = eventrepository;
    this.eventMapper = eventmapper;
  }

  @Transactional(readOnly = true)
  public Page<EventResponseDTO> listarEventos(Pageable pageable) {
    Page<Evento> listagemeventos = eventRepository.findAll(pageable);
    return listagemeventos.map(eventMapper::entityToDto);
  }

  @Transactional(readOnly = true)
  public EventResponseDTO buscarID(Long id) {
    Evento localizaID = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    return eventMapper.entityToDto(localizaID);
  }

  public EventResponseDTO salvarEvento(EventRequestDTO eventrequestdto) {
    Evento evento = eventMapper.dtoToEntity(eventrequestdto);
    Evento eventoSalvo = eventRepository.save(evento);
    return eventMapper.entityToDto(eventoSalvo);
  }

  @Transactional
  public EventResponseDTO atualizarEvento(Long id, EventUpdateDTO dto) {
    Evento eventoExistente = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    eventMapper.updateEntityFromDto(dto, eventoExistente);
    Evento eventoSalvo = eventRepository.save(eventoExistente);
    return eventMapper.entityToDto(eventoSalvo);
  }

  public List<EventResponseDTO> salvarEventosLote(List<EventRequestDTO> eventosRequestDTO) {
    List<Evento> eventos = eventosRequestDTO.stream().map(dto -> eventMapper.dtoToEntity(dto)).toList();
    List<Evento> eventosSalvos = eventRepository.saveAll(eventos);
    return eventosSalvos.stream().map(evento -> eventMapper.entityToDto(evento)).toList();
  }

  public void deletarEvento(Long id) {
    if (!eventRepository.existsById(id)) {
      throw new EventNotFoundException(id);
    }
    eventRepository.deleteById(id);
  }
}
