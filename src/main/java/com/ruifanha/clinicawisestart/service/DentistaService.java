package com.ruifanha.clinicawisestart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;
import com.ruifanha.clinicawisestart.dto.dentista.DentistaRequest;
import com.ruifanha.clinicawisestart.repository.DentistaRepository;

// Service criado para concentrar regras de negocio relacionadas aos dentistas.
@Service
public class DentistaService {

	private final DentistaRepository dentistaRepository;

	public DentistaService(DentistaRepository dentistaRepository) {
		this.dentistaRepository = dentistaRepository;
	}

	@Transactional
	public Dentista criar(DentistaRequest dentistaRequest) {
		Dentista dentista = new Dentista();
		aplicarDados(dentista, dentistaRequest);
		validarDuplicidadeEmail(dentista);
		validarDuplicidadeCpf(dentista);
		validarDuplicidadeCro(dentista);
		return dentistaRepository.save(dentista);
	}

	@Transactional
	public Dentista atualizar(Long id, DentistaRequest dentistaRequest) {
		Dentista dentista = buscarPorId(id);
		aplicarDados(dentista, dentistaRequest);
		validarDuplicidadeEmail(dentista);
		validarDuplicidadeCpf(dentista);
		validarDuplicidadeCro(dentista);
		return dentistaRepository.save(dentista);
	}

	@Transactional
	public Dentista salvar(Dentista dentista) {
		validarDuplicidadeEmail(dentista);
		validarDuplicidadeCpf(dentista);
		validarDuplicidadeCro(dentista);
		return dentistaRepository.save(dentista);
	}

	@Transactional(readOnly = true)
	public List<Dentista> listarTodos() {
		// Lista todos os dentistas para apoiar telas de cadastro e manutencao.
		return dentistaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Dentista> listarTodos(Boolean ativo) {
		// Permite filtrar dentistas ativos quando o parametro for informado.
		if (ativo != null) {
			return dentistaRepository.findByAtivo(ativo);
		}
		return listarTodos();
	}

	@Transactional(readOnly = true)
	public List<Dentista> listarAtivos() {
		// Lista apenas dentistas ativos para uso em agendamentos.
		return dentistaRepository.findByAtivo(Boolean.TRUE);
	}

	@Transactional(readOnly = true)
	public Dentista buscarPorId(Long id) {
		// Centraliza a busca por id para manter uma mensagem padronizada.
		return dentistaRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Dentista nao encontrado."));
	}

	@Transactional
	public void excluir(Long id) {
		// Confirma que o dentista existe antes de solicitar a exclusao.
		Dentista dentista = buscarPorId(id);
		dentistaRepository.delete(dentista);
	}

	private void aplicarDados(Dentista dentista, DentistaRequest dentistaRequest) {
		// Copia os dados recebidos para a entidade antes de salvar.
		if (dentistaRequest == null) {
			throw new IllegalArgumentException("Dados do dentista sao obrigatorios.");
		}

		dentista.setNome(dentistaRequest.nome());
		dentista.setCpf(dentistaRequest.cpf());
		dentista.setEmail(dentistaRequest.email());
		dentista.setCro(dentistaRequest.cro());
		dentista.setAtivo(dentistaRequest.ativo());
	}

	private void validarDuplicidadeEmail(Dentista dentista) {
		// Evita cadastro de dentistas com email ja utilizado por outro registro.
		dentistaRepository.findByEmail(dentista.getEmail())
			.filter(dentistaExistente -> !dentistaExistente.getId().equals(dentista.getId()))
			.ifPresent(dentistaExistente -> {
				throw new IllegalArgumentException("Ja existe dentista cadastrado com este email.");
			});
	}

	private void validarDuplicidadeCpf(Dentista dentista) {
		// Evita cadastro de dentistas com CPF ja utilizado por outro registro.
		dentistaRepository.findByCpf(dentista.getCpf())
			.filter(dentistaExistente -> !dentistaExistente.getId().equals(dentista.getId()))
			.ifPresent(dentistaExistente -> {
				throw new IllegalArgumentException("Ja existe dentista cadastrado com este CPF.");
			});
	}

	private void validarDuplicidadeCro(Dentista dentista) {
		// Evita cadastro de dentistas com CRO ja utilizado por outro registro.
		dentistaRepository.findByCro(dentista.getCro())
			.filter(dentistaExistente -> !dentistaExistente.getId().equals(dentista.getId()))
			.ifPresent(dentistaExistente -> {
				throw new IllegalArgumentException("Ja existe dentista cadastrado com este CRO.");
			});
	}
}
