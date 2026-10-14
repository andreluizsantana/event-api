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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "CRUD de eventos")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping
  @Operation(summary = "Lista eventos de forma paginada")
  public ResponseEntity<PagedModel<EventResponseDTO>> listarEventos(Pageable pageable) {
    Page<EventResponseDTO> eventos = eventService.listarEventos(pageable);
    return ResponseEntity.ok(new PagedModel<>(eventos));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Busca um evento por ID")
  @ApiResponse(responseCode = "404", description = "Evento não encontrado")
  public ResponseEntity<EventResponseDTO> buscarPorId(@PathVariable Long id) {
    return ResponseEntity.ok(eventService.buscarID(id));
  }

  @PostMapping
  @Operation(summary = "Cria um novo evento")
  @ApiResponse(responseCode = "201", description = "Evento criado")
  public ResponseEntity<EventResponseDTO> salvarEvento(@Valid @RequestBody EventRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(eventService.salvarEvento(dto));
  }

  @PostMapping("/lote")
  @Operation(summary = "Cria eventos em lote")
  @ApiResponse(responseCode = "201", description = "Eventos criados")
  public ResponseEntity<List<EventResponseDTO>> salvarEventosLote(
      @Valid @RequestBody List<EventRequestDTO> eventosRequestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(eventService.salvarEventosLote(eventosRequestDTO));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualiza um evento existente")
  @ApiResponse(responseCode = "404", description = "Evento não encontrado")
  public ResponseEntity<EventResponseDTO> atualizarEvento(@PathVariable Long id,
      @Valid @RequestBody EventUpdateDTO eventUpdateDTO) {
    return ResponseEntity.ok(eventService.atualizarEvento(id, eventUpdateDTO));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Remove um evento")
  @ApiResponse(responseCode = "204", description = "Evento removido")
  public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
    eventService.deletarEvento(id);
    return ResponseEntity.noContent().build();
  }

}
