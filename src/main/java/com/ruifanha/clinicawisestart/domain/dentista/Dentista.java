package com.ruifanha.clinicawisestart.domain.dentista;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// Mapeia a tabela dentistas conforme os campos definidos na documentacao.
@Entity
@Table(name = "dentistas")
public class Dentista {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String cpf;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String cro;

	@Column(name = "data_criacao", nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	@Column(nullable = false)
	private Boolean ativo;

	// Mantem as especialidades vinculadas ao dentista pela tabela intermediaria.
	@OneToMany(mappedBy = "dentista")
	private List<DentistaEspecialidade> especialidades = new ArrayList<>();

	@PrePersist
	void prepararNovoRegistro() {
		// Define valores iniciais antes de salvar um novo dentista.
		if (dataCriacao == null) {
			dataCriacao = LocalDateTime.now();
		}
		if (ativo == null) {
			ativo = Boolean.TRUE;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<DentistaEspecialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<DentistaEspecialidade> especialidades) {
		this.especialidades = especialidades;
	}
}
