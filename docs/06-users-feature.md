# ServiceFlow — User Feature

- 21/04/2026

## Objetivo

Implementar o CRUD da entidade User para representar o profissional ou responsável pelo negócio dentro da plataforma.

## Papel no domínio

O User será a base para futuras funcionalidades:

- autenticação
- autorização
- vínculo com serviços
- vínculo com agendamentos
- multi-tenant no futuro

## Escopo inicial

Neste momento, a entidade User será apenas um CRUD administrativo.

## Campos planejados

- id
- name
- email
- phone
- password
- active
- createdAt
- updatedAt

## Regras iniciais

- email deve ser único
- password é obrigatória
- exclusão lógica via `active = false`

## Observação

Nesta fase, a senha pode ser persistida de forma simples apenas para modelagem inicial.
Quando a autenticação for implementada, será obrigatório aplicar hash com BCrypt.
