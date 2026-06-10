package com.ruifanha.clinicawisestart.dto.dentista;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;

// Dados retornados nos endpoints de dentistas.
public record DentistaResponse(
	Long id,
	String nome,
	String cpf,
	String email,
	String cro,
	Boolean ativo,
	LocalDateTime dataCriacao
) {

	public static DentistaResponse fromEntity(Dentista dentista) {
		return new DentistaResponse(
			dentista.getId(),
			dentista.getNome(),
			dentista.getCpf(),
			dentista.getEmail(),
			dentista.getCro(),
			dentista.getAtivo(),
			dentista.getDataCriacao()
		);
	}
}
