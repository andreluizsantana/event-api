package com.project.EventAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.EventAPI.dto.mapper.EventMapper;
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

  // listar
  @Transactional
  public Page<EventResponseDTO> listarEventos(Pageable pageable) {
    Page<Evento> listagemeventos = eventrepository.findAll(pageable);
    return listagemeventos.map(eventmapper::entityToDto);
  }


}
