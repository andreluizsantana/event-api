package com.project.EventAPI.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "evento_auditoria")
public class EventoAuditoria {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_aud_seq")
  @SequenceGenerator(name = "evento_aud_seq", sequenceName = "evento_auditoria_sequence", initialValue = 1, allocationSize = 1)
  private Long id;

  @Column(name = "evento_id", nullable = false)
  private Long eventoId;

  @Column(name = "campo_modificado", nullable = false, length = 60)
  private String campoModificado;

  @Column(name = "valor_anterior", columnDefinition = "TEXT")
  private String valorAnterior;

  @Column(name = "valor_novo", columnDefinition = "TEXT")
  private String valorNovo;

  @Column(name = "data_modificacao", nullable = false)
  private LocalDateTime dataModificacao;

  public EventoAuditoria() {}

  public EventoAuditoria(Long eventoId, String campoModificado, String valorAnterior, String valorNovo, LocalDateTime dataModificacao) {
    this.eventoId = eventoId;
    this.campoModificado = campoModificado;
    this.valorAnterior = valorAnterior;
    this.valorNovo = valorNovo;
    this.dataModificacao = dataModificacao;
  }

  public Long getId() { return id; }
  public Long getEventoId() { return eventoId; }
  public String getCampoModificado() { return campoModificado; }
  public String getValorAnterior() { return valorAnterior; }
  public String getValorNovo() { return valorNovo; }
  public LocalDateTime getDataModificacao() { return dataModificacao; }

  public void setId(Long id) { this.id = id; }
  public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
  public void setCampoModificado(String campoModificado) { this.campoModificado = campoModificado; }
  public void setValorAnterior(String valorAnterior) { this.valorAnterior = valorAnterior; }
  public void setValorNovo(String valorNovo) { this.valorNovo = valorNovo; }
  public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }
}
