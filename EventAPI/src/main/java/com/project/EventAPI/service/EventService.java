package com.project.EventAPI.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.EventAPI.dto.mapper.EventMapper;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.repository.EventRepository;

@Service
@Transactional
public class EventService {

  private final EventRepository eventrepository;
  private final EventMapper eventmapper;

  public EventService(EventRepository eventrepository, EventMapper eventmapper) {
    super();
    this.eventrepository = eventrepository;
    this.eventmapper = eventmapper;
  }

  @Transactional
  public Page<EventResponseDTO> listarEventos(Pageable pageable) {
    Page<Evento> listagemeventos = eventrepository.findAll(pageable);
    return listagemeventos.map(eventmapper::entityToDto);
  }

  @Transactional
  public EventResponseDTO salvarEvento(EventRequestDTO eventrequestdto) {
    Evento evento;
    evento = eventmapper.dtoToEntity(eventrequestdto);
    Evento eventoSalvo = eventrepository.save(evento);
    return eventmapper.entityToDto(eventoSalvo);
  }

  @Transactional
  public List<EventResponseDTO> salvarEventosLote(List<EventRequestDTO> eventosRequestDTO) {
    List<Evento> eventos = eventosRequestDTO.stream().map(dto -> eventmapper.dtoToEntity(dto)).toList();
    List<Evento> eventosSalvos = eventrepository.saveAll(eventos);
    return eventosSalvos.stream().map(evento -> eventmapper.entityToDto(evento)).toList();
  }

}
