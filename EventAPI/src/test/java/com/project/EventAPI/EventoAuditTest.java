package com.project.EventAPI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.project.EventAPI.audit.RevisaoEvento;
import com.project.EventAPI.entity.Evento;
import com.project.EventAPI.enums.Status;
import com.project.EventAPI.repository.EventRepository;
import com.project.EventAPI.service.EventService;
import com.project.EventAPI.service.EventoAuditService;
import com.project.EventAPI.support.AbstractIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.data.domain.PageRequest;

class EventoAuditTest extends AbstractIntegrationTest {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private EventoAuditService eventoAuditService;

  @Autowired
  private EventService eventService;

  private Evento criarEvento(String titulo, String descricao, Status status) {
    Evento evento = new Evento();
    evento.setTitulo(titulo);
    evento.setDescricaoEvento(descricao);
    evento.setPrevisaoInicio(LocalDateTime.now());
    evento.setPrevisaoFim(LocalDateTime.now().plusDays(1));
    evento.setStatus(status);
    return evento;
  }

  @Test
  void deveCriarRevisaoAoCriarEvento() {
    Long eventoId = transactionTemplate.execute(status -> {
      Evento evento = criarEvento("Evento Teste", "Descricao do evento de teste", Status.PENDENTE);
      return eventRepository.save(evento).getId();
    });

    EntityManager em = entityManagerFactory.createEntityManager();
    AuditReader reader = AuditReaderFactory.get(em);
    List<Number> revisoes = reader.getRevisions(Evento.class, eventoId);
    em.close();

    assertThat(revisoes).hasSize(1);

    em = entityManagerFactory.createEntityManager();
    reader = AuditReaderFactory.get(em);
    RevisaoEvento rev = reader.findRevision(RevisaoEvento.class, revisoes.get(0));
    em.close();

    assertThat(rev.getUsuario()).isEqualTo("system");
  }

  @Test
  void deveCriarRevisaoAoAtualizarEvento() {
    Long eventoId = transactionTemplate.execute(status -> {
      Evento evento = criarEvento("Original", "Descricao original", Status.PENDENTE);
      return eventRepository.save(evento).getId();
    });

    transactionTemplate.execute(status -> {
      Evento evento = eventRepository.findById(eventoId).orElseThrow();
      evento.setTitulo("Alterado");
      eventRepository.save(evento);
      return null;
    });

    EntityManager em = entityManagerFactory.createEntityManager();
    AuditReader reader = AuditReaderFactory.get(em);
    List<Number> revisoes = reader.getRevisions(Evento.class, eventoId);
    em.close();

    assertThat(revisoes).hasSize(2);
  }

  @Test
  void deveConterDadosDaEntidadeNaRevisao() {
    Long eventoId = transactionTemplate.execute(status -> {
      Evento evento = criarEvento("Meu Evento", "Desc", Status.CONCLUIDO);
      return eventRepository.save(evento).getId();
    });

    EntityManager em = entityManagerFactory.createEntityManager();
    AuditReader reader = AuditReaderFactory.get(em);
    List<Number> revisoes = reader.getRevisions(Evento.class, eventoId);
    em.close();

    assertThat(revisoes).isNotEmpty();

    em = entityManagerFactory.createEntityManager();
    reader = AuditReaderFactory.get(em);
    Evento revisao = reader.find(Evento.class, eventoId, revisoes.get(0));
    em.close();

    assertThat(revisao).isNotNull();
    assertThat(revisao.getTitulo()).isEqualTo("Meu Evento");
    assertThat(revisao.getStatus()).isEqualTo(Status.CONCLUIDO);
  }

  @Test
  void deveManterHistoricoConsultavelAposExclusaoDoEvento() {
    Long eventoId = transactionTemplate.execute(status ->
        eventRepository.save(criarEvento("Excluir", "Evento para excluir", Status.PENDENTE)).getId());

    eventService.deletarEvento(eventoId);

    var historico = eventoAuditService.listarRevisoes(eventoId, PageRequest.of(0, 10));

    assertThat(historico.getContent()).hasSize(2);
    assertThat(historico.getContent().get(0).operacao()).isEqualTo("DEL");
    assertThat(eventoAuditService.buscarRevisao(eventoId, historico.getContent().get(0).idRevisao()).operacao())
        .isEqualTo("DEL");
  }

  @Test
  void deveFiltrarHistoricoPorIntervaloComFusoExplicito() {
    Long eventoId = transactionTemplate.execute(status ->
        eventRepository.save(criarEvento("Período", "Evento para filtrar", Status.PENDENTE)).getId());
    OffsetDateTime agora = OffsetDateTime.now();

    var resultado = eventoAuditService.listarRevisoesPorPeriodo(eventoId,
        agora.minusMinutes(1), agora.plusMinutes(1), PageRequest.of(0, 10));

    assertThat(resultado.getContent()).hasSize(1);
  }

  @Test
  void deveRejeitarIntervaloDeAuditoriaInvertido() {
    OffsetDateTime agora = OffsetDateTime.now();

    assertThatThrownBy(() -> eventoAuditService.listarRevisoesPorPeriodo(1L,
        agora.plusMinutes(1), agora, PageRequest.of(0, 10)))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
