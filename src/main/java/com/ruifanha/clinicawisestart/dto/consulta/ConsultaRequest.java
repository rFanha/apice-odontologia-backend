package com.ruifanha.clinicawisestart.dto.consulta;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Dados recebidos para criar ou atualizar consultas.
public record ConsultaRequest(
	@NotNull(message = "Paciente e obrigatorio.")
	Long pacienteId,

	@NotNull(message = "Dentista e obrigatorio.")
	Long dentistaId,

	@NotBlank(message = "Descricao e obrigatoria.")
	String descricao,

	String motivoCancelamento,

	@NotNull(message = "Data inicial da consulta e obrigatoria.")
	LocalDateTime dataInicio,

	@NotNull(message = "Data final da consulta e obrigatoria.")
	LocalDateTime dataFim,

	StatusConsulta status
) {
}
