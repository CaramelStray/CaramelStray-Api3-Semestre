<template>
  <div class="page">
    <!-- Header -->
    <header class="page__header">
      <div class="header__icon"> <img src="@/assets/target-svgrepo-com.svg" alt="Ícone de alvo" /> </div>
      <div>
        <h1 class="header__title">Insira suas Competências</h1>
        <p class="header__subtitle">Selecione e gerencie suas habilidades profissionais</p>
      </div>
    </header>

    <!-- Selecionadas -->
    <section class="selected">
      <div
        class="selected__dropzone"
        @dragover.prevent
        @drop="onDropToSelected"
      >
        <div class="selected__icon"> <img src="@/assets/target-svgrepo-com.svg" alt="Ícone de alvo" width="40" height="40" /></div>
        <h2 class="selected__title">Suas Competências Selecionadas</h2>
        <p class="selected__hint">Arraste competências da lista abaixo para adicioná-las</p>

        <div v-if="selected.length" class="selected__chips">
          <div
            v-for="(item, idx) in selected"
            :key="item.id + '-sel'"
            class="chip"
            draggable="true"
            @dragstart="onDragStart(item, 'selected')"
            @dragend="dragPayload = null"
            @keydown.delete.prevent="removeSelected(idx)"
            tabindex="0"
            title="Arraste para remover ou tecle Delete"
          >
            <span class="chip__dot" :data-variant="item.categoria"></span>
            <span>{{ item.nome }}</span>
            <button class="chip__close" @click="removeSelected(idx)" aria-label="Remover">×</button>
          </div>
        </div>
      </div>
    </section>

    <!-- Busca e filtros -->
    <section class="searchbar">
      <h3 class="sr-only">Buscar Competências</h3>
      <div class="searchbar__box">
        <span class="searchbar__icon">🔍</span>
        <input
          v-model="query"
          class="searchbar__input"
          type="text"
          placeholder="Pesquisar competências…"
          aria-label="Pesquisar competências"
        />
      </div>

      <div class="filters">
        <button
          v-for="tag in categoriasComTodas"
          :key="tag"
          class="filter"
          :data-active="filtroCategoria === tag"
          @click="toggleFiltro(tag)"
        >
          {{ tag }}
        </button>
      </div>
    </section>

    <!-- Disponíveis -->
    <section class="available">
      <div class="available__head">
        <h3>Competências Disponíveis ({{ disponiveisFiltradas.length }})</h3>
        <p class="available__hint">Arraste as competências ou clique no botão + para adicionar</p>
      </div>

      <div class="grid">
        <article
          v-for="comp in disponiveisFiltradas"
          :key="comp.id"
          class="card"
          draggable="true"
          @dragstart="onDragStart(comp, 'available')"
          @dragend="dragPayload = null"
        >
          <header class="card__header">
            <h4 class="card__title">{{ comp.nome }}</h4>
            <button class="card__add" @click="add(comp)" aria-label="Adicionar">+</button>
          </header>
          <p class="card__desc">{{ comp.descricao }}</p>
          <div class="card__tags">
            <span class="pill" :data-variant="comp.categoria">{{ comp.categoria }}</span>
            <span
              v-for="extra in comp.extras || []"
              :key="extra"
              class="pill pill--muted"
            >{{ extra }}</span>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'

