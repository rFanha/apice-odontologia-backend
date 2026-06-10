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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.dto.paciente.PacienteRequest;
import com.ruifanha.clinicawisestart.dto.paciente.PacienteResponse;
import com.ruifanha.clinicawisestart.service.PacienteService;

// Controller criado para expor o CRUD de pacientes.
@RestController
@RequestMapping("/pacientes")
public class PacienteController {

	private final PacienteService pacienteService;

	public PacienteController(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	@GetMapping
	public List<PacienteResponse> listarTodos() {
		return pacienteService.listarTodos()
			.stream()
			.map(PacienteResponse::fromEntity)
			.toList();
	}

	@GetMapping("/{id}")
	public PacienteResponse buscarPorId(@PathVariable Long id) {
		try {
			return PacienteResponse.fromEntity(pacienteService.buscarPorId(id));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PacienteResponse criar(@RequestBody PacienteRequest pacienteRequest) {
		try {
			return PacienteResponse.fromEntity(pacienteService.criar(pacienteRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PutMapping("/{id}")
	public PacienteResponse atualizar(
		@PathVariable Long id,
		@RequestBody PacienteRequest pacienteRequest
	) {
		try {
			return PacienteResponse.fromEntity(pacienteService.atualizar(id, pacienteRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		try {
			pacienteService.excluir(id);
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	private ResponseStatusException converterErro(IllegalArgumentException exception) {
		// Converte erros de regra de negocio para respostas HTTP simples.
		if (exception.getMessage().contains("nao encontrado")) {
			return new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}
}
