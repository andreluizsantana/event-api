package com.project.EventAPI.service;

import com.project.EventAPI.audit.RevisaoEvento;
import com.project.EventAPI.dto.mapper.EventMapper;
import com.project.EventAPI.dto.response.EventoRevisaoDTO;
import com.project.EventAPI.dto.response.EventResponseDTO;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.exception.EventNotFoundException;
import com.project.EventAPI.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.OffsetDateTime;
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
    garantirHistoricoExiste(reader, eventoId);

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
    garantirHistoricoExiste(reader, eventoId);
    @SuppressWarnings("unchecked")
    List<Object[]> resultados = (List<Object[]>) reader.createQuery()
        .forRevisionsOfEntity(Evento.class, false, true)
        .add(AuditEntity.id().eq(eventoId))
        .add(AuditEntity.revisionNumber().eq(revisaoNum))
        .getResultList();
    if (resultados.isEmpty()) {
      throw new ResourceNotFoundException(
          "Revisão " + revisaoNum + " não encontrada para o evento " + eventoId);
    }
    return toDTO(resultados.get(0));
  }

  public Page<EventoRevisaoDTO> listarRevisoesPorPeriodo(
      Long eventoId, OffsetDateTime inicio, OffsetDateTime fim, Pageable pageable) {
    if (inicio.isAfter(fim)) {
      throw new IllegalArgumentException("A data inicial deve ser anterior ou igual à data final.");
    }
    long inicioMs = inicio.toInstant().toEpochMilli();
    long fimMs = fim.toInstant().toEpochMilli();

    AuditReader reader = AuditReaderFactory.get(entityManager);
    garantirHistoricoExiste(reader, eventoId);

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

  private void garantirHistoricoExiste(AuditReader reader, Long eventoId) {
    if (reader.getRevisions(Evento.class, eventoId).isEmpty()) {
      throw new EventNotFoundException(eventoId);
    }
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
