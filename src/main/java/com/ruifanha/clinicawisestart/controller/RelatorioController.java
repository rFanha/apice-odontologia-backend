package com.ruifanha.clinicawisestart.controller;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruifanha.clinicawisestart.dto.relatorio.DashboardResponse;
import com.ruifanha.clinicawisestart.service.RelatorioService;

// Controller criado para expor relatorios e indicadores do dashboard.
@RestController
@RequestMapping("/relatorios")
@PreAuthorize("hasRole('ADMIN')")
public class RelatorioController {

	private final RelatorioService relatorioService;

	public RelatorioController(RelatorioService relatorioService) {
		this.relatorioService = relatorioService;
	}

	@GetMapping("/dashboard")
	public DashboardResponse buscarDashboard(
		@RequestParam(required = false) Long usuarioId,
		@RequestParam(required = false) Long pacienteId,
		@RequestParam(required = false) Long especialidadeId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
	) {
		return relatorioService.buscarDashboard(usuarioId, pacienteId, especialidadeId, dataInicio, dataFim);
	}
}
