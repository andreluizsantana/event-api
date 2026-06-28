package com.project.EventAPI.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class evento {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="evento_seq")
	@SequenceGenerator(name = "evento_seq", sequenceName = "evento_sequence", initialValue = 1, allocationSize = 1)
	private Long id;
	
	@NotBlank(message = "O título é obrigatório")
	@Size(max = 120, message = "O título deve ter no máximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String titulo;
	
	@NotBlank(message = "Descrição sobre o veneto é obrigatório")
	@Column(name ="descricao_evento", nullable = false, columnDefinition = "TEXT")
	private String descricaoEvento;
	
	@NotBlank(message = "Data prevista para o evento é obrigatório")
	@Column(name = "data_evento")
	private LocalDateTime dataEvento;
	
	@Column(name = "valor_total")
	private BigDecimal valorTotal;
	
	
}
