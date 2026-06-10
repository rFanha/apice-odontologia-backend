package com.ruifanha.clinicawisestart.dto.dentista;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Dados recebidos para criar ou atualizar dentistas.
public record DentistaRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	String nome,

	@NotBlank(message = "CPF e obrigatorio.")
	String cpf,

	@NotBlank(message = "Email e obrigatorio.")
	String email,

	@NotBlank(message = "CRO e obrigatorio.")
	String cro,

	@NotNull(message = "Status ativo e obrigatorio.")
	Boolean ativo
) {
}
