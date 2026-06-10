package com.ruifanha.clinicawisestart.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;
import com.ruifanha.clinicawisestart.dto.relatorio.DashboardResponse;
import com.ruifanha.clinicawisestart.repository.ConsultaRepository;
import com.ruifanha.clinicawisestart.repository.DentistaRepository;
import com.ruifanha.clinicawisestart.repository.EspecialidadeRepository;
import com.ruifanha.clinicawisestart.repository.PacienteRepository;

// Service criado para concentrar consultas de relatorios e dashboard.
@Service
public class RelatorioService {

	private final ConsultaRepository consultaRepository;
	private final PacienteRepository pacienteRepository;
	private final DentistaRepository dentistaRepository;
	private final EspecialidadeRepository especialidadeRepository;

	public RelatorioService(
		ConsultaRepository consultaRepository,
		PacienteRepository pacienteRepository,
		DentistaRepository dentistaRepository,
		EspecialidadeRepository especialidadeRepository
	) {
		this.consultaRepository = consultaRepository;
		this.pacienteRepository = pacienteRepository;
		this.dentistaRepository = dentistaRepository;
		this.especialidadeRepository = especialidadeRepository;
	}

	@Transactional(readOnly = true)
	public DashboardResponse buscarDashboard(
		Long usuarioId,
		Long pacienteId,
		Long especialidadeId,
		LocalDateTime dataInicio,
		LocalDateTime dataFim
	) {
		// Resume os principais indicadores, aplicando filtros informados.
		validarPeriodo(dataInicio, dataFim);

		return new DashboardResponse(
			consultaRepository.countRelatorio(usuarioId, pacienteId, especialidadeId, dataInicio, dataFim),
			consultaRepository.countRelatorioPorStatus(
				usuarioId,
				pacienteId,
				especialidadeId,
				dataInicio,
				dataFim,
				StatusConsulta.AGENDADA
			),
			consultaRepository.countRelatorioPorStatus(
				usuarioId,
				pacienteId,
				especialidadeId,
				dataInicio,
				dataFim,
				StatusConsulta.CANCELADA
			),
			consultaRepository.countRelatorioPorStatus(
				usuarioId,
				pacienteId,
				especialidadeId,
				dataInicio,
				dataFim,
				StatusConsulta.FINALIZADA
			),
			pacienteRepository.count(),
			dentistaRepository.count(),
			especialidadeRepository.count()
		);
	}

	private void validarPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
		// Garante que o periodo informado no relatorio seja coerente.
		if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
			throw new IllegalArgumentException("Data final do relatorio deve ser posterior a data inicial.");
		}
	}
}
