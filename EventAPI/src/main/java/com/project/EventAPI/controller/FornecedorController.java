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
import com.project.EventAPI.dto.request.FornecedorRequestDTO;
import com.project.EventAPI.dto.response.FornecedorResponseDTO;
import com.project.EventAPI.dto.update.FornecedorUpdateDTO;
import com.project.EventAPI.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events/{eventoId}/fornecedores")
@Tag(name = "Fornecedores", description = "Gestão de fornecedores de um evento")
public class FornecedorController {

  private final FornecedorService fornecedorService;

  public FornecedorController(FornecedorService fornecedorService) {
    this.fornecedorService = fornecedorService;
  }

  @GetMapping
  @Operation(summary = "Lista os fornecedores de um evento")
  public ResponseEntity<PagedModel<FornecedorResponseDTO>> listar(@PathVariable Long eventoId, Pageable pageable) {
    Page<FornecedorResponseDTO> fornecedores = fornecedorService.listar(eventoId, pageable);
    return ResponseEntity.ok(new PagedModel<>(fornecedores));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Busca um fornecedor por ID")
  public ResponseEntity<FornecedorResponseDTO> buscar(@PathVariable Long eventoId, @PathVariable Long id) {
    return ResponseEntity.ok(fornecedorService.buscar(eventoId, id));
  }

  @PostMapping
  @Operation(summary = "Adiciona um fornecedor ao evento")
  public ResponseEntity<FornecedorResponseDTO> salvar(@PathVariable Long eventoId,
      @Valid @RequestBody FornecedorRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.salvar(eventoId, dto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualiza um fornecedor")
  public ResponseEntity<FornecedorResponseDTO> atualizar(@PathVariable Long eventoId, @PathVariable Long id,
      @Valid @RequestBody FornecedorUpdateDTO dto) {
    return ResponseEntity.ok(fornecedorService.atualizar(eventoId, id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Remove um fornecedor")
  public ResponseEntity<Void> deletar(@PathVariable Long eventoId, @PathVariable Long id) {
    fornecedorService.deletar(eventoId, id);
    return ResponseEntity.noContent().build();
  }
}
