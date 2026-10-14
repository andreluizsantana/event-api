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
import com.project.EventAPI.dto.request.ItemEventoRequestDTO;
import com.project.EventAPI.dto.response.ItemEventoResponseDTO;
import com.project.EventAPI.dto.update.ItemEventoUpdateDTO;
import com.project.EventAPI.service.ItemEventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events/{eventoId}/itens")
@Tag(name = "Itens do Evento", description = "Gestão de itens de custo de um evento")
public class ItemEventoController {

  private final ItemEventoService itemEventoService;

  public ItemEventoController(ItemEventoService itemEventoService) {
    this.itemEventoService = itemEventoService;
  }

  @GetMapping
  @Operation(summary = "Lista os itens de um evento")
  public ResponseEntity<PagedModel<ItemEventoResponseDTO>> listar(@PathVariable Long eventoId, Pageable pageable) {
    Page<ItemEventoResponseDTO> itens = itemEventoService.listar(eventoId, pageable);
    return ResponseEntity.ok(new PagedModel<>(itens));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Busca um item por ID")
  public ResponseEntity<ItemEventoResponseDTO> buscar(@PathVariable Long eventoId, @PathVariable Long id) {
    return ResponseEntity.ok(itemEventoService.buscar(eventoId, id));
  }

  @PostMapping
  @Operation(summary = "Adiciona um item ao evento")
  public ResponseEntity<ItemEventoResponseDTO> salvar(@PathVariable Long eventoId,
      @Valid @RequestBody ItemEventoRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(itemEventoService.salvar(eventoId, dto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualiza um item")
  public ResponseEntity<ItemEventoResponseDTO> atualizar(@PathVariable Long eventoId, @PathVariable Long id,
      @Valid @RequestBody ItemEventoUpdateDTO dto) {
    return ResponseEntity.ok(itemEventoService.atualizar(eventoId, id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Remove um item")
  public ResponseEntity<Void> deletar(@PathVariable Long eventoId, @PathVariable Long id) {
    itemEventoService.deletar(eventoId, id);
    return ResponseEntity.noContent().build();
  }
}
