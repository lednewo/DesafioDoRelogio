# Desafio Relógio ⌚

API REST para cadastro, consulta, atualização e remoção de relógios. Possui filtros combinados por texto livre, tipo de movimento, material da caixa, tipo de vidro e faixa de resistência à água, além de paginação e múltiplas opções de ordenação. A resposta de cada relógio inclui campos calculados automaticamente: **etiqueta de resistência à água** e **pontuação de colecionador**.

---

## Tecnologias utilizadas

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.0.2 |
| Web | Spring Web MVC |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL |
| Validação | Jakarta Bean Validation (spring-boot-starter-validation) |
| Build | Maven (wrapper incluso) |
| Utilitários | Lombok |
| Dev experience | Spring Boot DevTools (hot reload) |

---

## Estrutura do projeto

```
src/main/java/dev/java10x/desafiorelogio/
├── DesafiorelogioApplication.java        # Classe principal (Spring Boot)
├── config/                               # Configurações extras
├── controller/
│   └── RelogioController.java            # Endpoints REST
├── domain/
│   ├── dto/
│   │   ├── CriarRelogioRequest.java      # DTO de criação (record + validações)
│   │   ├── AtualizarRelogioRequest.java  # DTO de atualização (record + validações)
│   │   ├── RelogioResponse.java          # DTO de resposta (com campos calculados)
│   │   ├── ResultDefaultResponse.java    # Resposta genérica de sucesso
│   │   └── mapper/
│   │       └── RelogioMapper.java        # Conversão Entity → Response
│   ├── entity/
│   │   ├── RelogioEntity.java            # Entidade JPA (tabela "relogio")
│   │   └── PaginationEntity.java         # Wrapper genérico de paginação
│   ├── enums/
│   │   ├── TipoMovimentoEnum.java        # quartz | automatic | manual
│   │   ├── MaterialCaixaEnum.java        # steel | titanium | resin | bronze | ceramic
│   │   └── TipoVidroEnum.java           # mineral | safira | acrilico
│   └── exception/
│       ├── ErrorApi.java                 # Payload padrão de erro
│       ├── GlobalExceptionHandler.java   # @ControllerAdvice
│       └── RelogioNotFoundException.java # 404 personalizado
├── repository/
│   └── RelogioRepository.java            # JpaRepository + JpaSpecificationExecutor
└── service/
    ├── RelogioService.java               # Regra de negócio
    ├── RelogioSpecs.java                 # Specifications dinâmicas (filtros JPA)
    └── OrdenacaoRelogiosEnum.java        # Mapeamento de opções de ordenação
```

---

## Pré-requisitos

- **Java 21** (ou superior)
- **PostgreSQL** rodando e acessível
- Criar o banco de dados (ex.: `CREATE DATABASE relogiobd;`)

---

## Configuração

Crie o arquivo `src/main/resources/application.properties` com as propriedades do seu banco:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/relogiobd
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

> **Nota:** o arquivo `application.properties` está no `.gitignore` por conter credenciais.

---

## Como executar

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/desafiorelogio.git
cd desafiorelogio

