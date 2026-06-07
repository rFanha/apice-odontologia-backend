# Imports usados

`org.springframework.boot.SpringApplication`: inicia a aplicacao Spring Boot pelo metodo `main`.

`org.springframework.boot.autoconfigure.SpringBootApplication`: marca a classe principal e habilita a configuracao automatica do Spring Boot.

`org.springframework.data.jpa.repository.JpaRepository`: fornece operacoes prontas de banco, como salvar, buscar e excluir entidades.

`org.springframework.data.jpa.repository.Query`: permite escrever uma consulta JPQL personalizada no repository.

`org.springframework.data.repository.query.Param`: liga parametros Java aos nomes usados dentro da query.

`org.springframework.stereotype.Service`: marca uma classe como service para o Spring gerenciar como componente de regra de negocio.

`org.springframework.transaction.annotation.Transactional`: executa o metodo dentro de uma transacao de banco.

`java.time.LocalDateTime`: representa data e hora sem fuso horario, usada em campos como criacao, login e horarios de consulta.

`java.util.ArrayList`: cria listas mutaveis usadas para iniciar colecoes de relacionamentos.

`java.util.List`: define colecoes de objetos, como especialidades de um dentista.

`com.ruifanha.clinicawisestart.domain.consulta.StatusConsulta`: enum interno que define os status possiveis de uma consulta.

`com.ruifanha.clinicawisestart.domain.consulta.Consulta`: entidade usada para salvar e consultar agendamentos odontologicos.

`com.ruifanha.clinicawisestart.domain.dentista.Dentista`: entidade usada para relacionar consultas e especialidades com dentistas.

`com.ruifanha.clinicawisestart.domain.dentista.DentistaEspecialidade`: entidade intermediaria usada no relacionamento entre dentistas e especialidades.

`com.ruifanha.clinicawisestart.domain.especialidade.Especialidade`: entidade usada para relacionar dentistas com suas especialidades.

`com.ruifanha.clinicawisestart.domain.paciente.Paciente`: entidade usada para relacionar consultas com pacientes.

`com.ruifanha.clinicawisestart.domain.usuario.PerfilUsuario`: enum usado para validar permissoes de acesso por perfil.

`com.ruifanha.clinicawisestart.domain.usuario.Usuario`: entidade usada para registrar qual usuario marcou uma consulta.

`com.ruifanha.clinicawisestart.repository.ConsultaRepository`: repository usado pelo service para acessar dados de consultas.

`jakarta.persistence.Column`: configura detalhes de uma coluna, como obrigatoriedade, nome e unicidade.

`jakarta.persistence.Entity`: indica que a classe Java sera mapeada como tabela no banco.

`jakarta.persistence.EnumType`: define como um enum sera persistido no banco.

`jakarta.persistence.Enumerated`: configura o armazenamento de campos enum, como perfil e status.

`jakarta.persistence.FetchType`: define como relacionamentos sao carregados, por exemplo de forma tardia com `LAZY`.

`jakarta.persistence.GeneratedValue`: configura a geracao automatica do valor da chave primaria.

`jakarta.persistence.GenerationType`: define a estrategia de geracao do id, como `IDENTITY`.

`jakarta.persistence.Id`: marca o campo que representa a chave primaria da tabela.

`jakarta.persistence.JoinColumn`: configura a coluna de chave estrangeira em um relacionamento.

`jakarta.persistence.ManyToOne`: representa relacionamento muitos-para-um, como varias consultas para um paciente.

`jakarta.persistence.OneToMany`: representa relacionamento um-para-muitos, como um dentista para varios vinculos de especialidade.

`jakarta.persistence.PrePersist`: executa um metodo antes do primeiro salvamento da entidade.

`jakarta.persistence.Table`: define o nome da tabela e configuracoes da tabela no banco.

`jakarta.persistence.UniqueConstraint`: cria uma restricao de unicidade composta na tabela.
