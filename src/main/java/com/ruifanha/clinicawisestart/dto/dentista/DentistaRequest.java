package com.ruifanha.clinicawisestart.dto.dentista;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Dados recebidos para criar ou atualizar dentistas.
public record DentistaRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	@Size(max = 255, message = "Nome deve ter no maximo 255 caracteres.")
	String nome,

	@NotBlank(message = "CPF e obrigatorio.")
	@Size(max = 14, message = "CPF deve ter no maximo 14 caracteres.")
	String cpf,

	@NotBlank(message = "Email e obrigatorio.")
	@Size(max = 255, message = "Email deve ter no maximo 255 caracteres.")
	String email,

	@NotBlank(message = "CRO e obrigatorio.")
	@Size(max = 30, message = "CRO deve ter no maximo 30 caracteres.")
	String cro,

	@NotNull(message = "Status ativo e obrigatorio.")
	Boolean ativo
) {
}