# Rode a aplicação (porta 8080)
./mvnw spring-boot:run
```

Para gerar o JAR executável:

```bash
./mvnw clean package -DskipTests
java -jar target/desafiorelogio-0.0.1-SNAPSHOT.jar
```

A aplicação ficará disponível em **http://localhost:8080**.

---

## Endpoints

Base path: **`/V1/api/relogios`**

### 1. Listar relógios (paginado + filtros)

```
GET /V1/api/relogios/getAllPaginated
```

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `termo` | string | não | Busca por marca, modelo ou referência (case-insensitive) |
| `marca` | string | não | Filtro exato por marca |
| `tipoMovimento` | string | não | `quartz`, `automatic` ou `manual` |
| `tipoVidro` | string | não | `mineral`, `safira` ou `acrilico` |
| `materialCaixa` | string | não | `steel`, `titanium`, `resin`, `bronze` ou `ceramic` |
| `resistenciaAguaMin` | int | não | Resistência mínima (metros) |
| `resistenciaAguaMax` | int | não | Resistência máxima (metros) |
| `ordenacao` | string | não | `newest` (padrão), `price_asc`, `price_desc`, `diameter_asc`, `ar_desc` |
| `page` | int | não | Página (default `0`) |
| `size` | int | não | Itens por página (default `10`) |

**Exemplo de resposta:**

```json
{
  "page": 0,
  "size": 10,
  "totalPages": 1,
  "items": [
    {
      "id": "a1b2c3d4-...",
      "marca": "Seiko",
      "modelo": "Prospex Diver",
      "referencia": "SPB143J1",
      "tipoMovimento": "automatico",
      "materialCaixa": "aço",
      "tipoVidro": "safira",
      "resistenciaAguaM": 200,
      "diametroMm": 40,
      "lugtoLugMm": 47,
      "espessuraMm": 13,
      "larguraMm": 20,
      "precoEmCentavos": 459900,
      "urlImagem": "https://exemplo.com/imagens/seiko-prospex.jpg",
      "etiquetaResistenciaAgua": "mergulho",
      "pontuacaoColecionador": 90
    }
  ]
}
```

---

### 2. Detalhar relógio por UUID

```
GET /V1/api/relogios/details?id={uuid}
```

Retorna um objeto `RelogioResponse` com todos os campos (incluindo os calculados).

---

### 3. Criar relógio

```
POST /V1/api/relogios/create
Content-Type: application/json
```

**Body:**

```json
{
  "marca": "Seiko",
  "modelo": "Prospex Diver",
  "referencia": "SPB143J1",
  "tipoMovimento": "automatic",
  "materialCaixa": "steel",
  "tipoVidro": "safira",
  "resistenciaAguaM": 200,
  "diametroMm": 40,
  "lugtoLugMm": 47,
  "espessuraMm": 13,
  "larguraMm": 20,
  "precoEmCentavos": 459900,
  "urlImagem": "https://exemplo.com/imagens/seiko-prospex.jpg"
}
```

**Validações aplicadas:**

| Campo | Regra |
|---|---|
| `marca` | obrigatório, máx. 80 caracteres |
| `modelo` | obrigatório, máx. 120 caracteres |
| `referencia` | obrigatório, máx. 80 caracteres |
| `tipoMovimento` | obrigatório (`quartz`, `automatic`, `manual`) |
| `materialCaixa` | obrigatório (`steel`, `titanium`, `resin`, `bronze`, `ceramic`) |
| `tipoVidro` | obrigatório (`mineral`, `safira`, `acrilico`) |
| `resistenciaAguaM` | mínimo 0 |
| `diametroMm` | mínimo 20 |
| `lugtoLugMm` | mínimo 20 |
| `espessuraMm` | mínimo 5 |
| `larguraMm` | mínimo 10 |
| `precoEmCentavos` | mínimo 1 |
| `urlImagem` | obrigatório, máx. 600 caracteres |

**Resposta (201):**

```json
{
  "message": "Relógio criado com sucesso",
  "statusCode": "201"
}
```

---

### 4. Atualizar relógio

```
PUT /V1/api/relogios/update?id={uuid}
Content-Type: application/json
```

Body e validações idênticos ao endpoint de criação.

**Resposta (200):**

```json
{
  "message": "Relógio atualizado com sucesso",
  "statusCode": "200"
}
```

---

## Campos calculados

Cada `RelogioResponse` inclui dois campos extras gerados automaticamente pelo `RelogioMapper`:

### Etiqueta de resistência à água

| Resistência (metros) | Etiqueta |
|---|---|
| < 50 | `respingos` |
| 50 – 99 | `uso_diario` |
| 100 – 199 | `natacao` |
| ≥ 200 | `mergulho` |

### Pontuação de colecionador

Soma de pontos calculada com base nas características do relógio:

| Critério | Pontos |
|---|---|
| Vidro safira | +25 |
| Movimento automático | +20 |
| Resistência ≥ 100 m | +15 |
| Resistência ≥ 200 m | +10 (cumulativo) |
| Caixa em titânio | +12 |
| Caixa em aço | +10 |
| Diâmetro entre 38–42 mm | +8 |

Pontuação máxima possível: **100 pontos**.

---

## Tratamento de erros

A API retorna um payload padronizado `ErrorApi` para todos os erros:

```json
{
  "timestamp": "2026-02-24T12:00:00Z",
  "status": 400,
  "erro": "Falha de validação",
  "message": "Um ou mais campos estão inválidos",
  "path": "/V1/api/relogios/create",
  "errors": [
    { "field": "marca", "message": "must not be blank" }
  ]
}
```

| Exceção | HTTP Status | Quando ocorre |
|---|---|---|
| `MethodArgumentNotValidException` | 400 | Campos do body não passam nas validações |
| `IllegalArgumentException` | 400 | Valor inválido para enum (ex.: `tipoMovimento=xyz`) |
| `RelogioNotFoundException` | 404 | UUID não encontrado no banco |

---

## Banco de dados

A entidade `RelogioEntity` é mapeada para a tabela **`relogio`** com os seguintes índices para performance:

- `IDX_RELOGIO_MARCA` — busca por marca
- `IDX_RELOGIO_CRIADO_EM` — ordenação por data de criação
- `IDX_RELOGIO_PRECO` — ordenação por preço

O campo `id` é um **UUID** gerado automaticamente via `@PrePersist`. O campo `criadoEm` é um `Instant` preenchido na inserção.

---

## Filtros dinâmicos (Specifications)

A classe `RelogioSpecs` usa **JPA Specifications** para compor filtros dinamicamente:

- **Busca textual** — `LIKE` case-insensitive em marca, modelo e referência
- **Tipo de movimento** — igualdade exata com enum
- **Tipo de vidro** — igualdade exata com enum
- **Material da caixa** — igualdade exata com enum
- **Resistência à água** — range `BETWEEN`, `>=` ou `<=`

Todos os filtros são opcionais e combináveis via `AND`.

---

## Testes rápidos

O arquivo `src/main/resources/requests.http` contém exemplos prontos para uso em IDEs como VS Code (extensão REST Client) ou IntelliJ IDEA:

```http
### Listar todos
GET http://localhost:8080/V1/api/relogios/getAllPaginated

### Criar relógio
POST http://localhost:8080/V1/api/relogios/create
Content-Type: application/json

{
  "marca": "Seiko",
  "modelo": "Prospex Diver",
  "referencia": "SPB143J1",
  "tipoMovimento": "automatic",
  "materialCaixa": "steel",
  "tipoVidro": "safira",
  "resistenciaAguaM": 200,
  "diametroMm": 40,
  "lugtoLugMm": 47,
  "espessuraMm": 13,
  "larguraMm": 20,
  "precoEmCentavos": 459900,
  "urlImagem": "https://exemplo.com/imagens/seiko-prospex.jpg"
}

### Buscar por UUID
GET http://localhost:8080/V1/api/relogios/details?id={uuid}
```