/** Dados base – ajuste/expanda conforme necessário */
const all = reactive([
  // Comportamental
  { id: 1,  nome: 'Liderança', descricao: 'Capacidade de liderar equipes e projetos', categoria: 'Comportamental' },
  { id: 2,  nome: 'Comunicação', descricao: 'Habilidades de comunicação verbal e escrita', categoria: 'Comportamental' },
  { id: 3,  nome: 'Trabalho em Equipe', descricao: 'Colaboração eficaz com colegas', categoria: 'Comportamental' },
  { id: 4,  nome: 'Resolução de Problemas', descricao: 'Análise e solução de desafios', categoria: 'Comportamental' },
  { id: 5,  nome: 'Gestão de Tempo', descricao: 'Organização e priorização de tarefas', categoria: 'Comportamental' },

  // Técnica
  { id: 6,  nome: 'Excel Avançado', descricao: 'Uso avançado de planilhas eletrônicas', categoria: 'Técnica' },
  { id: 7,  nome: 'Power BI', descricao: 'Criação de dashboards e relatórios', categoria: 'Técnica' },
  { id: 8,  nome: 'Python', descricao: 'Programação em linguagem Python', categoria: 'Técnica' },
  { id: 9,  nome: 'SQL', descricao: 'Consultas e manipulação de banco de dados', categoria: 'Técnica' },

  // Negócios
  { id:10,  nome: 'Análise de Negócios', descricao: 'Mapeamento de requisitos e processos', categoria: 'Negócios' },
  { id:11,  nome: 'Gestão de Projetos', descricao: 'Planejamento, execução e monitoramento', categoria: 'Negócios' },

  // RH
  { id:12,  nome: 'Recrutamento & Seleção', descricao: 'Triagem, entrevistas e hunting', categoria: 'RH' },
  { id:13,  nome: 'Treinamento & Desenvolvimento', descricao: 'Desenho e avaliação de trilhas', categoria: 'RH' },

  // Idiomas
  { id:14,  nome: 'Inglês', descricao: 'Leitura, escrita e conversação', categoria: 'Idiomas', extras:['B2+'] },
  { id:15,  nome: 'Alemão', descricao: 'Leitura, escrita e conversação', categoria: 'Idiomas', extras:['A2'] },

  // Extras para chegar perto de 20
  { id:16,  nome: 'Noções de Estatística', descricao: 'Métricas, distribuições, testes', categoria: 'Técnica' },
  { id:17,  nome: 'Data Visualization', descricao: 'Boas práticas de visualização', categoria: 'Técnica' },
  { id:18,  nome: 'Negociação', descricao: 'Condução de acordos ganha-ganha', categoria: 'Comportamental' },
  { id:19,  nome: 'Documentação Técnica', descricao: 'Especificações claras e reutilizáveis', categoria: 'Técnica' },
  { id:20,  nome: 'Planejamento Estratégico', descricao: 'Metas, OKRs e desdobramento', categoria: 'Negócios' },
])

const categorias = ['Comportamental', 'Técnica', 'Negócios', 'RH', 'Idiomas']
const categoriasComTodas = ['Todas', ...categorias]

const query = ref('')
const filtroCategoria = ref('Todas')
const selected = ref([])

/** Drag & Drop payload */
const dragPayload = ref(null)
function onDragStart(item, from) {
  dragPayload.value = { item, from }
}
function onDropToSelected(e) {
  if (!dragPayload.value) return
  add(dragPayload.value.item)
  dragPayload.value = null
}

/** Ações */
function add(comp) {
  if (!selected.value.find(s => s.id === comp.id)) {
    selected.value.push(comp)
  }
}
function removeSelected(idx) {
  selected.value.splice(idx, 1)
}
function toggleFiltro(tag) {
  filtroCategoria.value = tag
}

/** Computeds */
const disponiveisFiltradas = computed(() => {
  const q = query.value.trim().toLowerCase()
  const cat = filtroCategoria.value

  return all.filter(c => {
    const matchCat = cat === 'Todas' ? true : c.categoria === cat
    const matchQ =
      !q ||
      c.nome.toLowerCase().includes(q) ||
      c.descricao.toLowerCase().includes(q)
    // Se já está selecionado, continua aparecendo (como na maioria dos catálogos)
    return matchCat && matchQ
  })
})
</script>

<style scoped>
/* Layout base */
.page {
  --bg: #eaf7fb;
  --surface: #ffffff;
  --muted: #6b7785;
  --text: #142032;
  --primary: #2aa6a1;        /* tom agradável turquesa */
  --outline: #d7e6ea;
  --ring: #bfe7e5;
  --shadow: 0 8px 24px rgba(18, 53, 70, 0.05);
  background: var(--bg);
  color: var(--text);
  min-height: 100%;
  padding: 24px;
  line-height: 1.35;
}

.header__icon img {
  width: 40px;
  height: 40px;
}

/* Header */
.page__header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.header__icon {
  width: 40px; height: 40px;
  display: grid; place-items: center;
  border-radius: 10px;
  background: var(--surface);
  border: 1px dashed var(--outline);
}
.header__title { margin: 0; font-size: 20px; font-weight: 700; }
.header__subtitle { margin: 2px 0 0; color: var(--muted); font-size: 12px; }

