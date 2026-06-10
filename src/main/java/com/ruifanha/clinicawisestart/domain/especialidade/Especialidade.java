package com.ruifanha.clinicawisestart.domain.especialidade;

import java.util.ArrayList;
import java.util.List;

import com.ruifanha.clinicawisestart.domain.dentista.DentistaEspecialidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// Mapeia a tabela especialidades conforme os campos definidos na documentacao.
@Entity
@Table(name = "especialidades")
public class Especialidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String nome;

	// Mantem os dentistas vinculados a esta especialidade pela tabela intermediaria.
	@OneToMany(mappedBy = "especialidade")
	private List<DentistaEspecialidade> dentistas = new ArrayList<>();

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

	public List<DentistaEspecialidade> getDentistas() {
		return dentistas;
	}

	public void setDentistas(List<DentistaEspecialidade> dentistas) {
		this.dentistas = dentistas;
	}
}
