package com.ruifanha.clinicawisestart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.especialidade.Especialidade;
import com.ruifanha.clinicawisestart.dto.especialidade.EspecialidadeRequest;
import com.ruifanha.clinicawisestart.repository.EspecialidadeRepository;

// Service criado para concentrar regras de negocio relacionadas as especialidades.
@Service
public class EspecialidadeService {

	private final EspecialidadeRepository especialidadeRepository;

	public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
		this.especialidadeRepository = especialidadeRepository;
	}

	@Transactional
	public Especialidade criar(EspecialidadeRequest especialidadeRequest) {
		Especialidade especialidade = new Especialidade();
		aplicarDados(especialidade, especialidadeRequest);
		validarDuplicidadeNome(especialidade);
		return especialidadeRepository.save(especialidade);
	}

	@Transactional
	public Especialidade atualizar(Long id, EspecialidadeRequest especialidadeRequest) {
		Especialidade especialidade = buscarPorId(id);
		aplicarDados(especialidade, especialidadeRequest);
		validarDuplicidadeNome(especialidade);
		return especialidadeRepository.save(especialidade);
	}

	@Transactional
	public Especialidade salvar(Especialidade especialidade) {
		validarDuplicidadeNome(especialidade);
		return especialidadeRepository.save(especialidade);
	}

	@Transactional(readOnly = true)
	public List<Especialidade> listarTodas() {
		// Lista todas as especialidades para apoiar o cadastro de dentistas.
		return especialidadeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Especialidade> buscarPorNome(String nome) {
		// Permite localizar especialidades por parte do nome digitado.
		return especialidadeRepository.findByNomeContainingIgnoreCase(nome);
	}

	@Transactional(readOnly = true)
	public List<Especialidade> listar(String nome) {
		// Usa busca por nome quando o filtro for informado.
		if (nome != null && !nome.isBlank()) {
			return buscarPorNome(nome);
		}
		return listarTodas();
	}

	@Transactional(readOnly = true)
	public Especialidade buscarPorId(Long id) {
		// Centraliza a busca por id para manter uma mensagem padronizada.
		return especialidadeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Especialidade nao encontrada."));
	}

	@Transactional
	public void excluir(Long id) {
		// Confirma que a especialidade existe antes de solicitar a exclusao.
		Especialidade especialidade = buscarPorId(id);
		especialidadeRepository.delete(especialidade);
	}

	private void aplicarDados(Especialidade especialidade, EspecialidadeRequest especialidadeRequest) {
		// Copia os dados recebidos para a entidade antes de salvar.
		if (especialidadeRequest == null) {
			throw new IllegalArgumentException("Dados da especialidade sao obrigatorios.");
		}

		especialidade.setNome(especialidadeRequest.nome());
	}

	private void validarDuplicidadeNome(Especialidade especialidade) {
		// Evita cadastro de especialidades com nome ja utilizado por outro registro.
		especialidadeRepository.findByNome(especialidade.getNome())
			.filter(especialidadeExistente -> !especialidadeExistente.getId().equals(especialidade.getId()))
			.ifPresent(especialidadeExistente -> {
				throw new IllegalArgumentException("Ja existe especialidade cadastrada com este nome.");
			});
	}
}
