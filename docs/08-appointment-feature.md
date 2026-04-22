# ServiceFlow — Appointment Feature

- 21/04/2026

## Objetivo

Permitir o agendamento de serviços para clientes.

## Relações

- um appointment pertence a um user
- um appointment referencia um service

## Regras iniciais

- o service deve pertencer ao user informado
- o horário final é calculado no backend com base na duração do serviço
- não pode haver conflito de horário para o mesmo user no mesmo dia
- apenas appointments com status SCHEDULED bloqueiam horário

## Status

- SCHEDULED
- CANCELLED
- COMPLETED
