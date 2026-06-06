package com.ruifanha.clinicawisestart.domain.dentista;

import com.ruifanha.clinicawisestart.domain.especialidade.Especialidade;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// Representa a ligacao muitos-para-muitos entre dentistas e especialidades.
@Entity
@Table(
	name = "dentista_especialidade",
	uniqueConstraints = @UniqueConstraint(columnNames = {"id_dentista", "id_especialidade"})
)
public class DentistaEspecialidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_dentista", nullable = false)
	private Dentista dentista;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_especialidade", nullable = false)
	private Especialidade especialidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dentista getDentista() {
		return dentista;
	}

	public void setDentista(Dentista dentista) {
		this.dentista = dentista;
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}
}
