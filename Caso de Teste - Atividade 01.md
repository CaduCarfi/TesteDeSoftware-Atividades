## CASO DE TESTE

## Introdução:

Este documento apresenta os casos de teste do sistema de reservas de salas. Os testes têm como objetivo verificar se o sistema atende aos requisitos funcionais e não funcionais definidos para o projeto. Os casos de teste serão divididos em testes funcionais e testes não funcionais.

## Caso de Teste: Regras de Compatibilidade e Capacidade na Criação de Reservas

| Campo | Descrição |
| --- | --- |
| ID | CT-01 |
| Requisito | RF-01, RF-03 |
| Tipo | Funcional |
| Objetivo | Verificar se o sistema permite a reserva para turmas compatíveis e bloqueia tentativas que excedam a capacidade da sala. |
| Pré- condição | Usuário logado; salas e turmas cadastradas com suas respectivas capacidades e quantidades de alunos. |
| Dados | Sala 101 — capacidade 30 e 40; Turmas A e B — 30 e 40 alunos; Data: 20/08/2026. |
| Passos | 1. Acessar tela de reservas 2. Selecionar a Sala 101 3. Selecionar a Turma desejada 4. Informar data e horário 5. Confirmar reserva |
| Resultado esperado | O sistema deve criar a reserva quando a capacidade for suficiente e exibir mensagem de erro quando a turma for maior que a sala. |


## Cenários:

## Cenário 1 - Reservar sala disponível para turma compatível

- Dados de Teste: Sala: "Sala 101" | Capacidade da sala: 40 alunos | Turma: "Turma A" | Quantidade de alunos: 30 | Data: 20/08/2026 | Horário: 10h às 12h

- Resultado Esperado: O sistema permite a criação da reserva. A reserva é registrada com a data, horário, turma e responsável.

## Cenário 3 - Turma maior que a capacidade da sala

- Dados de Teste: Sala: "Sala 101" | Capacidade da sala: 30 alunos | Turma: "Turma B" | Quantidade de alunos: 40

- Resultado Esperado: O sistema impede a criação da reserva. O sistema informa que a quantidade de alunos da turma excede a capacidade da sala.

## Caso de Teste: Validação de Disponibilidade de Agenda e Status da Sala

| Campo | Descrição |
| --- | --- |
| ID | CT-02 |
| Requisito | RF-02, RF-04 |
| Tipo | Funcional |
| Objetivo | Garantir que o sistema impeça sobreposição de reservas na mesma sala e bloqueie o agendamento em salas que estão em manutenção. |
| Pré- condição | Usuário logado; sistema possuir uma sala já reservada em um horário específico e outra sala marcada com status "Em manutenção". |
| Dados | Sala 101 (ocupada das 10h às 12h); Sala 102 (Status: Em manutenção); Data: 20/08/2026. |
| Passos | 1. Acessar tela de reservas 2. Selecionar sala ocupada ou em manutenção 3. Informar turma, data e horário conflitante 4. Confirmar reserva |
| Resultado esperado | O sistema deve bloquear ambas as ações informando, respectivamente, que a sala já está reservada ou em manutenção. |


## Cenários:

## Cenário 2 - Tentar reservar uma sala já ocupada

- Dados de Teste: Sala: "Sala 101" | Data: 20/08/2026 | Horário: 10h às 12h | A sala já possui uma reserva nesse período.

- Resultado Esperado: O sistema impede a criação da nova reserva. O sistema informa que a sala já está reservada para o horário selecionado.

## Cenário 4 - Tentar reservar sala em manutenção

- Dados de Teste: Sala: "Sala 102" | Status da sala: Em manutenção | Data: 20/08/2026 | Horário: 14h às 16h

- Resultado Esperado: O sistema impede a criação da reserva. O sistema informa que a sala está em manutenção.

## Caso de Teste: Validação de Restrições de Horário de Funcionamento

| Campo | Descrição |
| --- | --- |
| ID | CT-03 |
| Requisito | RF-05 |
| Tipo | Funcional |
| Objetivo | Verificar se o sistema restringe a criação de reservas exclusivamente ao intervalo permitido (entre 07h30 e 22h30). |
| Pré- condição | Usuário logado; sala disponível e capacidade adequada para a turma. |
| Dados | Sala 101; Data: 20/08/2026; Horários para teste: 08h-10h, 07h-09h e 22h-23h. |
| Passos | 1. Acessar tela de reservas 2. Preencher dados da sala e turma 3. Inserir um horário dentro, antes e depois do limite 4. Confirmar reserva |
| Resultado esperado | O sistema deve aprovar horários permitidos e bloquear horários fora da janela operacional com mensagens de aviso específicas. |


