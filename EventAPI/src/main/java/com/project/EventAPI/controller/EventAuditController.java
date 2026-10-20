package com.project.EventAPI.controller;

import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.EventAPI.dto.response.EventoRevisaoDTO;
import com.project.EventAPI.service.EventoAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Histórico de Eventos", description = "Consultas de auditoria (Hibernate Envers)")
public class EventAuditController {

  private final EventoAuditService auditService;
  public EventAuditController(EventoAuditService auditService) {
    this.auditService = auditService;
  }

  @GetMapping("/{id}/historico")
  @Operation(summary = "Lista o histórico de revisões de um evento")
  public ResponseEntity<PagedModel<EventoRevisaoDTO>> historico(@PathVariable Long id, Pageable pageable) {
    Page<EventoRevisaoDTO> revisoes = auditService.listarRevisoes(id, pageable);
    return ResponseEntity.ok(new PagedModel<>(revisoes));
  }

  @GetMapping("/{id}/historico/periodo")
  @Operation(summary = "Lista as revisões de um evento em um período")
  public ResponseEntity<PagedModel<EventoRevisaoDTO>> historicoPorPeriodo(
      @PathVariable Long id,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fim,
      Pageable pageable) {
    Page<EventoRevisaoDTO> revisoes = auditService.listarRevisoesPorPeriodo(id, inicio, fim, pageable);
    return ResponseEntity.ok(new PagedModel<>(revisoes));
  }

  @GetMapping("/{id}/historico/{revisao}")
  @Operation(summary = "Busca uma revisão específica de um evento")
  public ResponseEntity<EventoRevisaoDTO> revisao(
      @PathVariable Long id, @PathVariable Long revisao) {
    return ResponseEntity.ok(auditService.buscarRevisao(id, revisao));
  }
}
