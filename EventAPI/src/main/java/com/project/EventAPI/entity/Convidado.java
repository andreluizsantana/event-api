package com.project.EventAPI.entity;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "convidados")
@Audited
public class Convidado {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convidado_seq")
  @SequenceGenerator(name = "convidado_seq", sequenceName = "convidado_sequence", initialValue = 1, allocationSize = 1)
  private Long id;

  @NotBlank
  @Size(max = 150)
  @Column(nullable = false, length = 150)
  private String nome;

  @Email
  @Size(max = 200)
  @Column(length = 200)
  private String email;

  @Size(max = 20)
  @Column(length = 20)
  private String telefone;

  @Column(nullable = false)
  private Boolean confirmado = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evento_id", nullable = false)
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Evento evento;

  public Convidado() {}

  public Convidado(Long id, String nome, String email, String telefone, Boolean confirmado, Evento evento) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
    this.confirmado = confirmado;
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
    Convidado other = (Convidado) obj;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public Boolean getConfirmado() {
    return confirmado;
  }

  public void setConfirmado(Boolean confirmado) {
    this.confirmado = confirmado;
  }

  public Evento getEvento() {
    return evento;
  }

  public void setEvento(Evento evento) {
    this.evento = evento;
  }
}
