# ServiceFlow — Relacionamento entre User e Service

- 21/04/2026

## Objetivo

Associar serviços a um usuário responsável.

## Regra de domínio

- um usuário pode ter vários serviços
- um serviço pertence a um único usuário

## Motivação

Esse relacionamento prepara a base para:

- autenticação futura
- ownership dos dados
- criação de agendamentos
- multi-tenant no futuro

## Decisão técnica

O relacionamento será mapeado inicialmente apenas no lado de `Service` para manter simplicidade.

## Estrutura

- `Service` possui `user_id`
- `User` não precisa expor lista de serviços neste momento
