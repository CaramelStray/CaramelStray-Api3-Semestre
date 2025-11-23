✅ Sprint 3 – Detalhamento de Tasks (Altallent)

Esta Sprint é focada em arrumar todas as pendências referentes à sprint anterior, entregar o cadastro de um novo colaborador e a visualização do Dashboard.

---
## 🎯 User Story S3-1:  Cadastro de novos colaboradores

**Como diretor ou supervisor, eu quero poder registrar novos usuários, para que eles possam ter acesso aos recursos do sistema.**

| Disciplina              | Task ID | Task (O que fazer)                                                                 | Estimativa (Horas) | Status        | DoD Atendido                            |
|-------------------------|---------|------------------------------------------------------------------------------------|---------------------|---------------|------------------------------------------|
| Design (Figma)          | D3.1    | Fazer o design da tela de criar novo colaborador                                   | 4h                  | ✔️ Concluída  | ✔️ Figma, ✔️ Protótipo UI                |
| Backend (Spring Boot)   | B3.1    | Criar o backend da tela de cadastro de colaborador                                 | 6h                  | ✔️ Concluída  | ✔️ Spring Boot, ✔️ API REST              |
| Frontend (Vue.js)       | F3.1    | Criar o frontend da tela de cadastro de colaborador                                | 7h                  | ✔️ Concluída  | ✔️ Vue.js, ✔️ Layout Responsivo          |
| Integração (Fullstack)  | I3.1    | Conectar o frontend com o backend da tela de cadastro do colaborador               | 3h                  | ✔️ Concluída  | ✔️ API Integrada, ✔️ Fluxo Funcional     |
| Frontend (Vue.js)       | F3.2    | Adicionar os botões de cadastro de colaboradores nos menus lateral e inicial do líder | 2h               | ✔️ Concluída  | ✔️ Vue.js, ✔️ Navegação Global           |


## 🎯 User Story S3-2:  Login e autenticação do usuário

**Como um usuário, eu quero realizar login na plataforma, para que eu possa acessar as funcionalidades de acordo com meu nível de acesso.
**

| Disciplina              | Task ID | Task (O que fazer)                                                               | Estimativa (Horas) | Status        | DoD Atendido                                 |
|-------------------------|---------|----------------------------------------------------------------------------------|---------------------|---------------|-----------------------------------------------|
| Backend (Spring Boot)   | B4.1    | Realizar o sistema de autenticação do backend                                   | 6h                  | ✔️ Concluída  | ✔️ Spring Boot, ✔️ Segurança, ✔️ Auth         |
| Frontend (Vue.js)       | F4.1    | Realizar o sistema de autenticação do frontend                                  | 5h                  | ✔️ Concluída  | ✔️ Vue.js, ✔️ Controle de Sessão              |
| Backend (Spring Boot)   | B4.2    | Criar o backend da tela de login                                                | 4h                  | ✔️ Concluída  | ✔️ Spring Boot, ✔️ API Login                  |
| Backend (Spring Boot)   | B4.3    | Ajudar no sistema de autenticação do backend (suporte/ajustes)                  | 3h                  | ✔️ Concluída  | ✔️ Correções BE, ✔️ Auth Revisado             |
| Frontend (Vue.js)       | F4.2    | Fazer a tela de login (frontend)                                                | 4h                  | ✔️ Concluída  | ✔️ Vue.js, ✔️ UI Login                        |
| Integração (Fullstack)  | I4.1    | Conectar a tela de login ao endpoint feito pelo Honda                           | 2h                  | ✔️ Concluída  | ✔️ API Integrada, ✔️ Fluxo de Login           |
| Frontend (Vue.js)       | F4.3    | Incluir campo e botão de inserção de novas competências                         | 3h                  | ✔️ Concluída  | ✔️ Vue.js, ✔️ CRUD Competências               |
| Backend (Spring Boot)   | B4.4    | Permitir que o supervisor realize as avaliações destinadas a ele                | 4h                  | ✔️ Concluída  | ✔️ Regras de Permissão, ✔️ Fluxo Revisado     |
| Backend (Spring Boot)   | B4.5    | Mostrar somente as avaliações do setor correspondente ao supervisor             | 3h                  | ✔️ Concluída  | ✔️ Filtro por Setor, ✔️ Lógica de Supervisor  |


## 🎯 User Story S3-3: Dashboard

**Como diretor, eu quero acessar dashboards de competências, para que eu possa visualizar métricas da equipe de forma clara e rápida.**