/* Selecionadas */
.selected__dropzone {
  border: 2px dashed var(--outline);
  background: #f2fbfb;
  border-radius: 14px;
  padding: 32px 20px;
  text-align: center;
  box-shadow: var(--shadow);
}
.selected__icon { font-size: 28px; margin-bottom: 4px; }
.selected__title { margin: 0; font-size: 16px; font-weight: 700; }
.selected__hint  { margin: 6px 0 0; color: var(--muted); font-size: 12px; }

.selected__chips {
  margin-top: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
}

.chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: var(--surface);
  border: 1px solid var(--outline);
  border-radius: 999px;
  padding: 6px 10px;
  box-shadow: var(--shadow);
  outline: none;
}
.chip:focus { box-shadow: 0 0 0 3px var(--ring); }
.chip__close {
  all: unset;
  cursor: pointer;
  width: 18px; height: 18px; line-height: 18px; text-align: center;
  background: #edf4f5; border-radius: 50%;
}
.chip__dot {
  width: 8px; height: 8px; border-radius: 50%; display: inline-block;
}
.chip__dot[data-variant="Comportamental"] { background: #6c9cff; }
.chip__dot[data-variant="Técnica"]        { background: #38b48e; }
.chip__dot[data-variant="Negócios"]       { background: #f7b733; }
.chip__dot[data-variant="RH"]             { background: #b381f0; }
.chip__dot[data-variant="Idiomas"]        { background: #ef6f6c; }

/* Busca e filtros */
.searchbar {
  margin-top: 22px;
  background: var(--surface);
  border: 1px solid var(--outline);
  border-radius: 14px;
  padding: 16px;
  box-shadow: var(--shadow);
}
.searchbar__box {
  position: relative;
  margin-bottom: 12px;
}
.searchbar__icon {
  position: absolute; left: 12px; top: 50%; transform: translateY(-50%);
}
.searchbar__input {
  width: 100%;
  padding: 12px 14px 12px 36px;
  border: 1px solid var(--outline);
  border-radius: 10px;
  background: #f7fbfc;
}
.filters {
  display: flex; flex-wrap: wrap; gap: 8px;
  margin-top: 8px;
}
.filter {
  padding: 8px 12px;
  border-radius: 999px;
  border: 1px solid var(--outline);
  background: #f3f9fa;
  cursor: pointer;
  font-weight: 600;
  font-size: 12px;
}
.filter[data-active="true"] {
  background: #e6f7f6;
  border-color: var(--primary);
  box-shadow: inset 0 0 0 1px var(--primary);
}

/* Disponíveis */
.available { margin-top: 22px; }
.available__head {
  display: flex; align-items: baseline; gap: 12px; flex-wrap: wrap;
  margin-bottom: 10px;
}
.available__head h3 { margin: 0; font-size: 16px; }
.available__hint { margin: 0; color: var(--muted); font-size: 12px; }

.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
}
@media (max-width: 1080px) {
  .grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
@media (max-width: 680px) {
  .grid { grid-template-columns: 1fr; }
}

.card {
  background: var(--surface);
  border: 1px solid var(--outline);
  border-radius: 14px;
  padding: 14px;
  box-shadow: var(--shadow);
}
.card__header {
  display: flex; justify-content: space-between; gap: 8px; align-items: start;
}
.card__title { margin: 0; font-size: 15px; font-weight: 700; }
.card__add {
  all: unset;
  width: 28px; height: 28px;
  display: grid; place-items: center;
  border-radius: 10px;
  background: #e9f7f6;
  border: 1px solid var(--primary);
  color: var(--primary);
  cursor: pointer;
  font-weight: 700;
}
.card__add:hover { filter: brightness(0.97); }
.card__desc { margin: 8px 0 12px; color: var(--muted); font-size: 13px; }
.card__tags { display: flex; gap: 8px; flex-wrap: wrap; }

.pill {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  color: #0f2330;
  background: #eef6ff;
}
.pill--muted { background: #edf4f5; color: #30414f; }

.pill[data-variant="Comportamental"] { background: #e9f0ff; }
.pill[data-variant="Técnica"]        { background: #e7f8f2; }
.pill[data-variant="Negócios"]       { background: #fff3df; }
.pill[data-variant="RH"]             { background: #f1e9ff; }
.pill[data-variant="Idiomas"]        { background: #ffe9e9; }

/* Acessibilidade invisível */
.sr-only {
  position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px;
  overflow: hidden; clip: rect(0,0,0,0); white-space: nowrap; border: 0;
}
</style>