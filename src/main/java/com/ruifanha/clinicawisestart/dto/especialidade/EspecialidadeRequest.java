package com.ruifanha.clinicawisestart.dto.especialidade;

import jakarta.validation.constraints.NotBlank;

// Dados recebidos para criar ou atualizar especialidades.
public record EspecialidadeRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	String nome
) {
}
