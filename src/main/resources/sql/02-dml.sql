-- Insere dados de exemplo para testes iniciais da API.

INSERT INTO usuarios (nome, cpf, email, senha, perfil, ativo)
VALUES
	('Administrador Apice', '00000000000', 'admin@apiceodontologia.com.br', '$2a$10$9x6klaIz1bURrB5kM7T9nOZB1RLpKXagL7s5LVRCz5c8aoM.fBd8e', 'ADMIN', TRUE),
	('Dra. Marina Lopes', '44444444444', 'marina.lopes@apiceodontologia.com.br', '$2a$10$9x6klaIz1bURrB5kM7T9nOZB1RLpKXagL7s5LVRCz5c8aoM.fBd8e', 'DENTISTA', TRUE)
ON CONFLICT (email) DO UPDATE
SET nome = EXCLUDED.nome,
	cpf = EXCLUDED.cpf,
	senha = EXCLUDED.senha,
	perfil = EXCLUDED.perfil,
	ativo = EXCLUDED.ativo;

INSERT INTO pacientes (nome, email, cpf, telefone)
VALUES
	('Carlos Henrique Souza', 'carlos.souza@email.com', '22222222222', '(11) 98888-1111'),
	('Ana Paula Ribeiro', 'ana.ribeiro@email.com', '33333333333', '(11) 97777-2222')
ON CONFLICT (cpf) DO NOTHING;

INSERT INTO dentistas (nome, cpf, email, cro, ativo)
VALUES
	('Dra. Marina Lopes', '44444444444', 'marina.dentista@apiceodontologia.com.br', 'CRO-SP-12345', TRUE),
	('Dr. Felipe Andrade', '55555555555', 'felipe.andrade@apiceodontologia.com.br', 'CRO-SP-67890', TRUE)
ON CONFLICT (cpf) DO NOTHING;

INSERT INTO especialidades (nome)
SELECT 'Clinica Geral'
WHERE NOT EXISTS (SELECT 1 FROM especialidades WHERE nome = 'Clinica Geral');

INSERT INTO especialidades (nome)
SELECT 'Ortodontia'
WHERE NOT EXISTS (SELECT 1 FROM especialidades WHERE nome = 'Ortodontia');

INSERT INTO dentista_especialidade (id_dentista, id_especialidade)
SELECT d.id, e.id
FROM dentistas d
JOIN especialidades e ON e.nome = 'Clinica Geral'
WHERE d.cpf = '44444444444'
ON CONFLICT (id_dentista, id_especialidade) DO NOTHING;

INSERT INTO dentista_especialidade (id_dentista, id_especialidade)
SELECT d.id, e.id
FROM dentistas d
JOIN especialidades e ON e.nome = 'Ortodontia'
WHERE d.cpf = '55555555555'
ON CONFLICT (id_dentista, id_especialidade) DO NOTHING;

INSERT INTO consultas (
	id_paciente,
	id_dentista,
	id_usuario,
	descricao,
	data_inicio,
	data_fim,
	status
)
SELECT
	p.id,
	d.id,
	u.id,
	'Avaliacao inicial',
	TIMESTAMP '2026-06-10 09:00:00',
	TIMESTAMP '2026-06-10 10:00:00',
	'AGENDADA'
FROM pacientes p
JOIN dentistas d ON d.cpf = '44444444444'
JOIN usuarios u ON u.cpf = '00000000000'
WHERE p.cpf = '22222222222'
AND NOT EXISTS (
	SELECT 1
	FROM consultas c
	WHERE c.id_dentista = d.id
	AND c.data_inicio = TIMESTAMP '2026-06-10 09:00:00'
);
