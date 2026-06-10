package com.ruifanha.clinicawisestart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.dto.especialidade.EspecialidadeRequest;
import com.ruifanha.clinicawisestart.dto.especialidade.EspecialidadeResponse;
import com.ruifanha.clinicawisestart.service.EspecialidadeService;

// Controller criado para expor o CRUD de especialidades.
@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

	private final EspecialidadeService especialidadeService;

	public EspecialidadeController(EspecialidadeService especialidadeService) {
		this.especialidadeService = especialidadeService;
	}

	@GetMapping
	public List<EspecialidadeResponse> listar(@RequestParam(required = false) String nome) {
		return especialidadeService.listar(nome)
			.stream()
			.map(EspecialidadeResponse::fromEntity)
			.toList();
	}

	@GetMapping("/{id}")
	public EspecialidadeResponse buscarPorId(@PathVariable Long id) {
		try {
			return EspecialidadeResponse.fromEntity(especialidadeService.buscarPorId(id));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EspecialidadeResponse criar(@RequestBody EspecialidadeRequest especialidadeRequest) {
		try {
			return EspecialidadeResponse.fromEntity(especialidadeService.criar(especialidadeRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PutMapping("/{id}")
	public EspecialidadeResponse atualizar(
		@PathVariable Long id,
		@RequestBody EspecialidadeRequest especialidadeRequest
	) {
		try {
			return EspecialidadeResponse.fromEntity(especialidadeService.atualizar(id, especialidadeRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		try {
			especialidadeService.excluir(id);
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	private ResponseStatusException converterErro(IllegalArgumentException exception) {
		// Converte erros de regra de negocio para respostas HTTP simples.
		if (exception.getMessage().contains("nao encontrada")) {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}
}
