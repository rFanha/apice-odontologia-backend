package com.ruifanha.clinicawisestart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.consulta.Consulta;
import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;
import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;
import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
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
		validarPeriodoConsulta(consulta);
		validarDataFutura(consulta);
		validarMotivoCancelamento(consulta);
		validarConflitoHorario(consulta);
		return consultaRepository.save(consulta);
	}

	@Transactional(readOnly = true)
	public List<Consulta> listarPorDentista(Long dentistaId) {
		// Garante que o dentista visualize apenas consultas ligadas ao seu cadastro.
		return consultaRepository.findByDentistaId(dentistaId);
	}

	@Transactional(readOnly = true)
	public List<Consulta> listarTodasParaAdmin(Usuario usuarioLogado) {
		// Permite visao completa das consultas apenas para usuarios ADMIN.
		if (usuarioLogado == null || !PerfilUsuario.ADMIN.equals(usuarioLogado.getPerfil())) {
			throw new IllegalArgumentException("Somente usuarios ADMIN podem visualizar todas as consultas.");
		}

		return consultaRepository.findAll();
	}

	private void validarPeriodoConsulta(Consulta consulta) {
		// Garante que a consulta termine depois do horario de inicio.
		if (!consulta.getDataFim().isAfter(consulta.getDataInicio())) {
			throw new IllegalArgumentException("A data final da consulta deve ser posterior a data inicial.");
		}
	}

	private void validarDataFutura(Consulta consulta) {
		// Impede marcar consulta com inicio em data ou horario que ja passou.
		if (consulta.getDataInicio().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Nao e permitido agendar consulta em data passada.");
		}
	}

	private void validarMotivoCancelamento(Consulta consulta) {
		// Exige justificativa quando a consulta esta sendo cancelada.
		boolean consultaCancelada = StatusConsulta.CANCELADA.equals(consulta.getStatus());
		boolean motivoVazio = consulta.getMotivoCancelamento() == null
			|| consulta.getMotivoCancelamento().isBlank();

		if (consultaCancelada && motivoVazio) {
			throw new IllegalArgumentException("Informe o motivo para cancelar a consulta.");
		}
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
