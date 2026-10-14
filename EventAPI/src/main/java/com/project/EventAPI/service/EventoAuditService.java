package com.project.EventAPI.service;

import com.project.EventAPI.audit.RevisaoEvento;
import com.project.EventAPI.dto.mapper.EventMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventoAuditService {

  private final EntityManager entityManager;
  private final EventMapper eventMapper;

  public EventoAuditService(EntityManager entityManager, EventMapper eventMapper) {
    this.entityManager = entityManager;
    this.eventMapper = eventMapper;
  }

  public Page<EventoRevisaoDTO> listarRevisoes(Long eventoId, Pageable pageable) {
    AuditReader reader = AuditReaderFactory.get(entityManager);

    @SuppressWarnings("unchecked")
    List<Object[]> resultados = (List<Object[]>) reader.createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .addOrder(AuditEntity.revisionNumber().desc())
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    long total = countRevisoes(eventoId);

    List<EventoRevisaoDTO> conteudo = resultados.stream()
        .map(this::toDTO)
        .toList();

    return new PageImpl<>(conteudo, pageable, total);
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

  public Page<EventoRevisaoDTO> listarRevisoesPorPeriodo(
      Long eventoId, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
    long inicioMs = inicio.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long fimMs = fim.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    AuditReader reader = AuditReaderFactory.get(entityManager);

    @SuppressWarnings("unchecked")
    List<Object[]> resultados = (List<Object[]>) reader.createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .add(AuditEntity.revisionProperty("timestamp").ge(inicioMs))
        .add(AuditEntity.revisionProperty("timestamp").le(fimMs))
        .addOrder(AuditEntity.revisionNumber().desc())
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    long total = countRevisoesPorPeriodo(eventoId, inicioMs, fimMs);

    List<EventoRevisaoDTO> conteudo = resultados.stream()
        .map(this::toDTO)
        .toList();

    return new PageImpl<>(conteudo, pageable, total);
  }

  private long countRevisoes(Long eventoId) {
    Number total = (Number) AuditReaderFactory.get(entityManager).createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .addProjection(AuditEntity.revisionNumber().count())
        .getSingleResult();
    return total.longValue();
  }

  private long countRevisoesPorPeriodo(Long eventoId, long inicioMs, long fimMs) {
    Number total = (Number) AuditReaderFactory.get(entityManager).createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .add(AuditEntity.revisionProperty("timestamp").ge(inicioMs))
        .add(AuditEntity.revisionProperty("timestamp").le(fimMs))
        .addProjection(AuditEntity.revisionNumber().count())
        .getSingleResult();
    return total.longValue();
  }

  private EventoRevisaoDTO toDTO(Object[] row) {
    Evento evento = (Evento) row[0];
    RevisaoEvento rev = (RevisaoEvento) row[1];
    RevisionType tipo = (RevisionType) row[2];
    return buildDTO(evento, rev, tipo, rev.getId());
  }

  private EventoRevisaoDTO buildDTO(
      Evento evento, RevisaoEvento rev, RevisionType tipo, Number revNum) {
    EventResponseDTO dados = evento == null ? null : eventMapper.entityToDto(evento);

    return new EventoRevisaoDTO(
        revNum.longValue(),
        Instant.ofEpochMilli(rev.getTimestamp()),
        rev.getUsuario(),
        tipo.name(),
        dados);
  }
}
