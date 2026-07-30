package com.project.EventAPI.service;

import com.project.EventAPI.audit.RevisaoEvento;
import com.project.EventAPI.dto.response.EventoRevisaoDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.exception.EventNotFoundException;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventoAuditService {

  private final EntityManager entityManager;

  public EventoAuditService(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @SuppressWarnings("unchecked")
  public List<EventoRevisaoDTO> listarRevisoes(Long eventoId) {
    List<Object[]> resultados = (List<Object[]>) AuditReaderFactory.get(entityManager)
        .createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .addOrder(AuditEntity.revisionNumber().asc())
        .getResultList();

    if (resultados.isEmpty()) {
      throw new EventNotFoundException("Nenhuma revisão encontrada para o evento ID: " + eventoId);
    }

    return resultados.stream()
        .map(this::toDTO)
        .toList();
  }

  public EventoRevisaoDTO buscarRevisao(Long eventoId, Long revisaoNum) {
    AuditReader reader = AuditReaderFactory.get(entityManager);
    Evento evento = reader.find(Evento.class, eventoId, revisaoNum);
    if (evento == null) {
      throw new EventNotFoundException(
          "Evento ID " + eventoId + " não encontrado na revisão " + revisaoNum);
    }
    RevisaoEvento rev = reader.findRevision(RevisaoEvento.class, revisaoNum);
    return buildDTO(evento, rev, RevisionType.MOD, revisaoNum);
  }

  @SuppressWarnings("unchecked")
  public List<EventoRevisaoDTO> listarRevisoesPorPeriodo(
      Long eventoId, LocalDateTime inicio, LocalDateTime fim) {
    long inicioMs = inicio.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long fimMs = fim.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    List<Object[]> resultados = (List<Object[]>) AuditReaderFactory.get(entityManager)
        .createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .add(AuditEntity.revisionProperty("timestamp").ge(inicioMs))
        .add(AuditEntity.revisionProperty("timestamp").le(fimMs))
        .addOrder(AuditEntity.revisionNumber().asc())
        .getResultList();

    return resultados.stream()
        .map(this::toDTO)
        .toList();
  }

  private EventoRevisaoDTO toDTO(Object[] row) {
    Evento evento = (Evento) row[0];
    RevisaoEvento rev = (RevisaoEvento) row[1];
    RevisionType tipo = (RevisionType) row[2];
    return buildDTO(evento, rev, tipo, rev.getId());
  }

  private EventoRevisaoDTO buildDTO(
      Evento evento, RevisaoEvento rev, RevisionType tipo, Number revNum) {
    EventResponseDTO dados = null;
    if (evento != null) {
      dados = new EventResponseDTO(
          evento.getId(),
          evento.getTitulo(),
          evento.getDescricaoEvento(),
          evento.getPrevisaoInicio(),
          evento.getPrevisaoFim(),
          evento.getStatus(),
          evento.getEndereco());
    }

    return new EventoRevisaoDTO(
        revNum.longValue(),
        Instant.ofEpochMilli(rev.getTimestamp()),
        rev.getUsuario(),
        tipo.name(),
        dados);
  }
}
