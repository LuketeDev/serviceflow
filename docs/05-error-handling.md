# ServiceFlow — Error Handling

## Objetivo

Padronizar as respostas de erro da API para facilitar:

- consumo pelo frontend
- debugging
- manutenção
- evolução futura da API

## Estratégia atual

Foi implementado um `GlobalExceptionHandler` com `@RestControllerAdvice` para interceptar exceções lançadas pela aplicação e retornar respostas HTTP padronizadas.

## Estruturas de resposta

### ApiError

Usado para erros gerais da aplicação.

Campos:

- `timestamp`: data e hora do erro
- `status`: código HTTP
- `message`: mensagem descritiva
- `path`: endpoint da requisição

Exemplo:

```json
{
  "timestamp": "2026-04-20T13:00:00",
  "status": 404,
  "message": "Service not found with id: ...",
  "path": "/api/services/123"
}
```

### ValidationErrorResponse

Usado para erros de validação de entrada

Campos:

- `timestamp`: data e hora do erro
- `status`: código HTTP
- `message`: mensagem geral
- `path`: endpoint da requisição
- `errors`: mapa com campo e mensagem de validação

Exemplo:

```json
{
  "timestamp": "2026-04-20T13:00:00",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/services",
  "errors": {
    "name": "must not be blank",
    "price": "must be greater than or equal to 0"
  }
}
```

## Exceções tratadas atualmente

### ResourceNotFoundException

Retorna:

- HTTP 404 Not Found

Uso:

- quando um recurso solicitado não existe no banco

### MethodArgumentNotValidException

Retorna:

- HTTP 400 Bad Request

Uso:

- quando os dados enviados na requisição violam regras de validação

### Exception

Retorna:

- HTTP 500 Internal Server Error

Uso:

- fallback para erros inesperados

## Decisões técnicas

### Padronização de resposta

Mesmo em erros, a API deve responder com estrutura previsível.

### Separação entre erro genérico e erro de validação

Erros de validação têm estrutura própria porque o frontend precisa saber exatamente quais campos falharam.

### Mensagem genérica para erro interno

Erros inesperados não devem expor detalhes internos da aplicação para o cliente.

### Melhorias futuras

- adicionar tratamento para exceções de regra de negócio
- adicionar código interno de erro
- adicionar logging estruturado
- padronizar erro com RFC 7807 ou modelo próprio mais robusto
