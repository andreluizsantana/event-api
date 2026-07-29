package com.project.EventAPI.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.EventAPI.dto.mapper.EventMapper;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.dto.update.EventUpdateDTO;
import com.project.EventAPI.entity.Endereco;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.entity.EventoAuditoria;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.repository.EventRepository;
import com.project.EventAPI.repository.EventoAuditoriaRepository;

@Service
@Transactional
public class EventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;
  private final EventoAuditoriaRepository auditoriaRepository;

  public EventService(EventRepository eventrepository, EventMapper eventmapper, EventoAuditoriaRepository auditoriarepository) {
    this.eventRepository = eventrepository;
    this.eventMapper = eventmapper;
    this.auditoriaRepository = auditoriarepository;
  }

  public void validaEvento(EventRequestDTO eventRequestDTO) throws DateEventInvalidException {
    if (eventRequestDTO.previsaoInicio().isAfter(eventRequestDTO.previsaoFim())) {
      throw new DateEventInvalidException(
          "Data de início (%s) não pode ser posterior à data de fim (%s) para o evento '%s'".formatted(
              eventRequestDTO.previsaoInicio(), eventRequestDTO.previsaoFim(), eventRequestDTO.descricaoEvento()));
    }
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

  private String enderecoToString(Endereco endereco) {
    if (endereco == null) return null;
    return "%s, %s, %s, %s, %s, %s".formatted(
        endereco.getCep(), endereco.getUf(), endereco.getCidade(),
        endereco.getRua(), endereco.getNumero(), endereco.getReferencia());
  }

  @Transactional
  public EventResponseDTO atualizarEvento(Long id, EventUpdateDTO dto) {
    Evento eventoExistente = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));

    String oldTitulo = eventoExistente.getTitulo();
    String oldDescricao = eventoExistente.getDescricaoEvento();
    LocalDateTime oldPrevisaoInicio = eventoExistente.getPrevisaoInicio();
    LocalDateTime oldPrevisaoFim = eventoExistente.getPrevisaoFim();
    String oldStatus = eventoExistente.getStatus() != null ? eventoExistente.getStatus().name() : null;
    String oldEndereco = enderecoToString(eventoExistente.getEndereco());

    eventMapper.updateEntityFromDto(dto, eventoExistente);

    List<EventoAuditoria> auditorias = new ArrayList<>();
    LocalDateTime agora = LocalDateTime.now();

    if (dto.titulo() != null && !java.util.Objects.equals(oldTitulo, eventoExistente.getTitulo())) {
      auditorias.add(new EventoAuditoria(id, "titulo", oldTitulo, eventoExistente.getTitulo(), agora));
    }
    if (dto.descricaoEvento() != null && !java.util.Objects.equals(oldDescricao, eventoExistente.getDescricaoEvento())) {
      auditorias.add(new EventoAuditoria(id, "descricaoEvento", oldDescricao, eventoExistente.getDescricaoEvento(), agora));
    }
    if (dto.previsaoInicio() != null && !java.util.Objects.equals(oldPrevisaoInicio, eventoExistente.getPrevisaoInicio())) {
      auditorias.add(new EventoAuditoria(id, "previsaoInicio",
          oldPrevisaoInicio != null ? oldPrevisaoInicio.toString() : null,
          eventoExistente.getPrevisaoInicio() != null ? eventoExistente.getPrevisaoInicio().toString() : null, agora));
    }
    if (dto.previsaoFim() != null && !java.util.Objects.equals(oldPrevisaoFim, eventoExistente.getPrevisaoFim())) {
      auditorias.add(new EventoAuditoria(id, "previsaoFim",
          oldPrevisaoFim != null ? oldPrevisaoFim.toString() : null,
          eventoExistente.getPrevisaoFim() != null ? eventoExistente.getPrevisaoFim().toString() : null, agora));
    }
    if (dto.status() != null && !java.util.Objects.equals(oldStatus, eventoExistente.getStatus().name())) {
      auditorias.add(new EventoAuditoria(id, "status", oldStatus, eventoExistente.getStatus().name(), agora));
    }
    if (dto.endereco() != null && !java.util.Objects.equals(oldEndereco, enderecoToString(eventoExistente.getEndereco()))) {
      auditorias.add(new EventoAuditoria(id, "endereco", oldEndereco, enderecoToString(eventoExistente.getEndereco()), agora));
    }

    if (!auditorias.isEmpty()) {
      auditoriaRepository.saveAll(auditorias);
    }

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
