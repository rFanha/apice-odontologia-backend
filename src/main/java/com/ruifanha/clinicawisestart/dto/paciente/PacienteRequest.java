package com.ruifanha.clinicawisestart.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos para criar ou atualizar pacientes.
public record PacienteRequest(
	@NotBlank(message = "Nome e obrigatorio.")
	@Size(max = 255, message = "Nome deve ter no maximo 255 caracteres.")
	String nome,

	@NotBlank(message = "Email e obrigatorio.")
	@Email(message = "Email deve ter um formato valido.")
	@Size(max = 255, message = "Email deve ter no maximo 255 caracteres.")
	String email,

	@NotBlank(message = "CPF e obrigatorio.")
	@Size(max = 14, message = "CPF deve ter no maximo 14 caracteres.")
	String cpf,

	@Size(max = 30, message = "Telefone deve ter no maximo 30 caracteres.")
	String telefone
) {
}
