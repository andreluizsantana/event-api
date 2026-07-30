package com.project.EventAPI.controller;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.EventAPI.dto.response.EventoRevisaoDTO;
import com.project.EventAPI.service.EventService;
import com.project.EventAPI.service.EventoAuditService;

@RestController
@RequestMapping("/api/events")
public class EventAuditController {

  private final EventoAuditService auditService;
  private final EventService eventService;

  public EventAuditController(EventoAuditService auditService, EventService eventService) {
    this.auditService = auditService;
    this.eventService = eventService;
  }

  @GetMapping("/{id}/historico")
  public ResponseEntity<PagedModel<EventoRevisaoDTO>> historico(@PathVariable Long id, Pageable pageable) {
    eventService.buscarID(id);

    List<EventoRevisaoDTO> todas = auditService.listarRevisoes(id);

    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), todas.size());
    List<EventoRevisaoDTO> conteudo = todas.subList(start, end);
    PageImpl<EventoRevisaoDTO> pagina = new PageImpl<>(conteudo, pageable, todas.size());

    return ResponseEntity.ok(new PagedModel<>(pagina));
  }
}
