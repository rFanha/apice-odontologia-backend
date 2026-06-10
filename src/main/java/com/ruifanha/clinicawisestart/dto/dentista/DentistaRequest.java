package com.ruifanha.clinicawisestart.dto.dentista;

// Dados recebidos para criar ou atualizar dentistas.
public record DentistaRequest(
	String nome,
	String cpf,
	String email,
	String cro,
	Boolean ativo
) {
}
