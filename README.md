PEDRO AUGUSTO MARTINS CERTO
556268

# Reserva de Salas (API)

API REST para **cadastro de salas** e **gestão de reservas**, com validações de regra de negócio como **intervalo de datas** e **bloqueio de conflitos** (mesma sala no mesmo horário).

## Tecnologias

- Java 21
- Spring Boot (Web)
- Spring Data JPA
- Bean Validation (Jakarta Validation)
- H2 Database (runtime)
- Maven
- Lombok
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito (testes)

## Como executar

### Pré-requisitos

- Java 21 instalado
- Maven Wrapper (já incluído no projeto)

### Rodando a aplicação

No Windows (cmd):

```bat
cd c:\Users\labsfiap\Downloads\reserva-equipamentos\reserva-equipamentos
mvnw.cmd spring-boot:run
```

A aplicação sobe, por padrão, em:

- `http://localhost:8080`

### Swagger (OpenAPI)

Após subir o projeto, acesse:

- `http://localhost:8080/swagger-ui/index.html`

> Obs.: dependendo da sua configuração de security, pode ser necessário liberar o Swagger no `SecurityConfig`.

## Endpoints disponíveis

### 🏢 Salas (`/api/sala`)

- `POST /api/sala` — Criar sala
- `GET /api/sala/all` — Listar salas
- `GET /api/sala/{id}` — Buscar sala por ID
- `PUT /api/sala/{id}` — Atualizar sala
- `DELETE /api/sala/{id}` — Remover sala

#### Body (criar / atualizar)

- `CriarSalaDto`: `{ "nome": "Sala 101" }`
- `AtualizarSalaDto`: `{ "nome": "Sala 102" }`

---

### 📅 Reservas (`/api/reserva`)

- `POST /api/reserva` — Criar reserva
- `GET /api/reserva` — Listar reservas
- `DELETE /api/reserva/{id}` — Cancelar reserva

#### Regras de negócio

- **Não permitir conflito de reservas**: não pode existir outra reserva **não cancelada** para a **mesma sala** com horário sobreposto.
- **Validar intervalo**: `finalReserva` deve ser **depois** de `inicioReserva`.

## Exemplos de requisições (Postman/Insomnia)

### 1) Criar Sala

**POST** `http://localhost:8080/api/sala`

Headers:
- `Content-Type: application/json`

Body:
```json
{
  "nome": "Sala 101"
}
```

Resposta (exemplo):
```json
{
  "id": 1,
  "nome": "Sala 101"
}
```

### 2) Listar Salas

**GET** `http://localhost:8080/api/sala/all`

Resposta (exemplo):
```json
[
  { "id": 1, "nome": "Sala 101" }
]
```

### 3) Criar Reserva

**POST** `http://localhost:8080/api/reserva`

Headers:
- `Content-Type: application/json`

Body:
```json
{
  "salaId": 1,
  "inicioReserva": "2026-05-01T10:00:00",
  "finalReserva": "2026-05-01T11:00:00",
  "nomeResponsavel": "João da Silva"
}
```

Resposta (exemplo):
```json
{
  "id": 1,
  "salaId": 1,
  "salaNome": "Sala 101",
  "inicioReserva": "2026-05-01T10:00:00",
  "finalReserva": "2026-05-01T11:00:00",
  "nomeResponsavel": "João da Silva",
  "cancelada": false
}
```

### 4) Erro de conflito de reserva (mesma sala e horário)

Se tentar criar outra reserva sobrepondo o horário:

**POST** `http://localhost:8080/api/reserva`

Body:
```json
{
  "salaId": 1,
  "inicioReserva": "2026-05-01T10:30:00",
  "finalReserva": "2026-05-01T11:30:00",
  "nomeResponsavel": "Maria"
}
```

Retorno esperado:
- HTTP `409 CONFLICT`
- Mensagem: `Já existe reserva para esta sala nesse horário`

### 5) Cancelar Reserva

**DELETE** `http://localhost:8080/api/reserva/1`

Retorno esperado:
- HTTP `200`/`204`

## Testes

Para rodar os testes:

```bat
cd c:\Users\labsfiap\Downloads\reserva-equipamentos\reserva-equipamentos
mvnw.cmd test
```

Inclui testes unitários cobrindo regras de negócio, por exemplo conflito de reserva em `ReservaServiceTest`.
