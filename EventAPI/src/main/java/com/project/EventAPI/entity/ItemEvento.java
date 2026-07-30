package com.project.EventAPI.entity;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "item_evento")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class ItemEvento {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_evento_seq")
  @SequenceGenerator(name = "item_evento_seq", sequenceName = "item_evento_sequence", initialValue = 1,
      allocationSize = 1)
  private long id;

  @NotBlank(message = "A descrição é obrigatória")
  @Size(max = 120, message = "A descrição deve ter no máximo 120 caracteres")
  @Column(nullable = false, length = 120, name = "descricao_item")
  private String descricao;

  @Column(name = "valor_item")
  private BigDecimal valor;

  @ManyToOne
  @JoinColumn(name = "evento_id", nullable = false)
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Evento evento;

  public ItemEvento() {}

  public ItemEvento(long id,
      @NotBlank(message = "A descrição é obrigatória") @Size(max = 120,
          message = "A descrição deve ter no máximo 120 caracteres") String descricao,
      BigDecimal valor, Evento evento) {
    super();
    this.id = id;
    this.descricao = descricao;
    this.valor = valor;
    this.evento = evento;
  }


  @Override
  public int hashCode() {
    return Objects.hash(Long.valueOf(id));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ItemEvento other = (ItemEvento) obj;
    return id == other.id;
  }

  @Override
  public String toString() {
    return "ItemEvento [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", evento=" + evento + "]";
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Evento getEvento() {
    return evento;
  }

  public void setEvento(Evento evento) {
    this.evento = evento;
  }

  public long getId() {
    return id;
  }



}
