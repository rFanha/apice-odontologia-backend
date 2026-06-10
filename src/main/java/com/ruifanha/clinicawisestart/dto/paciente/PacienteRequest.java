package com.ruifanha.clinicawisestart.dto.paciente;

// Dados recebidos para criar ou atualizar pacientes.
public record PacienteRequest(
	String nome,
	String email,
	String cpf,
	String telefone
) {
}
