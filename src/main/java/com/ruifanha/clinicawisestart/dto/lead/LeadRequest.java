package com.ruifanha.clinicawisestart.dto.lead;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LeadRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	@Size(max = 255, message = "Nome deve ter no maximo 255 caracteres.")
	String nome,

	@NotBlank(message = "Telefone e obrigatorio.")
	@Size(max = 30, message = "Telefone deve ter no maximo 30 caracteres.")
	String telefone,

	@Size(max = 255, message = "Email deve ter no maximo 255 caracteres.")
	String email,

	@Size(max = 255, message = "Especialidade deve ter no maximo 255 caracteres.")
	String especialidade,

	String mensagem
) {
}
