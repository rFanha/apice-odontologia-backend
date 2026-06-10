package com.ruifanha.clinicawisestart.dto.consulta;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;
import com.ruifanha.clinicawisestart.validation.PeriodoConsultaValido;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Dados recebidos para criar ou atualizar consultas.
@PeriodoConsultaValido
public record ConsultaRequest(
	@NotNull(message = "Paciente e obrigatorio.")
	Long pacienteId,

	@NotNull(message = "Dentista e obrigatorio.")
	Long dentistaId,

	@NotBlank(message = "Descricao e obrigatoria.")
	String descricao,

	String motivoCancelamento,

	@NotNull(message = "Data inicial da consulta e obrigatoria.")
	@FutureOrPresent(message = "Data inicial da consulta nao pode estar no passado.")
	LocalDateTime dataInicio,

	@NotNull(message = "Data final da consulta e obrigatoria.")
	@FutureOrPresent(message = "Data final da consulta nao pode estar no passado.")
	LocalDateTime dataFim,

	StatusConsulta status
) {
}
