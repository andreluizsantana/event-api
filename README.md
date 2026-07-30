# EventAPI

API REST para gerenciamento de eventos com suporte a auditoria completa via Hibernate Envers.

## Stack Tecnológica

- **Java 17**
- **Spring Boot 3.4.4**
- **Maven**
- **PostgreSQL**
- **Spring Data JPA + Hibernate**
- **Hibernate Envers** (auditoria)
- **MapStruct** (mapeamento DTO/Entidade)
- **Lombok**
- **Spring HATEOAS**
- **Bean Validation**
- **JWT** (dependências declaradas)

## Modelagem de Dados

| Entidade | Descrição |
|---|---|
| `Evento` | Evento principal com dados como título, descrição, datas, status e endereço embutido |
| `Convidado` | Convidados associados a um evento |
| `Fornecedor` | Fornecedores contratados para o evento |
| `ItemEvento` | Itens/despesas do evento |
| `ListaConvidado` | Relação N:N entre convidados e listas (com dados extras como confirmação de presente) |

Relacionamentos:
- `Evento` 1:N `Convidado`
- `Evento` 1:N `Fornecedor`
- `Evento` 1:N `ItemEvento`
- `Convidado` N:N `ListaConvidado` (tabela intermediária)

## Endpoints da API

### Eventos — `/api/events`

| Método | Caminho | Descrição | Status |
|---|---|---|---|
| `GET` | `/api/events` | Listar eventos (paginado) | 200 OK |
| `GET` | `/api/events/{id}` | Buscar evento por ID | 200 OK |
| `POST` | `/api/events` | Criar evento | 201 Created |
| `POST` | `/api/events/lote` | Criar eventos em lote | 201 Created |
| `PUT` | `/api/events/{id}` | Atualizar evento (parcial) | 200 OK |
| `DELETE` | `/api/events/{id}` | Excluir evento | 204 No Content |

### Auditoria — `/api/events`

| Método | Caminho | Descrição | Status |
|---|---|---|---|
| `GET` | `/api/events/{id}/historico` | Histórico de revisões do evento (paginado) | 200 OK |

## Pré-requisitos

- **Java 17+**
- **Maven 3.8+** (ou use o `mvnw` incluso)
- **PostgreSQL** rodando localmente

## Configuração do Banco de Dados

1. Crie o banco de dados no PostgreSQL:

```sql
CREATE DATABASE eventapi;
```

2. Configure as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eventapi?reWriteBatchedInserts=true
spring.datasource.username=postgres
spring.datasource.password=root
```

> As tabelas e sequências são criadas automaticamente pelo Hibernate (`ddl-auto=update`).

## Como Executar

```bash
./mvnw spring-boot:run
```

A aplicação inicia em `http://localhost:8080`.

## Como Testar

```bash
./mvnw test
```

## Estrutura do Projeto

```
src/main/java/com/project/EventAPI/
├── EventApiApplication.java          # Classe principal
├── audit/                            # Configuração de auditoria (Envers)
├── controller/                       # Controladores REST
├── dto/                              # DTOs de request, response, update e mapper
├── entity/                           # Entidades JPA
├── enums/                            # Enumeradores (Status, CategoriaCusto)
├── exception/                        # Tratamento global de exceções
├── repository/                       # Repositórios JPA
└── service/                          # Lógica de negócio
```

## Auditoria com Hibernate Envers

Todas as entidades são auditadas automaticamente. Cada operação de inserção, atualização ou exclusão gera uma revisão no banco. O histórico pode ser consultado via endpoint `/api/events/{id}/historico`.

Estratégia: `ValidityAuditStrategy` — tabelas de auditoria mantêm as colunas `REVSTART` e `REVEND` para consultas eficientes.

Dados das entidades são preservados mesmo após exclusão (`store_data_at_delete=true`).

## Licença

Este projeto está sob a licença MIT.
