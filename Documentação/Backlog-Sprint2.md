# ✅ Sprint 2 – Detalhamento de Tasks (Altallent)

Esta Sprint, focada em estabelecer a base de **Perfis** e **Tags de Competências**, está **CONCLUÍDA**. Os itens **JWT**, **Swagger** e **Flyway** foram postergados para Sprints futuras, conforme acordado.

---

## 🎯 User Story S2-1:  Listagem, organização de equipes e usabilidade do projeto

**Como usuário (diretor, supervisor ou colaborador), eu quero visualizar os perfis disponíveis e suas respectivas equipes, para que eu possa acessar informações de competências e experiências.**
*Requisito Referenciado: [3] Listagem de perfil e usabilidade do projeto*

| **Disciplina**            | **Task ID** | **Task (O que fazer)**                                                                               | **Estimativa (Horas)** | **Status**  | **DoD Atendido**                 |
| :------------------------ | :---------- | :--------------------------------------------------------------------------------------------------- | :--------------------- | :---------- | :------------------------------- |
| **Frontend (Figma)**      | F2.1        | **Criar o protótipo das telas** que o diretor terá acesso e a **tela de avaliação do colaborador**.  | 6h                     | ✅ Concluída | ✅ Figma, ✅ Prototipagem          |
| **Frontend (Vue.js)**     | F2.2        | **Criar o front das telas de listagem de perfil** utilizando **Vue.js**.                             | 8h                     | ✅ Concluída | ✅ Vue.js, ✅ Layout Responsivo    |
| **Frontend (Vue.js)**     | F2.3        | **Incluir botões de navegação** nas telas de perfil do colaborador e de competências.                | 4h                     | ✅ Concluída | ✅ Vue.js, ✅ Navegação            |
| **Backend (Spring Boot)** | B2.1        | **Criar os endpoints da tela de listagem de perfil** no backend utilizando **Spring Boot**.          | 6h                     | ✅ Concluída | ✅ Spring Boot, ✅ API REST        |
| **Frontend (Vue.js)**     | F2.4        | **Implementar redirecionamento** na tela de listagem de colaboradores para seus respectivos perfis.  | 5h                     | ✅ Concluída | ✅ Vue.js, ✅ Rotas                |
| **Frontend (Vue.js)**     | F2.5        | **Incluir o menu de navegação** com os botões de líder e colaboradores em todas as telas concluídas. | 4h                     | ✅ Concluída | ✅ Vue.js, ✅ Navegação Global     |
| **Frontend (Vue.js)**     | F2.6        | **Criar a tela de menu do colaborador**.                                                             | 5h                     | ✅ Concluída | ✅ Vue.js, ✅ Interface de Usuário |
| **Frontend (Vue.js)**     | F2.7        | **Implementar o menu lateral** do menu principal nas demais telas.                                   | 4h                     | ✅ Concluída | ✅ Vue.js, ✅ Menu Lateral         |
| **Frontend (Vue.js)**     | F2.8        | **Ajustar a cor das telas de perfil e de competências** conforme o padrão visual do sistema.         | 2h                     | ✅ Concluída | ✅ Vue.js, ✅ UI Consistente       |

## 🎯 User Story S2-2: Tags de Competências

**Como supervisor ou diretor, eu quero avaliar os colaboradores, para que eu possa acompanhar sua evolução profissional e identificar pontos fortes ou de melhoria.**
*Requisito Referenciado: [4] Criação de avaliação por colaboradores*

| **Disciplina**                  | **Task ID** | **Task (O que fazer)**                                                                                 | **Estimativa (Horas)** | **Status**  | **DoD Atendido**                    |
| :------------------------------ | :---------- | :----------------------------------------------------------------------------------------------------- | :--------------------- | :---------- | :---------------------------------- |
| **Frontend (Vue.js)**           | F3.1        | **Criar o front da tela de cadastrar perguntas por perfil.**                                           | 6h                     | ✅ Concluída | ✅ Vue.js, ✅ Integração              |
| **Frontend (Vue.js)**           | F3.2        | **Criar a tela de menu base.**                                                                         | 4h                     | ✅ Concluída | ✅ Vue.js, ✅ Navegação               |
| **Banco de Dados (PostgreSQL)** | D3.1        | **Incluir campo de avaliação ligado ao perfil.**                                                       | 3h                     | ✅ Concluída | ✅ PostgreSQL, ✅ Migração            |
| **Frontend (Figma)**            | F3.3        | **Definir e validar com a equipe o design das avaliações.**                                            | 5h                     | ✅ Concluída | ✅ Figma, ✅ Validação UX             |
| **Backend (Spring Boot)**       | B3.1        | **Criar os endpoints da tela de perguntas.**                                                           | 6h                     | ✅ Concluída | ✅ Spring Boot, ✅ API REST           |
| **Frontend (Vue.js)**           | F3.4        | **Incluir na tela de perguntas os filtros, tipos de resposta e botão "Criar Avaliação".**              | 5h                     | ✅ Concluída | ✅ Vue.js, ✅ UI Interativa           |
| **Backend (Spring Boot)**       | B3.2        | **Fazer o redirecionamento dos botões do menu inicial (líder) e menu lateral de colaboradores/líder.** | 4h                     | ✅ Concluída | ✅ Spring Boot, ✅ Rotas Configuradas |
| **Frontend (Vue.js)**           | F3.5        | **Desenvolver a tela de avaliação.**                                                                   | 6h                     | ✅ Concluída | ✅ Vue.js, ✅ Figma                   |
| **Frontend (Vue.js)**           | F3.6        | **Criar a tela de status das avaliações.**                                                             | 4h                     | ✅ Concluída | ✅ Vue.js, ✅ Feedback Visual         |
| **Frontend (Figma)**            | F3.7        | **Fazer o design da tela de listagem das avaliações.**                                                 | 4h                     | ✅ Concluída | ✅ Figma, ✅ Layout Aprovado          |
| **Banco de Dados (PostgreSQL)** | D3.2        | **Implementar mudanças para incluir respostas e prazo de conclusão das avaliações.**                   | 4h                     | ✅ Concluída | ✅ PostgreSQL, ✅ Scripts Versionados |
| **Backend (Spring Boot)**       | B3.3        | **Finalizar os endpoints relacionados às avaliações.**                                                 | 5h                     | ✅ Concluída | ✅ Spring Boot, ✅ Testes             |
| **Frontend (Vue.js)**           | F3.8        | **Criar a tela de listagem das respostas dos colaboradores.**                                          | 6h                     | ✅ Concluída | ✅ Vue.js, ✅ Integração Backend      |

### Dados
- 📝 Planilha original (Google Sheets): https://docs.google.com/spreadsheets/d/1frL6FXkfENcCwqjMnwLCObYQp4ZJwlVWINT7swGQLfs/edit?usp=sharing  
- 📁 Versão para análise: [Sprint1 Tasks csv](./sprint2.csv)