## Cenários:

## Cenário 5 - Reservar sala dentro do horário permitido

- Dados de Teste: Sala: "Sala 101" | Data: 20/08/2026 | Horário: 08h às 10h | Turma compatível com a sala.

- Resultado Esperado: O sistema permite a criação da reserva.

## Cenário 6 - Tentar reservar sala antes do horário permitido

- Dados de Teste: Sala: "Sala 101" | Data: 20/08/2026 | Horário: 07h às 09h

- Resultado Esperado: O sistema impede a criação da reserva. O sistema informa que as reservas só podem ser realizadas a partir das 07h30.

## Cenário 7 - Tentar reservar sala após o horário permitido

- Dados de Teste: Sala: "Sala 101" | Data: 20/08/2026 | Horário: 22h às 23h

- Resultado Esperado: O sistema impede a criação da reserva. O sistema informa que as reservas devem respeitar o horário máximo de 22h30.

## Caso de Teste: Regras de Permissão para Edição e Geração de Notificações

| Campo | Descrição |
| --- | --- |
| ID | CT-04 |
| Requisito | RF-06, RF-08 |
| Tipo | Funcional |
| Objetivo | Validar que apenas o dono da reserva ou a coordenação podem realizar alterações, e garantir que notificações e histórico sejam gerados. |
| Pré- condição | Reservas existentes no sistema atreladas a diferentes professores; usuários com perfis de Professor e Coordenação logados. |
| Dados | Reserva existente; Perfis: Professor A (dono), Professor B (terceiro), Coordenação; Novo horário disponível. |
| Passos | 1. Acessar painel de reservas cadastradas 2. Tentar editar uma reserva própria ou de outro usuário dependendo do perfil 3. Inserir nova data/horário 4. Confirmar alteração |
| Resultado esperado | O sistema permite alterações válidas (gerando histórico e notificações) e bloqueia tentativas de professores sobre reservas que não os pertencem. |


## Cenários:

## Cenário 8 - Professor altera sua própria reserva

- Dados de Teste: Usuário: Professor | Reserva pertencente ao próprio professor | Nova data e horário válidos.

- Resultado Esperado: O sistema permite a alteração da reserva. O sistema registra a alteração no histórico.

## Cenário 9 - Professor tenta alterar reserva de outro professor

- Dados de Teste: Usuário: Professor | Reserva pertencente a outro professor.

- Resultado Esperado: O sistema impede a alteração. O sistema informa que o usuário não possui permissão para alterar a reserva.

## Cenário 10 - Coordenação altera reserva de outro professor

- Dados de Teste: Usuário: Coordenação | Reserva pertencente a outro professor | Nova data e horário válidos.

- Resultado Esperado: O sistema permite a alteração. A alteração é registrada no histórico. Uma notificação é enviada aos usuários envolvidos.

## Cenário 12 - Alterar uma reserva

- Dados de Teste: Reserva existente | Novo horário disponível | Usuário possui permissão para realizar a alteração.

- Resultado Esperado: O sistema altera a reserva. A alteração é registrada no histórico. Uma notificação é enviada aos usuários envolvidos.

## Cenário 13 - Tentar realizar operação sem permissão

- Dados de Teste: Usuário sem permissão para alterar ou cancelar a reserva | Reserva pertencente a outro professor.

- Resultado Esperado: O sistema impede a operação. O sistema informa que o usuário não possui autorização.

## Caso de Teste: Cancelamento de Reservas, Liberação de Horário e Histórico

Campo

Descrição


| ID | CT-05 |
| --- | --- |
| Requisito | RF-07, RF-08 |
| Tipo | Funcional |
| Objetivo | Verificar se o cancelamento libera o horário da sala para novas locações, além de registrar o histórico e notificar os envolvidos. |
| Pré- condição | Usuário possui permissão para a ação (dono da reserva ou coordenação); existe uma reserva ativa na Sala 101. |
| Dados | Reserva ativa para a Sala 101 em data e horário futuros. |
| Passos | 1. Acessar o sistema de reservas 2. Localizar a reserva ativa da Sala 101 3. Solicitar o cancelamento 4. Confirmar ação |
| Resultado esperado | A reserva deve ser removida da agenda, tornando a sala disponível novamente, com a ação salva em histórico e notificação disparada. |

## Cenários:

## Cenário 11 - Cancelar uma reserva

- Dados de Teste: Reserva existente para a Sala 101 | Usuário possui permissão para cancelar a reserva.

- Resultado Esperado: O sistema cancela a reserva. O horário da sala fica novamente disponível. O cancelamento é registrado no histórico. Uma notificação é enviada aos usuários envolvidos.

