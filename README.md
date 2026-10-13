# EventAPI

API REST para gerenciamento de eventos, convidados, fornecedores e itens de custo, com auditoria via Hibernate Envers.

## Stack

- Java 17, Spring Boot 3.4 (Web, Data JPA, Validation, HATEOAS)
- PostgreSQL + Flyway (migrations)
- Hibernate Envers (trilha de auditoria)
- MapStruct (mapeamento DTO <-> entidade)
- springdoc-openapi (Swagger UI)
- Testcontainers (testes de integração)

## Estrutura do projeto

O projeto Maven está em `EventAPI/`. Na raiz ficam `docker-compose.yml` e `.env.example`.

## Executando localmente

Pré-requisitos: JDK 17, Maven 3.9+ e um PostgreSQL acessível.

Variáveis de ambiente (profile `dev` possui defaults locais):

```bash
export DB_URL="jdbc:postgresql://localhost:5432/eventapi?reWriteBatchedInserts=true"
export DB_USERNAME="postgres"
export DB_PASSWORD="postgres"
```

```bash
cd EventAPI
./mvnw spring-boot:run
```

O Flyway aplica o schema automaticamente na inicialização.

## Docker

```bash
cp .env.example .env
docker compose up --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Testes

Os testes de integração sobem um PostgreSQL via Testcontainers (Docker precisa estar ativo).

```bash
cd EventAPI
./mvnw verify
```

## Endpoints principais

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/api/events` | Lista eventos (paginado) |
| POST | `/api/events` | Cria evento |
| GET | `/api/events/{id}` | Busca evento |
| PUT | `/api/events/{id}` | Atualiza evento |
| DELETE | `/api/events/{id}` | Remove evento |
| GET | `/api/events/{id}/historico` | Histórico de revisões (Envers) |
| GET | `/api/events/{eventoId}/convidados` | Lista convidados |
| GET | `/api/events/{eventoId}/fornecedores` | Lista fornecedores |
| GET | `/api/events/{eventoId}/itens` | Lista itens de custo |

## Perfis

- `dev`: defaults para desenvolvimento local (logs Hibernate em DEBUG).
- `prod`: exige `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` via ambiente.
- `test`: usado pelos testes com Testcontainers.