| Disciplina              | Task ID | Task (O que fazer)                                                             | Estimativa (Horas) | Status        | DoD Atendido                                  |
|-------------------------|---------|------------------------------------------------------------------------------|---------------------|---------------|----------------------------------------------|
| Backend (Spring Boot)   | B5.1    | Criar endpoint de estatísticas gerais                                        | 5h                  | ✔️ Concluída  | ✔️ Spring Boot, ✔️ API REST                   |
| Backend (Spring Boot)   | B5.2    | Criar endpoint de evolução mensal                                            | 5h                  | ✔️ Concluída  | ✔️ Spring Boot, ✔️ API REST                   |
| Backend (Spring Boot)   | B5.3    | Criar endpoint de distribuição por departamento                              | 5h                  | ✔️ Concluída  | ✔️ Spring Boot, ✔️ API REST                   |
| Backend (Spring Boot)   | B5.4    | Testes de integração e validação                                             | 4h                  | ✔️ Concluída  | ✔️ Testes BE, ✔️ Integração Validada          |
| Frontend (Vue.js)       | F5.1    | Criar a tela de dashboard                                                    | 6h                  | ✔️ Concluída  | ✔️ Vue.js, ✔️ Interface de Usuário            |
| Frontend (Vue.js)       | F5.2    | Implementar consumo dos endpoints                                            | 4h                  | ✔️ Concluída  | ✔️ API Consumida, ✔️ Fluxo de Dados           |
| Frontend (Vue.js)       | F5.3    | Criar componentes reutilizáveis de Card                                      | 3h                  | ✔️ Concluída  | ✔️ Vue.js, ✔️ Componentização                 |
| Frontend (Vue.js)       | F5.4    | Implementar gráficos interativos                                             | 5h                  | ✔️ Concluída  | ✔️ Gráficos, ✔️ Interatividade                |
| Frontend (Vue.js)       | F5.5    | Aplicar estilização conforme o design system                                 | 3h                  | ✔️ Concluída  | ✔️ UI Consistente, ✔️ Design System           |
| Integração (Fullstack)  | I5.1    | Conectar front e back da tela de dashboard                                   | 3h                  | ✔️ Concluída  | ✔️ API Integrada, ✔️ Dados Sincronizados      |
| Testes (Qualidade)      | T5.1    | Teste de usabilidade e performance                                           | 4h                  | ✔️ Concluída  | ✔️ Usabilidade, ✔️ Performance Validada       |


## 🎯 User Story S3-4: Correção de pendencias de usabilidade

**Como usuário, eu devo ter uma usabilidade clara e padronizada no sistema**

| Disciplina             | Task ID | Task (O que fazer)                                                                                   | Estimativa (Horas) | Status       | DoD Atendido                                  |
|------------------------|---------|--------------------------------------------------------------------------------------------------------|---------------------|--------------|------------------------------------------------|
| Frontend (Vue.js)      | F4.1    | Arrumar layout da revisão de avaliação                                                                 | 3h                  | ✔️ Concluída | ✔️ Vue.js, ✔️ Layout                           |
| Backend (Spring Boot)  | B4.1    | Tela de perfil — equipe do colaborador não inserida (corrigir endpoint)                               | 4h                  | ✔️ Concluída | ✔️ Spring Boot, ✔️ Endpoint Ajustado           |
| Frontend (Vue.js)      | F4.2    | Incluir na tela de perfil do colaborador um campo de Cargo                                            | 2h                  | ✔️ Concluída | ✔️ Campo Adicionado, ✔️ UI Perfil              |
| Frontend (Vue.js)      | F4.3    | Incluir campo de prazo ao criar avaliação                                                              | 2h                  | ✔️ Concluída | ✔️ Campo Criado, ✔️ UI Avaliação               |
| Frontend (Vue.js)      | F4.4    | Ajustar dashboard: botão visível só para supervisor + mover botão de cadastro perto do botão sair     | 3h                  | ✔️ Concluída | ✔️ Navegação, ✔️ Permissões                    |
| Backend (Spring Boot)  | B4.2    | Questões somem após responder — permitir que supervisor visualize como concluídas                     | 4h                  | ✔️ Concluída | ✔️ Fluxo Corrigido, ✔️ Ajuste de Regras        |
| Backend (Spring Boot)  | B4.3    | Arrumar Dashboard para permitir filtro por equipes                                                    | 3h                  | ✔️ Concluída | ✔️ Filtro de Equipes, ✔️ API Dashboard         |
| Frontend (Vue.js)      | F4.5    | Incluir ScrollView para filtro de equipes na Dashboard                                                | 2h                  | ✔️ Concluída | ✔️ ScrollView, ✔️ UX Dashboard                 |
| Frontend (Vue.js)      | F4.6    | Barra lateral mostra “Sistema RH” — substituir por novo termo                                         | 1h                  | ✔️ Concluída | ✔️ Texto Atualizado                            |
| Frontend (Vue.js)      | F4.7    | Botão de sair duplicado — manter apenas na barra lateral                                              | 1h                  | ✔️ Concluída | ✔️ Navegação Corrigida                         |

### Dados
- 📁 Versão para análise: [Sprint1 Tasks csv](./TasksSprint3.csv)

