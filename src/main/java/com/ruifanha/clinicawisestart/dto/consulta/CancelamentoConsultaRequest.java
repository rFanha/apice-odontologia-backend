package com.ruifanha.clinicawisestart.dto.consulta;

import jakarta.validation.constraints.NotBlank;

// Dados recebidos para cancelar uma consulta.
public record CancelamentoConsultaRequest(
	@NotBlank(message = "Motivo do cancelamento e obrigatorio.")
	String motivoCancelamento
) {
}
