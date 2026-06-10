package com.ruifanha.clinicawisestart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.paciente.Paciente;
import com.ruifanha.clinicawisestart.dto.paciente.PacienteRequest;
import com.ruifanha.clinicawisestart.repository.PacienteRepository;

// Service criado para concentrar regras de negocio relacionadas aos pacientes.
@Service
public class PacienteService {

	private final PacienteRepository pacienteRepository;

	public PacienteService(PacienteRepository pacienteRepository) {
		this.pacienteRepository = pacienteRepository;
	}

	@Transactional
	public Paciente criar(PacienteRequest pacienteRequest) {
		Paciente paciente = new Paciente();
		aplicarDados(paciente, pacienteRequest);
		validarDuplicidadeEmail(paciente);
		validarDuplicidadeCpf(paciente);
		return pacienteRepository.save(paciente);
	}

	@Transactional
	public Paciente atualizar(Long id, PacienteRequest pacienteRequest) {
		Paciente paciente = buscarPorId(id);
		aplicarDados(paciente, pacienteRequest);
		validarDuplicidadeEmail(paciente);
		validarDuplicidadeCpf(paciente);
		return pacienteRepository.save(paciente);
	}

	@Transactional
	public Paciente salvar(Paciente paciente) {
		validarDuplicidadeEmail(paciente);
		validarDuplicidadeCpf(paciente);
		return pacienteRepository.save(paciente);
	}

	@Transactional(readOnly = true)
	public List<Paciente> listarTodos() {
		// Lista todos os pacientes para apoiar telas de cadastro e agendamento.
		return pacienteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Paciente buscarPorId(Long id) {
		// Centraliza a busca por id para manter uma mensagem padronizada.
		return pacienteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Paciente nao encontrado."));
	}

	@Transactional
	public void excluir(Long id) {
		// Confirma que o paciente existe antes de solicitar a exclusao.
		Paciente paciente = buscarPorId(id);
		pacienteRepository.delete(paciente);
	}

	private void aplicarDados(Paciente paciente, PacienteRequest pacienteRequest) {
		// Copia os dados recebidos para a entidade antes de salvar.
		if (pacienteRequest == null) {
			throw new IllegalArgumentException("Dados do paciente sao obrigatorios.");
		}

		paciente.setNome(pacienteRequest.nome());
		paciente.setEmail(pacienteRequest.email());
		paciente.setCpf(pacienteRequest.cpf());
		paciente.setTelefone(pacienteRequest.telefone());
	}

	private void validarDuplicidadeEmail(Paciente paciente) {
		// Evita cadastro de pacientes com email ja utilizado por outro registro.
		pacienteRepository.findByEmail(paciente.getEmail())
			.filter(pacienteExistente -> !pacienteExistente.getId().equals(paciente.getId()))
			.ifPresent(pacienteExistente -> {
				throw new IllegalArgumentException("Ja existe paciente cadastrado com este email.");
			});
	}

	private void validarDuplicidadeCpf(Paciente paciente) {
		// Evita cadastro de pacientes com CPF ja utilizado por outro registro.
		pacienteRepository.findByCpf(paciente.getCpf())
			.filter(pacienteExistente -> !pacienteExistente.getId().equals(paciente.getId()))
			.ifPresent(pacienteExistente -> {
				throw new IllegalArgumentException("Ja existe paciente cadastrado com este CPF.");
			});
	}
}
