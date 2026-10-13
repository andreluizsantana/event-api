package com.project.EventAPI.audit;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Auditoria {

  @CreatedDate
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @CreatedBy
  @Column(name = "criado_por", updatable = false, length = 100)
  private String criadoPor;

  @LastModifiedDate
  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  @LastModifiedBy
  @Column(name = "atualizado_por", length = 100)
  private String atualizadoPor;

  public Auditoria() {
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public String getCriadoPor() {
    return criadoPor;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public String getAtualizadoPor() {
    return atualizadoPor;
  }
}
