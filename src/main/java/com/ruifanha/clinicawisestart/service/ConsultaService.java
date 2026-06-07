package com.ruifanha.clinicawisestart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.consulta.Consulta;
import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;
import com.ruifanha.clinicawisestart.repository.ConsultaRepository;

// Service criado para concentrar as regras de negocio antes de salvar consultas.
@Service
public class ConsultaService {

	private final ConsultaRepository consultaRepository;

	public ConsultaService(ConsultaRepository consultaRepository) {
		this.consultaRepository = consultaRepository;
	}

	@Transactional
	public Consulta salvar(Consulta consulta) {
		validarConflitoHorario(consulta);
		return consultaRepository.save(consulta);
	}

	private void validarConflitoHorario(Consulta consulta) {
		// Bloqueia agendamento quando o dentista ja possui consulta no periodo.
		boolean existeConflito = consultaRepository.existsConflitoHorario(
			consulta.getId(),
			consulta.getDentista().getId(),
			consulta.getDataInicio(),
			consulta.getDataFim(),
			StatusConsulta.CANCELADA
		);

		if (existeConflito) {
			throw new IllegalArgumentException("Dentista ja possui consulta nesse horario.");
		}
	}
}
