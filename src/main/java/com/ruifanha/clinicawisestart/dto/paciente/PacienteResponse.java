package com.ruifanha.clinicawisestart.dto.paciente;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.paciente.Paciente;

// Dados retornados nos endpoints de pacientes.
public record PacienteResponse(
	Long id,
	String nome,
	String email,
	String cpf,
	String telefone,
	LocalDateTime dataCriacao
) {

	public static PacienteResponse fromEntity(Paciente paciente) {
		return new PacienteResponse(
			paciente.getId(),
			paciente.getNome(),
			paciente.getEmail(),
			paciente.getCpf(),
			paciente.getTelefone(),
			paciente.getDataCriacao()
		);
	}
}
