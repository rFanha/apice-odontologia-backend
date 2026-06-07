-- Cria a estrutura inicial do banco do sistema de consultas odontologicas.

-- Cria a tabela de usuarios com dados de acesso, perfil e controle de ativo.
CREATE TABLE IF NOT EXISTS usuarios (
	id BIGSERIAL PRIMARY KEY,
	nome VARCHAR(255) NOT NULL,
	cpf VARCHAR(14) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	senha VARCHAR(255) NOT NULL,
	data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	ultimo_login TIMESTAMP,
	perfil VARCHAR(30) NOT NULL,
	ativo BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT ck_usuarios_perfil CHECK (perfil IN ('ADMIN', 'DENTISTA'))
);

-- Cria a tabela de pacientes com dados pessoais unicos e telefone de contato.
CREATE TABLE IF NOT EXISTS pacientes (
	id BIGSERIAL PRIMARY KEY,
	nome VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	cpf VARCHAR(14) NOT NULL UNIQUE,
	data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	telefone VARCHAR(30)
);

-- Cria a tabela de dentistas com dados profissionais, CRO e controle de ativo.
CREATE TABLE IF NOT EXISTS dentistas (
	id BIGSERIAL PRIMARY KEY,
	nome VARCHAR(255) NOT NULL,
	cpf VARCHAR(14) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	cro VARCHAR(30) NOT NULL,
	data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Cria a tabela de especialidades odontologicas disponiveis para os dentistas.
CREATE TABLE IF NOT EXISTS especialidades (
	id BIGSERIAL PRIMARY KEY,
	nome VARCHAR(255) NOT NULL
);

-- Cria a tabela intermediaria que liga dentistas e especialidades sem duplicidade.
CREATE TABLE IF NOT EXISTS dentista_especialidade (
	id BIGSERIAL PRIMARY KEY,
	id_dentista BIGINT NOT NULL,
	id_especialidade BIGINT NOT NULL,
	CONSTRAINT fk_dentista_especialidade_dentista
		FOREIGN KEY (id_dentista) REFERENCES dentistas (id),
	CONSTRAINT fk_dentista_especialidade_especialidade
		FOREIGN KEY (id_especialidade) REFERENCES especialidades (id),
	CONSTRAINT uk_dentista_especialidade UNIQUE (id_dentista, id_especialidade)
);

-- Cria a tabela de consultas com agenda, status e vinculos com paciente, dentista e usuario.
CREATE TABLE IF NOT EXISTS consultas (
	id BIGSERIAL PRIMARY KEY,
	id_paciente BIGINT NOT NULL,
	id_dentista BIGINT NOT NULL,
	id_usuario BIGINT NOT NULL,
	descricao TEXT NOT NULL,
	motivo_cancelamento TEXT,
	data_inicio TIMESTAMP NOT NULL,
	data_fim TIMESTAMP NOT NULL,
	data_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	status VARCHAR(30) NOT NULL DEFAULT 'AGENDADA',
	CONSTRAINT fk_consultas_paciente
		FOREIGN KEY (id_paciente) REFERENCES pacientes (id),
	CONSTRAINT fk_consultas_dentista
		FOREIGN KEY (id_dentista) REFERENCES dentistas (id),
	CONSTRAINT fk_consultas_usuario
		FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
	CONSTRAINT ck_consultas_status
		CHECK (status IN ('AGENDADA', 'CANCELADA', 'FINALIZADA')),
	CONSTRAINT ck_consultas_periodo
		CHECK (data_fim > data_inicio),
	CONSTRAINT ck_consultas_motivo_cancelamento
		CHECK (status <> 'CANCELADA' OR motivo_cancelamento IS NOT NULL)
);
