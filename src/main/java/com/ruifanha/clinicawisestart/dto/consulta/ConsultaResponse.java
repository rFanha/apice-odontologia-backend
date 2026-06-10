package com.ruifanha.clinicawisestart.dto.consulta;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.consulta.Consulta;
import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;

// Dados retornados nos endpoints de consultas.
public record ConsultaResponse(
	Long id,
	Long pacienteId,
	Long dentistaId,
	Long usuarioId,
	String descricao,
	String motivoCancelamento,
	LocalDateTime dataInicio,
	LocalDateTime dataFim,
	LocalDateTime dataRegistro,
	StatusConsulta status
) {

	public static ConsultaResponse fromEntity(Consulta consulta) {
		return new ConsultaResponse(
			consulta.getId(),
			consulta.getPaciente().getId(),
			consulta.getDentista().getId(),
			consulta.getUsuario().getId(),
			consulta.getDescricao(),
			consulta.getMotivoCancelamento(),
			consulta.getDataInicio(),
			consulta.getDataFim(),
			consulta.getDataRegistro(),
			consulta.getStatus()
		);
	}
}
