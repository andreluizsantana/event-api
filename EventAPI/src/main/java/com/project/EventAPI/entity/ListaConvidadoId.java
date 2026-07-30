package com.project.EventAPI.entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ListaConvidadoId implements Serializable {

  @Column(name = "convidado_id")
  private Long convidadoId;

  @Column(name = "lista_id")
  private Long listaId;

  public ListaConvidadoId() {}

  public ListaConvidadoId(Long convidadoId, Long listaId) {
    this.convidadoId = convidadoId;
    this.listaId = listaId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(convidadoId, listaId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ListaConvidadoId other = (ListaConvidadoId) obj;
    return Objects.equals(convidadoId, other.convidadoId) && Objects.equals(listaId, other.listaId);
  }

  public Long getConvidadoId() {
    return convidadoId;
  }

  public void setConvidadoId(Long convidadoId) {
    this.convidadoId = convidadoId;
  }

  public Long getListaId() {
    return listaId;
  }

  public void setListaId(Long listaId) {
    this.listaId = listaId;
  }
}
