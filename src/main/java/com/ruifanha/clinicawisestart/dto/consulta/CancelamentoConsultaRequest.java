package com.ruifanha.clinicawisestart.dto.consulta;

// Dados recebidos para cancelar uma consulta.
public record CancelamentoConsultaRequest(
	String motivoCancelamento
) {
}
