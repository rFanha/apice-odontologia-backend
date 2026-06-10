package com.ruifanha.clinicawisestart.dto.paciente;

import jakarta.validation.constraints.NotBlank;

// Dados recebidos para criar ou atualizar pacientes.
public record PacienteRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	String nome,

	@NotBlank(message = "Email e obrigatorio.")
	String email,

	@NotBlank(message = "CPF e obrigatorio.")
	String cpf,

	String telefone
) {
}