## Caso de Teste: Desempenho e Tempo de Resposta

| Campo | Descrição |
| --- | --- |
| ID | CT-06 |
| Requisito | RNF-01 |
| Tipo | Não Funcional (Desempenho) |


| Objetivo | Verificar se o tempo de resposta das consultas e buscas no sistema atende ao limite máximo estabelecido. |
| --- | --- |
| Pré- condição | Sistema rodando em ambiente de homologação/produção com volume de dados representativo no banco de dados. |
| Dados | Filtro de busca por data (ex: 20/08/2026) e por sala (ex: Sala 101). |
| Passos | 1. Acessar a tela de busca de reservas. 2. Inserir os parâmetros de pesquisa. 3. Disparar a ação de busca. 4. Cronometrar o tempo desde o clique até a renderização total dos resultados na tela. |
| Resultado esperado | O sistema deve processar a requisição e exibir os resultados em 2 segundos ou menos. |

## Cenários:

## Cenário 14 - Tempo de resposta da busca com carga normal de dados

- Dados de Teste: Busca pelas reservas do mês atual; Base de dados contendo milhares de registros históricos.

- Resultado Esperado: O sistema retorna a lista de resultados em um tempo d 2 segundos sem apresentar travamentos.

## Caso de Teste: Trilha de Auditoria e Logs do Sistema

| Campo | Descrição |
| --- | --- |
| ID | CT-07 |
| Requisito | RNF-02 |
| Tipo | Não Funcional (Segurança / Auditoria) |


| Objetivo | Garantir que todas as operações críticas realizadas no sistema deixem um rastro claro e auditável. |
| --- | --- |
| Pré-condição | Usuário logado no sistema; acesso habilitado à tabela de logs do banco de dados ou painel de auditoria. |
| Dados | Usuário com permissão; Ações: Criar, alterar e cancelar uma reserva. |
| Passos | 1. Executar uma operação no sistema (ex: cancelar uma reserva existente). 2. Acessar o sistema de logs ou painel de auditoria do banco de dados. 3. Buscar pelo registro correspondente à ação recém-executada. |
| Resultado esperado | O sistema deve ter gravado um registro imutável contendo, no mínimo: ID do usuário, data, hora, tipo de operação realizada e ID da reserva afetada. |

## Cenários:

## Cenário 15 - Registro de operação de cancelamento no log

- Dados de Teste: Usuário: "Professor João" (ID: 15); Ação: Cancelamento da reserva da "Sala 101".

- Resultado Esperado: A trilha de auditoria exibe um novo log atrelado ao ID 15, detalhando que a ação "CANCELAMENTO" foi executada na reserva da "Sala 101" com o timestamp exato do servidor.

## Caso de Teste: Controle de Acesso por Unidades Autorizadas

| Campo | Descrição |
| --- | --- |
| ID | CT-08 |
| Requisito | RNF-03 |
| Tipo | Não Funcional (Segurança / Autorização) |


| Objetivo | Validar se o sistema isola corretamente os dados e bloqueia o acesso a unidades institucionais nas quais o usuário não possui vínculo/permissão. |
| --- | --- |
| Pré-condição | Sistema operando com múltiplas unidades cadastradas (ex: Unidade Norte e Unidade Sul); Usuário logado com vínculo exclusivo a apenas uma delas. |
| Dados | Usuário pertencente à "Unidade Norte"; Dados e URLs referentes à "Unidade Sul". |
| Passos | 1. Fazer login com o usuário da Unidade Norte. 2. Tentar acessar a agenda de reservas da Unidade Sul pelos menus. 3. Tentar forçar o acesso alterando parâmetros na URL ou na API para apontar para a Unidade Sul. |
| Resultado esperado | O sistema deve ocultar os dados de outras unidades na interface e bloquear qualquer requisição direta, retornando um erro de "Acesso Negado". |

## Cenários:

## Cenário 16 - Tentar acessar dados de uma unidade não autorizada via interface

- Dados de Teste: Usuário: Professor da Unidade Norte; Alvo: Agenda da Unidade Sul.

- Resultado Esperado: A interface não exibe a Unidade Sul como opção no filtro de busca.

## Cenário 17 - Tentar forçar acesso a unidade não autorizada via URL/API

- Dados de Teste: Usuário: Professor da Unidade Norte; Requisição forçada buscando o ID da Unidade Sul na rota de reservas.

- Resultado Esperado: O sistema recusa a requisição de busca, não retorna os dados das salas e exibe a mensagem "Você não tem permissão para acessar os dados desta unidade."
