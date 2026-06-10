package com.ruifanha.clinicawisestart.service;

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
	public DashboardResponse buscarDashboard() {
		// Resume os principais indicadores usados na tela inicial do dashboard.
		return new DashboardResponse(
			consultaRepository.count(),
			consultaRepository.countByStatus(StatusConsulta.AGENDADA),
			consultaRepository.countByStatus(StatusConsulta.CANCELADA),
			consultaRepository.countByStatus(StatusConsulta.FINALIZADA),
			pacienteRepository.count(),
			dentistaRepository.count(),
			especialidadeRepository.count()
		);
	}
}
