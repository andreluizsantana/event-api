package com.project.EventAPI.audit;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "revinfo")
@RevisionEntity(RevisaoListener.class)
public class RevisaoEvento extends DefaultRevisionEntity {

  @Column(name = "usuario", length = 100)
  private String usuario;

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
}
