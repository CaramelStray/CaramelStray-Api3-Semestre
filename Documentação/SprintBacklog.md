# Sprint Backlog – Altallent

### Requisitos Funcionais

[1] Cadastro de usuários com hierarquia (diretor, supervisor e colaborador).  
[2] Listagem e organização de equipes, com filtros por função.  
[3] Edição de perfil por colaboradores e superiores.  
[4] Implementação de tags de competências em perfis.  
[5] Avaliações de colaboradores por supervisores e diretores.  
[6] Login e autenticação de usuários via JWT.  
[7] Relatórios exportáveis (CSV/PDF) com filtros de equipe e competências.  
[8] Dashboard visual com métricas, indicadores e filtros dinâmicos.  

---

### ✅ Definition of Done (DoD) – Altallent

- Código backend desenvolvido em **Spring Boot** versionado no repositório principal.  
- Banco de dados (**PostgreSQL**) atualizado com **scripts versionados (Flyway)**.  
- Frontend implementado em **Vue.js + Vuetify**, responsivo e integrado ao backend.  
- Autenticação e autorização implementadas via **JWT**, validadas em **Postman**.  
- Testes unitários e de integração com cobertura mínima de **80%** nas regras críticas.  
- Endpoints REST documentados com **Swagger**.  
- Interfaces confirmadas no **Figma** antes da implementação.  
- Funcionalidade validada em ambiente de **homologação** com dados reais de teste.  
- Deploy automatizado testado (build + banco + API + frontend funcionando em conjunto).  
- Documentação de uso e técnica atualizada (README ou Wiki).  

---

### ✅ Definition of Ready (DoR) – Altallent

- User Story validada e compreendida pelo time técnico.  
- Critérios de aceitação claros, objetivos e documentados.  
- Protótipos/Wireframes definidos no **Figma** e revisados com o cliente.  
- Modelagem de entidades e relacionamentos definida em **PostgreSQL**.  
- Contratos de API descritos no **Swagger**.  
- Dependências externas identificadas (bibliotecas, integrações).  
- Estratégia de autenticação/autorização via **JWT** definida.  
- Dados de exemplo ou **mocks** prontos para testes.  
- Estimativa de esforço registrada pelo time.  
- Reunião de refinamento realizada com aprovação do cliente.  

---

## 🚀 Sprint 1 – Planejada

| ID   | User Story                                                                 | Prioridade | Status        | Backlog Completo | DoR Atendido | DoD Atendido | Requisito Referenciado |
|------|----------------------------------------------------------------------------|------------|---------------|------------------|--------------|--------------|------------------------|
| S1-1 | Como colaborador, quero editar meu perfil profissional                     | Alta       | 🚀 Planejada  | ✅                | ❌            | ❌            | [1] Edição de perfil |
| S1-2 | Como diretor ou supervisor, quero adicionar tags aos perfis dos colaboradores | Alta     | 🚀 Planejada  | ✅                | ❌            | ❌            | [2] Implementação de tags |

---

## 🚀 Sprint 2 – Planejada

| ID   | User Story                                                                 | Prioridade | Status        | Backlog Completo | DoR Atendido | DoD Atendido | Requisito Referenciado |
|------|----------------------------------------------------------------------------|------------|---------------|------------------|--------------|--------------|------------------------|
| S2-1 | Como usuário, quero visualizar os perfis e suas respectivas equipes        | Média      | 🚀 Planejada  | ✅                | ❌            | ❌            | [3] Listagem e organização de equipes |
| S2-2 | Como supervisor ou diretor, quero avaliar os colaboradores                 | Alta       | 🚀 Planejada  | ✅                | ❌            | ❌            | [4] Avaliações de colaboradores |

---

## 🚀 Sprint 3 – Planejada

| ID   | User Story                                                                 | Prioridade | Status        | Backlog Completo | DoR Atendido | DoD Atendido | Requisito Referenciado |
|------|----------------------------------------------------------------------------|------------|---------------|------------------|--------------|--------------|------------------------|
| S3-1 | Como diretor ou supervisor, quero cadastrar novos usuários                 | Alta       | 🚀 Planejada  | ✅                | ❌            | ❌            | [5] Cadastro de usuários com hierarquia |
| S3-2 | Como usuário, quero realizar login seguro na plataforma                    | Alta       | 🚀 Planejada  | ✅                | ❌            | ❌            | [6] Login e autenticação via JWT |
| S3-3 | Como diretor, quero gerar relatórios exportáveis da equipe                 | Média      | 🚀 Planejada  | ✅                | ❌            | ❌            | [7] Relatórios exportáveis |
| S3-4 | Como diretor, quero acessar dashboards de competências com filtros dinâmicos | Alta     | 🚀 Planejada  | ✅                | ❌            | ❌            | [8] Dashboard visual |

---

## 📌 Legenda de Status

- ✅ Concluída  
- 🔄 Iniciada / Em progresso  
- 🚀 Planejada (ainda não iniciada)  
- ❌ Não concluída  

## 📌 Legenda de Backlog Completo

- ✅ A história faz parte do Product Backlog oficial  
