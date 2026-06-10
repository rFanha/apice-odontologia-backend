package com.ruifanha.clinicawisestart.dto.consulta;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;

// Dados recebidos para criar ou atualizar consultas.
public record ConsultaRequest(
	Long pacienteId,
	Long dentistaId,
	String descricao,
	String motivoCancelamento,
	LocalDateTime dataInicio,
	LocalDateTime dataFim,
	StatusConsulta status
) {
}
