# EventAPI

API REST para gestão de eventos, com convidados, fornecedores e itens de custo, além de
auditoria versionada dos dados do evento.

Construída com **Java 17** e **Spring Boot 3.4**, segue uma arquitetura em camadas
(Controller → Service → Repository) com DTOs, validação de entrada, migrations versionadas
e cobertura de testes automatizados.

## Tecnologias

- **Java 17** + **Spring Boot 3.4.4**
- **Spring Web**, **Spring Data JPA**, **Bean Validation**
- **PostgreSQL** como banco de dados
- **Flyway** para versionamento de schema
- **Hibernate Envers** para auditoria do histórico
- **MapStruct** para mapeamento Entity ↔ DTO, **Lombok**
- **springdoc-openapi** (Swagger UI)
- **Testcontainers** + **JUnit 5** para testes
- **Docker** (multi-stage) e **docker-compose**
- **GitHub Actions** para CI

## Arquitetura

```
com.project.EventAPI
├── audit/         Configuração de auditoria (AuditorAware, revisão, listener)
├── config/        Configurações (OpenAPI)
├── controller/    Endpoints REST
├── dto/           Records de request/response/update + mappers MapStruct
├── entity/        Entidades JPA
├── enums/         Enumerações de domínio
├── exception/     Exceções e handler global de erros
├── repository/    Repositórios Spring Data
└── service/       Regras de negócio
```

Entidades principais: `Evento`, `Convidado`, `Fornecedor`, `ItemEvento`, `ListaConvidado`
e as tabelas de auditoria `*_aud` + `revinfo`.

## Requisitos

- JDK 17+
- Maven 3.9+ (ou o próprio `mvn` do sistema)
- Docker e Docker Compose (opcional, para execução em container)
- PostgreSQL 16 (opcional, se rodar localmente sem Docker)

## Como executar

### 1. Com Docker Compose (recomendado)

Sobe o banco PostgreSQL e a API já configurados para produção.

```bash
cp .env.example .env        # ajuste usuário/senha se desejar
docker compose up --build
```

A API fica disponível em `http://localhost:8080`.

### 2. Localmente

Suba um PostgreSQL (ex.: `docker run -e POSTGRES_DB=eventapi -e POSTGRES_PASSWORD=root -p 5432:5432 postgres:16-alpine`)
e rode a aplicação:

```bash
cd EventAPI
mvn spring-boot:run
```

O profile padrão é `dev`, apontando para `jdbc:postgresql://localhost:5432/eventapi`
com usuário `postgres` e senha `root` (configurável por variáveis de ambiente).

## Configuração

| Propriedade | Variável de ambiente | Padrão |
| --- | --- | --- |
| Profile ativo | `SPRING_PROFILES_ACTIVE` | `dev` |
| URL do banco | `DB_URL` | `jdbc:postgresql://localhost:5432/eventapi` |
| Usuário do banco | `DB_USERNAME` | `postgres` |
| Senha do banco | `DB_PASSWORD` | `root` |

Profiles:

- `dev`: valores padrão para desenvolvimento e log SQL detalhado.
- `prod`: exige as variáveis de ambiente `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.
- `test`: usado pelos testes com Testcontainers.

O schema é gerenciado exclusivamente pelo **Flyway** (`ddl-auto=validate`), com
`baseline-on-migrate` habilitado para bancos já existentes.

## Documentação da API

Com a aplicação rodando:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Endpoints

**Eventos**

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/api/events` | Lista eventos (paginado) |
| GET | `/api/events/{id}` | Busca evento por ID |
| POST | `/api/events` | Cria evento |
| POST | `/api/events/lote` | Cria eventos em lote |
| PUT | `/api/events/{id}` | Atualiza evento |
| DELETE | `/api/events/{id}` | Remove evento |

**Histórico (auditoria)**

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/api/events/{id}/historico` | Lista revisões do evento |
| GET | `/api/events/{id}/historico/periodo?inicio=&fim=` | Revisões em um período |
| GET | `/api/events/{id}/historico/{revisao}` | Busca uma revisão específica |

O filtro por período exige timestamps ISO 8601 com offset explícito, por exemplo
`inicio=2026-10-20T09:00:00-03:00&fim=2026-10-20T18:00:00-03:00`. O histórico permanece
consultável após a exclusão do evento e identifica operações de criação, alteração e exclusão.
O endpoint apresenta revisões da entidade evento; alterações em convidados, fornecedores e itens
não são consolidadas nessa resposta.

**Sub-recursos do evento** (mesmo padrão de listagem, busca, criação, atualização e remoção):

- `/api/events/{eventoId}/convidados`
- `/api/events/{eventoId}/fornecedores`
- `/api/events/{eventoId}/itens`

As listagens aceitam paginação via query params `page`, `size` e `sort`.

### Exemplos

Criar um evento:

```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Conferência de Tecnologia",
    "descricaoEvento": "Evento anual da empresa",
    "previsaoInicio": "2026-08-01T09:00:00",
    "previsaoFim": "2026-08-03T18:00:00",
    "status": "AGENDADO",
    "endereco": {
      "uf": "SP",
      "cidade": "São Paulo",
      "rua": "Av. Paulista",
      "numero": "1000",
      "cep": "01310-100"
    }
  }'
```

Adicionar um convidado ao evento `1`:

```bash
curl -X POST http://localhost:8080/api/events/1/convidados \
  -H "Content-Type: application/json" \
  -d '{"nome": "Maria Silva", "email": "maria@example.com", "telefone": "11999999999", "confirmado": true}'
```

Consultar o histórico do evento `1`:

```bash
curl http://localhost:8080/api/events/1/historico
```

## Tratamento de erros

O `EventGlobalExceptionHandler` padroniza as respostas de erro:

| Status | Situação |
| --- | --- |
| `400 Bad Request` | Dados inválidos (Bean Validation) ou período de datas inconsistente |
| `404 Not Found` | Recurso não encontrado |
| `409 Conflict` | Violação de integridade de dados |

Eventos com convidados, fornecedores ou itens vinculados não podem ser excluídos; remova os
dependentes antes. Atualizações concorrentes do mesmo evento também retornam `409`.

## Banco de dados e migrations

As migrations ficam em `EventAPI/src/main/resources/db/migration`:

- `V1__init_schema.sql` — schema inicial.
- `V2__add_version_and_audit_columns.sql` — colunas de lock otimista e auditoria de usuário.
- `V3__add_envers_validity_columns.sql` — colunas de validade exigidas pelo `ValidityAuditStrategy`.

A auditoria usa a estratégia de validade (`revend`/`revend_tstmp`) e o listener
`RevisaoListener`, que registra o usuário responsável de cada revisão.

## Testes

Os testes de integração sobem um PostgreSQL real via **Testcontainers**, portanto é
necessário ter o **Docker** em execução.

```bash
cd EventAPI
mvn test      # apenas testes
mvn verify    # testes + empacotamento
```

## Build e Docker

Build da imagem (multi-stage, runtime non-root):

```bash
cd EventAPI
docker build -t evento-api .
```

O Dockerfile usa `maven:3.9-eclipse-temurin-17` no estágio de build e
`eclipse-temurin:17-jre` em runtime, executando a aplicação com usuário não privilegiado.

## CI

O workflow em `.github/workflows/ci.yml` roda `mvn -B verify` a cada push na `main` e
em pull requests.
