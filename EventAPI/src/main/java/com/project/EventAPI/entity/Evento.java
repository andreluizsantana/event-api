package com.project.EventAPI.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;
import com.project.EventAPI.audit.Auditoria;
import com.project.EventAPI.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "eventos")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Evento extends RepresentationModel<Evento> implements Serializable {

  private static final long serialVersionID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_seq")
  @SequenceGenerator(name = "evento_seq", sequenceName = "evento_sequence", initialValue = 1, allocationSize = 1)
  private Long id;

  @NotBlank(message = "O título é obrigatório")
  @Size(max = 120, message = "O título deve ter no máximo 120 caracteres")
  @Column(nullable = false, length = 120)
  private String titulo;

  @NotBlank(message = "Descrição sobre o veneto é obrigatório")
  @Column(name = "descricao_evento", nullable = false, columnDefinition = "TEXT")
  private String descricaoEvento;

  @NotNull(message = "Data inicio, prevista para o evento é obrigatório")
  @Column(name = "previsao_inicio")
  private LocalDateTime previsaoInicio;

  @NotNull(message = "Data fim, prevista para o evento é obrigatório")
  @Column(name = "previsao_fim")
  private LocalDateTime previsaoFim;

  @Column(name = "custo_evento")
  private BigDecimal custoEvento;

  @Column(name = "custo_medio_convidado")
  private BigDecimal custoMedioConvidado;

  @Column(nullable = false, length = 60)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Embedded
  Endereco endereco;

  @OneToMany(mappedBy = "evento")
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private List<ItemEvento> itensEvento;

  @NotAudited
  @Embedded
  private final Auditoria auditoria = new Auditoria();

  public Evento() {}

  public Evento(Long id,
      @NotBlank(message = "O título é obrigatório") @Size(max = 120,
          message = "O título deve ter no máximo 120 caracteres") String titulo,
      @NotBlank(message = "Descrição sobre o veneto é obrigatório") String descricaoEvento,
      @NotNull(message = "Data inicio, prevista para o evento é obrigatório") LocalDateTime previsaoInicio,
      @NotNull(message = "Data fim, prevista para o evento é obrigatório") LocalDateTime previsaoFim,
      BigDecimal custoEvento, BigDecimal custoMedioConvidado, Status status, Endereco endereco) {
    super();
    this.id = id;
    this.titulo = titulo;
    this.descricaoEvento = descricaoEvento;
    this.previsaoInicio = previsaoInicio;
    this.previsaoFim = previsaoFim;
    this.custoEvento = custoEvento;
    this.custoMedioConvidado = custoMedioConvidado;
    this.status = status;
    this.endereco = endereco;
  }

  @Override
  public String toString() {
    return "Evento [id=" + id + ", titulo=" + titulo + ", descricaoEvento=" + descricaoEvento + ", previsaoInicio="
        + previsaoInicio + ", previsaoFim=" + previsaoFim + ", custoEvento=" + custoEvento + ", custoMedioConvidado="
        + custoMedioConvidado + ", status=" + status + ", endereco=" + endereco + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Evento other = (Evento) obj;
    return Objects.equals(id, other.id);
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricaoEvento() {
    return descricaoEvento;
  }

  public void setDescricaoEvento(String descricaoEvento) {
    this.descricaoEvento = descricaoEvento;
  }

  public LocalDateTime getPrevisaoInicio() {
    return previsaoInicio;
  }

  public void setPrevisaoInicio(LocalDateTime previsaoInicio) {
    this.previsaoInicio = previsaoInicio;
  }

  public LocalDateTime getPrevisaoFim() {
    return previsaoFim;
  }

  public void setPrevisaoFim(LocalDateTime previsaoFim) {
    this.previsaoFim = previsaoFim;
  }

  public BigDecimal getCustoEvento() {
    return custoEvento;
  }

  public void setCustoEvento(BigDecimal custoEvento) {
    this.custoEvento = custoEvento;
  }

  public BigDecimal getCustoMedioConvidado() {
    return custoMedioConvidado;
  }

  public void setCustoMedioConvidado(BigDecimal custoMedioConvidado) {
    this.custoMedioConvidado = custoMedioConvidado;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }

  public Long getId() {
    return id;
  }

  public Auditoria getAuditoria() {
    return auditoria;
  }

  public List<ItemEvento> getItensEvento() {
    return itensEvento;
  }

  public void setItensEvento(List<ItemEvento> itensEvento) {
    this.itensEvento = itensEvento;
  }



}
