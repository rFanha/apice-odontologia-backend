package com.ruifanha.clinicawisestart.dto.lead;

import java.time.LocalDateTime;

import com.ruifanha.clinicawisestart.domain.lead.Lead;

public record LeadResponse(
	Long id,
	String nome,
	String telefone,
	String email,
	String especialidade,
	String mensagem,
	LocalDateTime dataCriacao,
	Boolean lido
) {

	public static LeadResponse fromEntity(Lead lead) {
		return new LeadResponse(
			lead.getId(),
			lead.getNome(),
			lead.getTelefone(),
			lead.getEmail(),
			lead.getEspecialidade(),
			lead.getMensagem(),
			lead.getDataCriacao(),
			lead.getLido()
		);
	}
}
