package com.ruifanha.clinicawisestart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruifanha.clinicawisestart.domain.consulta.Consulta;
import com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta;
import com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario;
import com.ruifanha.clinicawisestart.domain.usuario.Usuario;
import com.ruifanha.clinicawisestart.dto.consulta.ConsultaRequest;
import com.ruifanha.clinicawisestart.repository.ConsultaRepository;
import com.ruifanha.clinicawisestart.repository.DentistaRepository;
import com.ruifanha.clinicawisestart.repository.PacienteRepository;
import com.ruifanha.clinicawisestart.repository.UsuarioRepository;

// Service criado para concentrar as regras de negocio antes de salvar consultas.
@Service
public class ConsultaService {

	private final ConsultaRepository consultaRepository;
	private final PacienteRepository pacienteRepository;
	private final DentistaRepository dentistaRepository;
	private final UsuarioRepository usuarioRepository;

	public ConsultaService(
		ConsultaRepository consultaRepository,
		PacienteRepository pacienteRepository,
		DentistaRepository dentistaRepository,
		UsuarioRepository usuarioRepository
	) {
		this.consultaRepository = consultaRepository;
		this.pacienteRepository = pacienteRepository;
		this.dentistaRepository = dentistaRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public Consulta criar(Usuario usuarioLogado, ConsultaRequest consultaRequest) {
		Consulta consulta = new Consulta();
		aplicarDados(consulta, usuarioLogado, consultaRequest, true);
		return salvar(consulta);
	}

	@Transactional
	public Consulta atualizar(Usuario usuarioLogado, Long id, ConsultaRequest consultaRequest) {
		Consulta consulta = buscarPorId(id);
		aplicarDados(consulta, usuarioLogado, consultaRequest, false);
		return salvar(consulta);
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
	public List<Consulta> listar(Usuario usuarioLogado, Long dentistaId) {
		// Quando informado, filtra a agenda pelo dentista escolhido.
		if (dentistaId != null) {
			return listarPorDentista(dentistaId);
		}
		return listarTodasParaAdmin(usuarioLogado);
	}

	@Transactional(readOnly = true)
	public Consulta buscarPorId(Long id) {
		// Centraliza a busca por id para manter uma mensagem padronizada.
		return consultaRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Consulta nao encontrada."));
	}

	@Transactional(readOnly = true)
	public List<Consulta> listarTodasParaAdmin(Usuario usuarioLogado) {
		// Permite visao completa das consultas apenas para usuarios ADMIN.
		if (usuarioLogado == null || !PerfilUsuario.ADMIN.equals(usuarioLogado.getPerfil())) {
			throw new IllegalArgumentException("Somente usuarios ADMIN podem visualizar todas as consultas.");
		}

		return consultaRepository.findAll();
	}

	@Transactional
	public void excluir(Long id) {
		// Confirma que a consulta existe antes de solicitar a exclusao.
		Consulta consulta = buscarPorId(id);
		consultaRepository.delete(consulta);
	}

	private void aplicarDados(
		Consulta consulta,
		Usuario usuarioLogado,
		ConsultaRequest consultaRequest,
		boolean novaConsulta
	) {
		// Copia os dados recebidos e resolve os relacionamentos por id.
		if (consultaRequest == null) {
			throw new IllegalArgumentException("Dados da consulta sao obrigatorios.");
		}
		if (usuarioLogado == null || usuarioLogado.getId() == null) {
			throw new IllegalArgumentException("Usuario autenticado e obrigatorio.");
		}
		if (consultaRequest.pacienteId() == null) {
			throw new IllegalArgumentException("Paciente e obrigatorio.");
		}
		if (consultaRequest.dentistaId() == null) {
			throw new IllegalArgumentException("Dentista e obrigatorio.");
		}

		consulta.setPaciente(pacienteRepository.findById(consultaRequest.pacienteId())
			.orElseThrow(() -> new IllegalArgumentException("Paciente nao encontrado.")));
		consulta.setDentista(dentistaRepository.findById(consultaRequest.dentistaId())
			.orElseThrow(() -> new IllegalArgumentException("Dentista nao encontrado.")));
		consulta.setUsuario(usuarioRepository.findById(usuarioLogado.getId())
			.orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado.")));
		consulta.setDescricao(consultaRequest.descricao());
		consulta.setMotivoCancelamento(consultaRequest.motivoCancelamento());
		consulta.setDataInicio(consultaRequest.dataInicio());
		consulta.setDataFim(consultaRequest.dataFim());

		if (consultaRequest.status() != null) {
			consulta.setStatus(consultaRequest.status());
		} else if (novaConsulta) {
			consulta.setStatus(StatusConsulta.AGENDADA);
		}
	}

	private void validarPeriodoConsulta(Consulta consulta) {
		// Garante que a consulta termine depois do horario de inicio.
		if (consulta.getDataInicio() == null || consulta.getDataFim() == null) {
			throw new IllegalArgumentException("Data inicial e data final da consulta sao obrigatorias.");
		}
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
