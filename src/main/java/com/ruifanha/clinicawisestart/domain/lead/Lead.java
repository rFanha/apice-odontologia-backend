package com.ruifanha.clinicawisestart.domain.lead;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "leads")
public class Lead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String nome;

	@Column(nullable = false, length = 30)
	private String telefone;

	@Column(length = 255)
	private String email;

	@Column(length = 255)
	private String especialidade;

	@Column(columnDefinition = "TEXT")
	private String mensagem;

	@Column(name = "data_criacao", nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	@Column(nullable = false)
	private Boolean lido;

	@PrePersist
	void prepararNovoRegistro() {
		if (dataCriacao == null) {
			dataCriacao = LocalDateTime.now();
		}
		if (lido == null) {
			lido = Boolean.FALSE;
		}
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getEspecialidade() { return especialidade; }
	public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	public LocalDateTime getDataCriacao() { return dataCriacao; }
	public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

	public Boolean getLido() { return lido; }
	public void setLido(Boolean lido) { this.lido = lido; }
}
