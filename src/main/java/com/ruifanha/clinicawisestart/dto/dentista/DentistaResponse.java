package com.ruifanha.clinicawisestart.dto.dentista;

import java.time.LocalDateTime;
import java.util.List;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;

// Dados retornados nos endpoints de dentistas.
public record DentistaResponse(
	Long id,
	String nome,
	String cpf,
	String email,
	String cro,
	Long especialidadeId,
	List<Long> especialidadeIds,
	EspecialidadeResumo especialidade,
	List<EspecialidadeResumo> especialidades,
	Boolean ativo,
	LocalDateTime dataCriacao
) {

	public record EspecialidadeResumo(Long id, String nome) {}

	public static DentistaResponse fromEntity(Dentista dentista) {
		List<EspecialidadeResumo> especialidades = dentista.getEspecialidades().stream()
			.map(de -> new EspecialidadeResumo(
				de.getEspecialidade().getId(),
				de.getEspecialidade().getNome()
			))
			.toList();

		Long especialidadeId = especialidades.isEmpty() ? null : especialidades.get(0).id();
		EspecialidadeResumo especialidade = especialidades.isEmpty() ? null : especialidades.get(0);
		List<Long> especialidadeIds = especialidades.stream()
			.map(EspecialidadeResumo::id)
			.toList();

		return new DentistaResponse(
			dentista.getId(),
			dentista.getNome(),
			dentista.getCpf(),
			dentista.getEmail(),
			dentista.getCro(),
			especialidadeId,
			especialidadeIds,
			especialidade,
			especialidades,
			dentista.getAtivo(),
			dentista.getDataCriacao()
		);
	}
}
