package com.ruifanha.clinicawisestart.dto.relatorio;

// Dados resumidos para cards de dashboard e relatorios iniciais.
public record DashboardResponse(
	long totalConsultas,
	long consultasAgendadas,
	long consultasCanceladas,
	long consultasFinalizadas,
	long totalPacientes,
	long totalDentistas,
	long totalEspecialidades
) {
}
