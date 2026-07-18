package com.project.EventAPI.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/event")
public class EventController {

  private final EventService eventservice;

  public EventController(EventService eventservice) {
    super();
    this.eventservice = eventservice;
  }

  @GetMapping
  public ResponseEntity<PagedModel<EventResponseDTO>> listarEventos(Pageable pageable) {
    Page<EventResponseDTO> pageventos = eventservice.listarEventos(pageable);
    PagedModel<EventResponseDTO> pagemodel = new PagedModel<>(pageventos);
    return ResponseEntity.ok(pagemodel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> buscaID(@PathVariable Long id) {
    EventResponseDTO busca = eventservice.buscarID(id);
    return ResponseEntity.status(HttpStatus.OK).body(busca);
  }

  @PostMapping
  public ResponseEntity<String> salvarEvento(@Valid @RequestBody EventRequestDTO eventrequestdto) {
    EventResponseDTO novoevento = eventservice.salvarEvento(eventrequestdto);
    return ResponseEntity.status(HttpStatus.CREATED).body("Evento Criado, ID: " + novoevento.id());
  }

  @PostMapping("/lote")
  public ResponseEntity<List<EventResponseDTO>> salvarEventosLote(
      @Valid @RequestBody List<EventRequestDTO> eventosRequestDTO) {
    List<EventResponseDTO> novosEventos = eventservice.salvarEventosLote(eventosRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(novosEventos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> atualizarEvento(@PathVariable Long id, @RequestBody EventUpdateDTO eventUpdateDTO) {
    EventResponseDTO eventoAtualizado = eventservice.atualizarEvento(id, eventUpdateDTO);
    return ResponseEntity.status(HttpStatus.OK).body("Evento Atualizado, ID: " + eventoAtualizado.id());
  }

}
