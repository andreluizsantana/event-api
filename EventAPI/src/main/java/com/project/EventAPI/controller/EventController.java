package com.project.EventAPI.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.EventAPI.dto.request.EventRequestDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.dto.update.EventUpdateDTO;
import com.project.EventAPI.service.EventService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventservice) {
    this.eventService = eventservice;
  }

  @GetMapping
  public ResponseEntity<PagedModel<EventResponseDTO>> listarEventos(Pageable pageable) {
    Page<EventResponseDTO> pageventos = eventService.listarEventos(pageable);
    PagedModel<EventResponseDTO> pagemodel = new PagedModel<>(pageventos);
    return ResponseEntity.ok(pagemodel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> buscarPorId(@PathVariable Long id) {
    EventResponseDTO busca = eventService.buscarID(id);
    return ResponseEntity.status(HttpStatus.OK).body(busca);
  }

  @PostMapping
  public ResponseEntity<EventResponseDTO> salvarEvento(@Valid @RequestBody EventRequestDTO dto) {
    EventResponseDTO novoEvento = eventService.salvarEvento(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
  }

  @PostMapping("/lote")
  public ResponseEntity<List<EventResponseDTO>> salvarEventosLote(
      @Valid @RequestBody List<EventRequestDTO> eventosRequestDTO) {
    List<EventResponseDTO> novosEventos = eventService.salvarEventosLote(eventosRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(novosEventos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EventResponseDTO> atualizarEvento(@PathVariable Long id,
      @Valid @RequestBody EventUpdateDTO eventUpdateDTO) {
    EventResponseDTO eventoAtualizado = eventService.atualizarEvento(id, eventUpdateDTO);
    return ResponseEntity.ok(eventoAtualizado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
    eventService.deletarEvento(id);
    return ResponseEntity.noContent().build();
  }

}
