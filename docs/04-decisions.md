# Decisões Técnicas

## IDs com UUID

Escolhido UUID para evitar acoplamento com IDs sequenciais e facilitar evolução futura.

## Soft deactivate em Service

Serviços não serão removidos fisicamente no início. Serão desativados com campo `active`.

## Organização por feature

A estrutura inicial será simples, organizada por domínio, sem Clean Architecture neste momento.

## Testes

Usei Postman para testar os endpoints.
