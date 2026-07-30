package com.project.EventAPI.entity;

import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "fornecedores")
@Audited
public class Fornecedor {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_seq")
  @SequenceGenerator(name = "fornecedor_seq", sequenceName = "fornecedor_sequence", initialValue = 1,
      allocationSize = 1)
  private Long id;

  @NotBlank
  @Size(max = 150)
  @Column(nullable = false, length = 150)
  private String nome;

  @Size(max = 200)
  @Column(length = 200)
  private String servico;

  @Column(name = "valor_contrato")
  private BigDecimal valorContrato;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evento_id", nullable = false)
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Evento evento;

  public Fornecedor() {}

  public Fornecedor(Long id, String nome, String servico, BigDecimal valorContrato, Evento evento) {
    this.id = id;
    this.nome = nome;
    this.servico = servico;
    this.valorContrato = valorContrato;
    this.evento = evento;
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
    Fornecedor other = (Fornecedor) obj;
    return Objects.equals(id, other.id);
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getServico() {
    return servico;
  }

  public void setServico(String servico) {
    this.servico = servico;
  }

  public BigDecimal getValorContrato() {
    return valorContrato;
  }

  public void setValorContrato(BigDecimal valorContrato) {
    this.valorContrato = valorContrato;
  }

  public Evento getEvento() {
    return evento;
  }

  public void setEvento(Evento evento) {
    this.evento = evento;
  }
}
