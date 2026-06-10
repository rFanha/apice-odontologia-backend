package com.ruifanha.clinicawisestart.dto.especialidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos para criar ou atualizar especialidades.
public record EspecialidadeRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	@Size(max = 255, message = "Nome deve ter no maximo 255 caracteres.")
	String nome
) {
}
