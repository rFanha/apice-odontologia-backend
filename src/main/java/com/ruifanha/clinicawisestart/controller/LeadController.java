package com.ruifanha.clinicawisestart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ruifanha.clinicawisestart.dto.lead.LeadRequest;
import com.ruifanha.clinicawisestart.dto.lead.LeadResponse;
import com.ruifanha.clinicawisestart.service.LeadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/leads")
public class LeadController {

	private final LeadService leadService;

	public LeadController(LeadService leadService) {
		this.leadService = leadService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LeadResponse criar(@Valid @RequestBody LeadRequest request) {
		try {
			return LeadResponse.fromEntity(leadService.criar(request));
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@GetMapping
	public List<LeadResponse> listar(@RequestParam(required = false) Boolean lido) {
		if (lido != null && !lido) {
			return leadService.listarNaoLidos().stream().map(LeadResponse::fromEntity).toList();
		}
		return leadService.listarTodos().stream().map(LeadResponse::fromEntity).toList();
	}

	@PatchMapping("/{id}/lido")
	public LeadResponse marcarComoLido(@PathVariable Long id) {
		try {
			return LeadResponse.fromEntity(leadService.marcarComoLido(id));
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
	}
}
