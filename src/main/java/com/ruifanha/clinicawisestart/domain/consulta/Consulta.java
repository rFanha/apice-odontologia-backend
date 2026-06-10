package com.ruifanha.clinicawisestart.domain.consulta;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;
import com.ruifanha.clinicawisestart.domain.paciente.Paciente;
import com.ruifanha.clinicawisestart.domain.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// Mapeia a tabela consultas com os relacionamentos principais do agendamento.
// Usando FetchType.LAZY para otimizar consultas e evitar carregamento desnecessário de dados relacionados.
@Entity
@Table(name = "consultas")
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_paciente", nullable = false)
	private Paciente paciente;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_dentista", nullable = false)
	private Dentista dentista;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

	@Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
	private String motivoCancelamento;

	@Column(name = "data_inicio", nullable = false)
	private LocalDateTime dataInicio;

	@Column(name = "data_fim", nullable = false)
	private LocalDateTime dataFim;

	@Column(name = "data_registro", nullable = false, updatable = false)
	private LocalDateTime dataRegistro;

	// Armazena o status como texto para facilitar consultas e relatorios.
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private StatusConsulta status;

	@PrePersist
	void prepararNovoRegistro() {
		// Define valores iniciais antes de salvar uma nova consulta.
		if (dataRegistro == null) {
			dataRegistro = LocalDateTime.now();
		}
		if (status == null) {
			status = StatusConsulta.AGENDADA;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Dentista getDentista() {
		return dentista;
	}

	public void setDentista(Dentista dentista) {
		this.dentista = dentista;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDateTime getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDateTime dataFim) {
		this.dataFim = dataFim;
	}

	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public StatusConsulta getStatus() {
		return status;
	}

	public void setStatus(StatusConsulta status) {
		this.status = status;
	}
}
