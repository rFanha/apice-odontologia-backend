package com.ruifanha.clinicawisestart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.dentista.Dentista;
import com.ruifanha.clinicawisestart.domain.dentista.DentistaEspecialidade;
import com.ruifanha.clinicawisestart.domain.especialidade.Especialidade;
import com.ruifanha.clinicawisestart.dto.dentista.DentistaRequest;
import com.ruifanha.clinicawisestart.repository.DentistaEspecialidadeRepository;
import com.ruifanha.clinicawisestart.repository.DentistaRepository;
import com.ruifanha.clinicawisestart.repository.EspecialidadeRepository;

// Service criado para concentrar regras de negocio relacionadas aos dentistas.
@Service
public class DentistaService {

	private final DentistaRepository dentistaRepository;
	private final DentistaEspecialidadeRepository dentistaEspecialidadeRepository;
	private final EspecialidadeRepository especialidadeRepository;

	public DentistaService(
		DentistaRepository dentistaRepository,
		DentistaEspecialidadeRepository dentistaEspecialidadeRepository,
		EspecialidadeRepository especialidadeRepository
	) {
		this.dentistaRepository = dentistaRepository;
		this.dentistaEspecialidadeRepository = dentistaEspecialidadeRepository;
		this.especialidadeRepository = especialidadeRepository;
	}

	@Transactional
	public Dentista criar(DentistaRequest dentistaRequest) {
		Dentista dentista = new Dentista();
		aplicarDados(dentista, dentistaRequest);
		validarDuplicidadeEmail(dentista);
		validarDuplicidadeCpf(dentista);
		validarDuplicidadeCro(dentista);
		Dentista salvo = dentistaRepository.save(dentista);
		sincronizarEspecialidades(salvo, resolverIds(dentistaRequest));
		return dentistaRepository.findByIdComEspecialidades(salvo.getId()).orElse(salvo);
	}

	@Transactional
	public Dentista atualizar(Long id, DentistaRequest dentistaRequest) {
		Dentista dentista = buscarPorId(id);
		aplicarDados(dentista, dentistaRequest);
		validarDuplicidadeEmail(dentista);
		validarDuplicidadeCpf(dentista);
		validarDuplicidadeCro(dentista);
		Dentista salvo = dentistaRepository.save(dentista);
		sincronizarEspecialidades(salvo, resolverIds(dentistaRequest));
		return dentistaRepository.findByIdComEspecialidades(salvo.getId()).orElse(salvo);
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
		return dentistaRepository.findAllComEspecialidades();
	}

	@Transactional(readOnly = true)
	public List<Dentista> listarTodos(Boolean ativo) {
		if (ativo != null) {
			return dentistaRepository.findByAtivoComEspecialidades(ativo);
		}
		return listarTodos();
	}

	@Transactional(readOnly = true)
	public List<Dentista> listarAtivos() {
		return dentistaRepository.findByAtivoComEspecialidades(Boolean.TRUE);
	}

	@Transactional(readOnly = true)
	public Dentista buscarPorId(Long id) {
		return dentistaRepository.findByIdComEspecialidades(id)
			.orElseThrow(() -> new IllegalArgumentException("Dentista nao encontrado."));
	}

	@Transactional
	public void excluir(Long id) {
		Dentista dentista = buscarPorId(id);
		dentistaRepository.delete(dentista);
	}

	private void aplicarDados(Dentista dentista, DentistaRequest dentistaRequest) {
		if (dentistaRequest == null) {
			throw new IllegalArgumentException("Dados do dentista sao obrigatorios.");
		}

		dentista.setNome(dentistaRequest.nome());
		dentista.setCpf(dentistaRequest.cpf());
		dentista.setEmail(dentistaRequest.email());
		dentista.setCro(dentistaRequest.cro());
		dentista.setAtivo(dentistaRequest.ativo());
	}

	private List<Long> resolverIds(DentistaRequest req) {
		if (req.especialidadeIds() != null && !req.especialidadeIds().isEmpty()) {
			return req.especialidadeIds();
		}
		if (req.especialidadeId() != null) {
			return List.of(req.especialidadeId());
		}
		return List.of();
	}

	private void sincronizarEspecialidades(Dentista dentista, List<Long> ids) {
		dentistaEspecialidadeRepository.deleteByDentista(dentista);
		dentistaEspecialidadeRepository.flush();
		if (ids.isEmpty()) {
			return;
		}
		List<DentistaEspecialidade> novas = new ArrayList<>();
		for (Long espId : ids) {
			Especialidade especialidade = especialidadeRepository.findById(espId)
				.orElseThrow(() -> new IllegalArgumentException("Especialidade nao encontrada: " + espId));
			DentistaEspecialidade de = new DentistaEspecialidade();
			de.setDentista(dentista);
			de.setEspecialidade(especialidade);
			novas.add(de);
		}
		dentistaEspecialidadeRepository.saveAll(novas);
	}

	private void validarDuplicidadeEmail(Dentista dentista) {
		dentistaRepository.findByEmail(dentista.getEmail())
			.filter(dentistaExistente -> !dentistaExistente.getId().equals(dentista.getId()))
			.ifPresent(dentistaExistente -> {
				throw new IllegalArgumentException("Ja existe dentista cadastrado com este email.");
			});
	}

	private void validarDuplicidadeCpf(Dentista dentista) {
		dentistaRepository.findByCpf(dentista.getCpf())
			.filter(dentistaExistente -> !dentistaExistente.getId().equals(dentista.getId()))
			.ifPresent(dentistaExistente -> {
				throw new IllegalArgumentException("Ja existe dentista cadastrado com este CPF.");
			});
	}

	private void validarDuplicidadeCro(Dentista dentista) {
		dentistaRepository.findByCro(dentista.getCro())
			.filter(dentistaExistente -> !dentistaExistente.getId().equals(dentista.getId()))
			.ifPresent(dentistaExistente -> {
				throw new IllegalArgumentException("Ja existe dentista cadastrado com este CRO.");
			});
	}
}
