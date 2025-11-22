# 🪴 Estrutura de branchs

## 📋 Visão Geral
Este documento descreve a estratégia de branches utilizando Git Flow adaptado para integração com GitHub Projects.

---

## 🌿 Estrutura de Branches

### `main`
Essa branch sempre refletirá o estado pronto para produção do código.  
Todos os commits dessa branch devem ser estáveis.

---

### `sprint1`
É onde foram inseridas todas as atualizações referentes à **Sprint 1**.

### `sprint2`
É onde foram inseridas todas as atualizações referentes à **Sprint 2**.

---

### `features`
Essas branchs são criadas a partir da branch da sprint de referência para facilitar o desenvolvimento de novas funcionalidades, testes ou correção de erros.  
Uma vez que a funcionalidade é completa, testada e aceita, ela é mergeada novamente na branch da sprint correspondente.

---

### 🧩 Nomenclatura de branchs
Para a criação de novas branchs seguiremos o padrão criado pelo **GitHub Projects**, que consiste em:
[id-issue]-[título-da-issue]
Exemplo:
23-endpoint-cliente
