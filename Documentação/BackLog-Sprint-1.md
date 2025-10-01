# 🚀 Sprint 1 – Detalhamento de Tasks (Altallent)

Esta Sprint tem foco em configurar a estrutura de dados inicial de **Perfis** e implementar a funcionalidade básica de **Edição de Perfil** e o recurso de **Tags de Competências**.

---

## 🎯 User Story S1-1: Edição de Perfil

**Como colaborador, quero editar meu perfil profissional**
*Requisito Referenciado: [3] Edição de perfil*

| Disciplina | Task ID | Task (O que fazer) | Estimativa (Horas) | Status | DoD Atendido |
|:----------|:-------|:-------------------|:-------------------|:------|:--------------|
| **Backend (Spring)** | B1.1 | **Implementar a entidade `User`** com os campos básicos (nome, e-mail, função/role) no Spring Boot. | 4h | 🚀 Planejada | ✅ Swagger, ✅ Testes |
| **Backend (Spring)** | B1.2 | **Criar endpoint PUT `/api/v1/users/profile`** para permitir que o **usuário logado** edite seus próprios dados de perfil (exceto hierarquia). Implementar a lógica de edição. **(Identificação do usuário será mockada nesta Sprint)**. | 6h | 🚀 Planejada | ✅ Swagger |
| **Backend (Spring)** | B1.3 | **Implementar testes de integração/unitários** para garantir cobertura mínima de 80% na lógica de edição e validação do perfil. | 8h | 🚀 Planejada | ✅ Testes 80% |
| **Database (SQL)** | D1.1 | **Criar e versionar o Script SQL (V1)** para a tabela `users` no PostgreSQL, incluindo colunas de perfil e a relação hierárquica (diretor, supervisor, colaborador). | 3h | 🚀 Planejada | ✅ Scripts versionados |
| **Frontend (Vue)** | F1.1 | **Desenvolver o componente "Formulário de Edição de Perfil"** utilizando **Vuetify** (responsivo). | 7h | 🚀 Planejada | ✅ Vuetify |
| **Frontend (Vue)** | F1.2 | **Integrar o formulário** (F1.1) com o endpoint de backend (B1.2) para carregar e salvar dados. | 5h | 🚀 Planejada | ✅ Integração |

---

## 🎯 User Story S1-2: Tags de Competências

**Como diretor ou supervisor, quero adicionar tags aos perfis dos colaboradores**
*Requisito Referenciado: [4] Implementação de tags de competências em perfis*

| Disciplina | Task ID | Task (O que fazer) | Estimativa (Horas) | Status | DoD Atendido |
|:----------|:-------|:-------------------|:-------------------|:------|:--------------|
| **Backend (Spring)** | B2.1 | **Criar a entidade `CompetencyTag`** e configurar o relacionamento *muitos-para-muitos* com a entidade `User`. | 5h | 🚀 Planejada | |
| **Backend (Spring)** | B2.2 | **Criar endpoint POST `/api/v1/users/{id}/tags`** para adicionar tags. Implementar a **validação de autorização** para que apenas Diretor/Supervisor possa executar a ação. **(A identificação e Role do usuário serão mockadas nesta Sprint).** | 7h | 🚀 Planejada | ✅ Swagger |
| **Backend (Spring)** | B2.3 | **Implementar testes de integração/unitários** para a lógica de tags, focando na autorização e na persistência de dados. | 9h | 🚀 Planejada | ✅ Testes 80% |
| **Database (SQL)** | D2.1 | **Criar e versionar o Script SQL (V2)** para a tabela `competency_tags` e a tabela pivô (`user_competency_tags`). | 3h | 🚀 Planejada | ✅ Scripts versionados |
| **Frontend (Vue)** | F2.1 | **Desenvolver o componente "Visualizador/Gerenciador de Tags"** para exibir as tags em um perfil e permitir a adição/remoção por usuários autorizados (Vuetify chips/autocomplete). | 8h | 🚀 Planejada | ✅ Vuetify |
| **Frontend (Vue)** | F2.2 | **Integrar o gerenciador de tags** (F2.1) com o endpoint de backend (B2.2) para atualização em tempo real. | 6h | 🚀 Planejada | ✅ Integração |