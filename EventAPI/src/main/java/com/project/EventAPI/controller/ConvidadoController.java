package com.project.EventAPI.controller;

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
import com.project.EventAPI.dto.request.ConvidadoRequestDTO;
import com.project.EventAPI.dto.response.ConvidadoResponseDTO;
import com.project.EventAPI.dto.update.ConvidadoUpdateDTO;
import com.project.EventAPI.service.ConvidadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events/{eventoId}/convidados")
@Tag(name = "Convidados", description = "Gestão de convidados de um evento")
public class ConvidadoController {

  private final ConvidadoService convidadoService;

  public ConvidadoController(ConvidadoService convidadoService) {
    this.convidadoService = convidadoService;
  }

  @GetMapping
  @Operation(summary = "Lista os convidados de um evento")
  public ResponseEntity<PagedModel<ConvidadoResponseDTO>> listar(@PathVariable Long eventoId, Pageable pageable) {
    Page<ConvidadoResponseDTO> convidados = convidadoService.listar(eventoId, pageable);
    return ResponseEntity.ok(new PagedModel<>(convidados));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Busca um convidado por ID")
  public ResponseEntity<ConvidadoResponseDTO> buscar(@PathVariable Long eventoId, @PathVariable Long id) {
    return ResponseEntity.ok(convidadoService.buscar(eventoId, id));
  }

  @PostMapping
  @Operation(summary = "Adiciona um convidado ao evento")
  public ResponseEntity<ConvidadoResponseDTO> salvar(@PathVariable Long eventoId,
      @Valid @RequestBody ConvidadoRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(convidadoService.salvar(eventoId, dto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualiza um convidado")
  public ResponseEntity<ConvidadoResponseDTO> atualizar(@PathVariable Long eventoId, @PathVariable Long id,
      @Valid @RequestBody ConvidadoUpdateDTO dto) {
    return ResponseEntity.ok(convidadoService.atualizar(eventoId, id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Remove um convidado")
  public ResponseEntity<Void> deletar(@PathVariable Long eventoId, @PathVariable Long id) {
    convidadoService.deletar(eventoId, id);
    return ResponseEntity.noContent().build();
  }
}
