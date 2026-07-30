package com.project.EventAPI.entity;

import java.time.LocalDateTime;
import org.hibernate.envers.Audited;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lista_convidados")
@Audited
public class ListaConvidado {

  @EmbeddedId
  private ListaConvidadoId id;

  @Column(name = "data_adicao")
  private LocalDateTime dataAdicao;

  @Column(name = "presente_confirmado")
  private Boolean presenteConfirmado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "convidado_id", insertable = false, updatable = false)
  private Convidado convidado;

  public ListaConvidado() {}

  public ListaConvidadoId getId() {
    return id;
  }

  public void setId(ListaConvidadoId id) {
    this.id = id;
  }

  public LocalDateTime getDataAdicao() {
    return dataAdicao;
  }

  public void setDataAdicao(LocalDateTime dataAdicao) {
    this.dataAdicao = dataAdicao;
  }

  public Boolean getPresenteConfirmado() {
    return presenteConfirmado;
  }

  public void setPresenteConfirmado(Boolean presenteConfirmado) {
    this.presenteConfirmado = presenteConfirmado;
  }

  public Convidado getConvidado() {
    return convidado;
  }

  public void setConvidado(Convidado convidado) {
    this.convidado = convidado;
  }
}
