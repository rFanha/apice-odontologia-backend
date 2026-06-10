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

import com.ruifanha.clinicawisestart.dto.dentista.DentistaRequest;
import com.ruifanha.clinicawisestart.dto.dentista.DentistaResponse;
import com.ruifanha.clinicawisestart.service.DentistaService;

// Controller criado para expor o CRUD de dentistas.
@RestController
@RequestMapping("/dentistas")
public class DentistaController {

	private final DentistaService dentistaService;

	public DentistaController(DentistaService dentistaService) {
		this.dentistaService = dentistaService;
	}

	@GetMapping
	public List<DentistaResponse> listarTodos(@RequestParam(required = false) Boolean ativo) {
		return dentistaService.listarTodos(ativo)
			.stream()
			.map(DentistaResponse::fromEntity)
			.toList();
	}

	@GetMapping("/{id}")
	public DentistaResponse buscarPorId(@PathVariable Long id) {
		try {
			return DentistaResponse.fromEntity(dentistaService.buscarPorId(id));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DentistaResponse criar(@RequestBody DentistaRequest dentistaRequest) {
		try {
			return DentistaResponse.fromEntity(dentistaService.criar(dentistaRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@PutMapping("/{id}")
	public DentistaResponse atualizar(
		@PathVariable Long id,
		@RequestBody DentistaRequest dentistaRequest
	) {
		try {
			return DentistaResponse.fromEntity(dentistaService.atualizar(id, dentistaRequest));
		} catch (IllegalArgumentException exception) {
			throw converterErro(exception);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		try {
			dentistaService.excluir(id);
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
