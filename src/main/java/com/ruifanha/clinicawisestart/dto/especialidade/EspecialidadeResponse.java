package com.ruifanha.clinicawisestart.dto.especialidade;

import com.ruifanha.clinicawisestart.domain.especialidade.Especialidade;

// Dados retornados nos endpoints de especialidades.
public record EspecialidadeResponse(
	Long id,
	String nome
) {

	public static EspecialidadeResponse fromEntity(Especialidade especialidade) {
		return new EspecialidadeResponse(
			especialidade.getId(),
			especialidade.getNome()
		);
	}
}
