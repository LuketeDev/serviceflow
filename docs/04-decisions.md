# Decisões Técnicas

- 20/04/2026

## IDs com UUID

Escolhido UUID para evitar acoplamento com IDs sequenciais e facilitar evolução futura.

## Soft deactivate em Service

Serviços não serão removidos fisicamente no início. Serão desativados com campo `active`.

## Organização por feature

A estrutura inicial será simples, organizada por domínio, sem Clean Architecture neste momento.

## Testes

Usei Postman para testar os endpoints.

- 20/04/2026 #2

## Tratamento global de exceções

A aplicação possui um `@RestControllerAdvice` para centralizar o tratamento de erros e padronizar as respostas HTTP.

Benefícios:

- consistência para o frontend
- menor duplicação nos controllers
- evolução centralizada da política de erro
