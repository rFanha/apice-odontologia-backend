package com.ruifanha.clinicawisestart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.lead.Lead;
import com.ruifanha.clinicawisestart.dto.lead.LeadRequest;
import com.ruifanha.clinicawisestart.repository.LeadRepository;

@Service
public class LeadService {

	private final LeadRepository leadRepository;

	public LeadService(LeadRepository leadRepository) {
		this.leadRepository = leadRepository;
	}

	@Transactional
	public Lead criar(LeadRequest request) {
		Lead lead = new Lead();
		lead.setNome(request.nome());
		lead.setTelefone(request.telefone());
		lead.setEmail(request.email());
		lead.setEspecialidade(request.especialidade());
		lead.setMensagem(request.mensagem());
		return leadRepository.save(lead);
	}

	@Transactional(readOnly = true)
	public List<Lead> listarTodos() {
		return leadRepository.findAllByOrderByDataCriacaoDesc();
	}

	@Transactional(readOnly = true)
	public List<Lead> listarNaoLidos() {
		return leadRepository.findByLidoOrderByDataCriacaoDesc(Boolean.FALSE);
	}

	@Transactional
	public Lead marcarComoLido(Long id) {
		Lead lead = leadRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Lead nao encontrado."));
		lead.setLido(Boolean.TRUE);
		return leadRepository.save(lead);
	}
}
