# ✅ Sprint 1 – Detalhamento de Tasks (Altallent)

Esta Sprint, focada em estabelecer a base de **Perfis** e **Tags de Competências**, está **CONCLUÍDA**. Os itens **JWT**, **Swagger** e **Flyway** foram postergados para Sprints futuras, conforme acordado.

---

## 🎯 User Story S1-1: Edição de Perfil

**Como colaborador, quero editar meu perfil profissional**
*Requisito Referenciado: [3] Edição de perfil*

| Disciplina | Task ID | Task (O que fazer) | Estimativa (Horas) | Status | DoD Atendido |
|:----------|:-------|:-------------------|:-------------------|:------|:--------------|
| **Backend (Spring)** | B1.1 | **Implementar a entidade `User`** com os campos básicos (nome, e-mail, função/role) no Spring Boot. | 4h | ✅ Concluída | ✅ Spring Boot, ✅ Testes |
| **Backend (Spring)** | B1.2 | **Criar endpoint PUT `/api/v1/users/profile`** para permitir a edição de perfil (exceto hierarquia). **(Autorização Mockada)**. | 6h | ✅ Concluída | ✅ Spring Boot, ✅ Integração |
| **Backend (Spring)** | B1.3 | **Implementar testes de integração/unitários** com cobertura mínima de 80% na lógica de edição e validação do perfil. | 8h | ✅ Concluída | ✅ Testes 80% |
| **Database (SQL)** | D1.1 | **Criar e versionar o Script SQL (V1)** para a tabela `users` no PostgreSQL. | 3h | ✅ Concluída | ✅ PostgreSQL, ✅ Scripts versionados |
| **Frontend (Vue)** | F1.1 | **Desenvolver o componente "Formulário de Edição de Perfil"** utilizando **Vuetify** (responsivo). | 7h | ✅ Concluída | ✅ Vuetify, ✅ Figma |
| **Frontend (Vue)** | F1.2 | **Integrar o formulário** (F1.1) com o endpoint de backend (B1.2) para carregar e salvar dados. Validar em Homologação. | 5h | ✅ Concluída | ✅ Integração, ✅ Homologação |

---

## 🎯 User Story S1-2: Tags de Competências

**Como diretor ou supervisor, quero adicionar tags aos perfis dos colaboradores**
*Requisito Referenciado: [4] Implementação de tags de competências em perfis*

| Disciplina | Task ID | Task (O que fazer) | Estimativa (Horas) | Status | DoD Atendido |
|:----------|:-------|:-------------------|:-------------------|:------|:--------------|
| **Backend (Spring)** | B2.1 | **Criar a entidade `CompetencyTag`** e configurar o relacionamento *muitos-para-muitos* com a entidade `User`. | 5h | ✅ Concluída | ✅ Spring Boot |
| **Backend (Spring)** | B2.2 | **Criar endpoint POST `/api/v1/users/{id}/tags`** para adicionar tags a um perfil específico, com validação de Role. **(Autorização Mockada)**. | 7h | ✅ Concluída | ✅ Spring Boot |
| **Backend (Spring)** | B2.3 | **Implementar testes de integração/unitários** para a lógica de tags, focando na autorização e na persistência de dados. | 9h | ✅ Concluída | ✅ Testes 80% |
| **Database (SQL)** | D2.1 | **Criar e versionar o Script SQL (V2)** para a tabela `competency_tags` e a tabela pivô (`user_competency_tags`). | 3h | ✅ Concluída | ✅ PostgreSQL, ✅ Scripts versionados |
| **Frontend (Vue)** | F2.1 | **Desenvolver o componente "Visualizador/Gerenciador de Tags"** para exibir as tags em um perfil e permitir a adição/remoção (Vuetify chips/autocomplete). | 8h | ✅ Concluída | ✅ Vuetify, ✅ Figma |
| **Frontend (Vue)** | F2.2 | **Integrar o gerenciador de tags** (F2.1) com o endpoint de backend (B2.2) para atualização em tempo real. Validar em Homologação. | 6h | ✅ Concluída | ✅ Integração |
