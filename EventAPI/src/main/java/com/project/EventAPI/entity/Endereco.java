package com.project.EventAPI.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

@Embeddable
public class Endereco {
	
	@Size(max = 2)
	@Column(length = 2)
	private String uf;
	
	@Size(max = 120)
	@Column(length = 120)
	private String cidade;
	
	@Size(max = 200)
	@Column(length = 200)
	private String rua;
	
	@Size(max = 10)
	@Column(length = 10)
	private String numero;
	
	@Size(max = 9)
	@Column(length = 9)
	private String cep;
	
	@Size(max = 120)
	@Column(length = 120)
	private String referencia;

	public Endereco() {
	}

	public Endereco(String uf, String cidade, String rua, String numero, String cep, String referencia) {
		super();
		this.uf = uf;
		this.cidade = cidade;
		this.rua = rua;
		this.numero = numero;
		this.cep = cep;
		this.referencia = referencia;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

}
