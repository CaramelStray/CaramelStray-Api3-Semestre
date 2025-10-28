<template>
  <div class="competencia-content-wrapper">
    <header class="page__header">
      <div class="header__left">
        <div class="header__icon">
          <img src="@/assets/target-svgrepo-com.svg" alt="Ícone de alvo" width="40" height="40" />
        </div>
        <div>
          <h1 class="header__title">{{ pageTitle }}</h1>
          <p class="header__subtitle">{{ pageSubtitle }}</p>
        </div>
      </div>
      <div class="header__user">
        <img src="@/assets/user-circle-svgrepo-com.svg" alt="Ícone de usuário" width="40" height="40" />
        <span class="user__name">{{ nomeUsuarioLogado }}</span>
      </div>
    </header>

    <section class="selected">
      <div class="selected__dropzone" @dragover.prevent @drop="onDropToSelected">
        <div class="selected__icon">
          <img src="@/assets/target-svgrepo-com.svg" alt="Ícone de alvo" width="40" height="40" />
        </div>
        <h2 class="selected__title">Competências Selecionadas</h2>
        <p class="selected__hint">Clique ou arraste as competências da lista abaixo para adicioná-las aqui</p>

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
        <p v-else class="selected__empty-hint">Nenhuma competência selecionada ainda.</p>
      </div>
    </section>

    <section class="action-bar">
      <button class="save-button" :disabled="isSaving || isLoading" @click="saveCompetencias">
        {{ isSaving ? 'Salvando...' : 'Salvar Competências' }}
      </button>
    </section>

    <template v-if="isLoading">
      <div class="loading-container">
        <h3>Carregando competências...</h3>
        <p>Aguarde a conexão com a API.</p>
      </div>
    </template>

    <template v-else>
      <section class="searchbar">
        <h3 class="sr-only">Buscar Competências</h3>
        <div class="searchbar__box">
          <span class="searchbar__icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-4 w-4">
              <path fill-rule="evenodd" d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z" clip-rule="evenodd" />
            </svg>
          </span>
          <input v-model="query" class="searchbar__input" type="text" placeholder="Pesquisar competências…" aria-label="Pesquisar competências"/>
          <button v-if="query" @click="query = ''" class="searchbar__clear" aria-label="Limpar busca">×</button>
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
              <button
                v-if="!isSelected(comp.id)"
                class="card__add"
                @click="add(comp)"
                aria-label="Adicionar"
              >+</button>
              <span v-else class="card__added-icon" title="Já selecionada">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5">
                  <path fill-rule="evenodd" d="M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12Zm13.36-1.814a.75.75 0 1 0-1.06-1.06l-3.25 3.25-1.5-1.5a.75.75 0 0 0-1.06 1.06l2 2a.75.75 0 0 0 1.06 0l3.75-3.75Z" clip-rule="evenodd" />
                </svg>
              </span>
            </header>

            <p class="card__desc">{{ comp.descricao }}</p>

            <div class="card__tags">
              <span class="pill" :data-variant="comp.categoria">{{ comp.categoria }}</span>
              <span v-for="extra in comp.extras || []" :key="extra" class="pill pill--muted">{{ extra }}</span>
            </div>
          </article>
        </div>

        <div v-if="!disponiveisFiltradas.length" class="no-results-available">
          Nenhuma competência encontrada com os termos ou filtros atuais.
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { computed, watch, ref, onMounted } from 'vue'
import axios from 'axios'
import { useRoute } from 'vue-router'
import { useAuth } from '../auth'

const route = useRoute();
const { usuarioLogado } = useAuth();

const FUNCIONARIO_ID_ALVO = computed(() => String(route.params.id));
const USUARIO_LOGADO_ID = computed(() => String(usuarioLogado.value?.codigo || ''));
const API_BASE_URL = 'http://localhost:8080/api/competencia'
const API_FUNCIONARIO_COMP_URL = computed(() => `http://localhost:8080/api/funcionario/${FUNCIONARIO_ID_ALVO.value}/competencias`);

const perfilAlvo = ref(null);
const all = ref([]);
const selected = ref([]);
const isLoading = ref(true);
const isSaving = ref(false);
const query = ref('');
const filtroCategoria = ref('Todas');
const dragPayload = ref(null);
const categorias = ['Comportamental', 'Técnica', 'Negócios', 'RH', 'Idiomas', 'Outros'];
const categoriasComTodas = ['Todas', ...categorias];

async function loadCompetencias() {
  isLoading.value = true;
  try {
    const [allResponse, selectResponse] = await Promise.all([
      axios.get(API_BASE_URL),
      axios.get(API_FUNCIONARIO_COMP_URL.value)
    ]);

    const allCompetencias = allResponse.data.map(comp => ({
      id: comp.id,
      nome: comp.nome,
      descricao: comp.descricao || `Detalhe da competência: ${comp.nome}`,
      categoria: categorias[(comp.id % categorias.length)] || 'Outros',
      extras: []
    }));
    all.value = allCompetencias;

    const existingCompetencias = selectResponse.data.competencias || [];
    selected.value = existingCompetencias
      .map(existingComp => {
        const fullDetails = allCompetencias.find(c => c.id === existingComp.id);
        return fullDetails || {
          id: existingComp.id,
          nome: existingComp.nome,
          descricao: 'Detalhes indisponíveis',
          categoria: 'Outros',
          extras: []
        };
      })
      .filter(Boolean);
  } catch (error) {
    console.error('Erro ao carregar dados iniciais:', error);
    alert('Falha ao conectar com a API ou buscar dados. Verifique a conexão.');
    all.value = [
      { id: 999, nome: 'Falha na Conexão', descricao: 'API error.', categoria: 'Erro', extras: [] }
    ];
  } finally {
    isLoading.value = false;
  }
}

async function loadPerfilAlvo() {
  try {
    const response = await axios.get(`http://localhost:8080/api/funcionario/${FUNCIONARIO_ID_ALVO.value}`);
    perfilAlvo.value = response.data;
  } catch (error) {
    console.error('Falha ao carregar dados do perfil alvo:', error);
  }
}

function isSelected(id) { return selected.value.some(s => s.id === id); }
function add(comp) { if (!isSelected(comp.id)) selected.value.push(comp); }
function removeSelected(idx) { selected.value.splice(idx, 1); }
function toggleFiltro(tag) { filtroCategoria.value = tag; }

function onDragStart(item, from) { dragPayload.value = { item, from }; }
function onDropToSelected() {
  if (!dragPayload.value || dragPayload.value.from !== 'available') return;
  add(dragPayload.value.item); dragPayload.value = null;
}

const isSelfEdit = computed(() =>
  !!(USUARIO_LOGADO_ID.value && FUNCIONARIO_ID_ALVO.value && USUARIO_LOGADO_ID.value === FUNCIONARIO_ID_ALVO.value)
);

const pageTitle = computed(() => {
  if (isSelfEdit.value) return 'Minhas Competências';
  if (perfilAlvo.value) return `Editando Competências de ${perfilAlvo.value.nomeCompleto}`;
  return 'Gerenciar Competências';
});

const pageSubtitle = computed(() =>
  isSelfEdit.value ? 'Selecione e gerencie suas habilidades' : 'Modifique as habilidades do perfil'
);

const nomeUsuarioLogado = computed(() => usuarioLogado.value?.nomeCompleto || 'Usuário');

const disponiveisFiltradas = computed(() => {
  const q = query.value.trim().toLowerCase();
  const cat = filtroCategoria.value;
  return all.value.filter(c =>
    (cat === 'Todas' || c.categoria === cat) &&
    (!q || c.nome.toLowerCase().includes(q) || c.descricao.toLowerCase().includes(q))
  );
});

async function saveCompetencias() {
  isSaving.value = true;
  const idsCompetencias = selected.value.map(item => item.id);
  const body = { codigosCompetencia: idsCompetencias };

  if (!USUARIO_LOGADO_ID.value) {
    alert('Erro de autenticação: ID do usuário logado não encontrado.');
    isSaving.value = false;
    return;
  }

  try {
    await axios.put(API_FUNCIONARIO_COMP_URL.value, body, {
      headers: { 'X-Usuario-Logado-Id': USUARIO_LOGADO_ID.value }
    });
    alert('Competências salvas com sucesso!');
    await loadCompetencias();
  } catch (error) {
    console.error('Falha ao salvar competências:', error);
    let message = 'Erro desconhecido ao salvar competências.';
    if (error.response) {
      if (error.response.status === 403)
        message = 'Ação não autorizada. Você só pode alterar suas próprias competências ou de subordinados na mesma área.';
      else if (error.response.status === 404)
        message = 'Funcionário ou Competência não encontrados.';
      else if (error.response.data?.message)
        message = error.response.data.message;
    } else if (error.request) {
      message = 'Sem resposta do servidor. Verifique a conexão.';
    }
    alert(`Erro ao salvar: ${message}`);
  } finally {
    isSaving.value = false;
  }
}

onMounted(() => { Promise.all([loadCompetencias(), loadPerfilAlvo()]); });
</script>

<style scoped>
/*LAYOUT PRINCIPAL*/
.competencia-content-wrapper{
  --bg:#f3f9f9; --surface:#ffffff; --muted:#6b7785; --text:#142032;
  --primary:#382eceff; --outline:#ccdeecff; --ring:#d5e7f8ff;
  --shadow:0 8px 24px rgba(18,53,70,0.05);

  background:var(--bg);
  color:var(--text);
  min-height:100%;
  padding:16px;                 /* mais encostado nas laterais */
  line-height:1.35;
  box-sizing:border-box;
}

/*CABEÇALHO*/
.page__header{
  display:flex; align-items:center; justify-content:space-between;
  gap:8px; margin-bottom:16px;
  padding:4px 4px;              /* menos espaço vertical e lateral */
}
.header__left{ display:flex; align-items:center; gap:8px; }
.header__icon img{ width:28px; height:28px; } /* menorzinho */
.header__title{ margin:0; font-size:20px; font-weight:700; }
.header__subtitle{ margin:0; color:var(--muted); font-size:13px; }
.header__user{ display:flex; align-items:center; gap:8px; font-weight:600; }
.user__name{ font-size:14px; }

/*SELECIONADAS*/
.selected{ margin-bottom:16px; }
.selected__dropzone{
  border:2px dashed var(--outline); background:#f8fbfd;
  border-radius:14px; padding:24px 16px; text-align:center;
  transition:border-color .2s ease, background-color .2s ease;
}
.selected__dropzone:hover{ border-color:var(--primary); background-color:#f0f3ff; }
.selected__icon{ font-size:28px; margin-bottom:8px; color:var(--primary); }
.selected__icon img{ width:40px; height:40px; margin:0 auto; }
.selected__title{ margin:0; font-size:16px; font-weight:700; color:var(--text); }
.selected__hint,.selected__empty-hint{ margin:6px 0 0; color:var(--muted); font-size:13px; }
.selected__empty-hint{ margin-top:16px; font-style:italic; }
.selected__chips{ margin-top:20px; display:flex; flex-wrap:wrap; gap:10px; justify-content:center; }

.chip{
  display:inline-flex; align-items:center; gap:8px;
  background:var(--surface); border:1px solid var(--outline);
  border-radius:999px; padding:6px 10px; /* pad menor */
  box-shadow:0 1px 2px rgba(0,0,0,0.05);
  outline:none; font-size:14px; cursor:grab;
}
.chip:focus{ box-shadow:0 0 0 3px var(--ring); }
.chip:active{ cursor:grabbing; }
.chip__close{
  all:unset; cursor:pointer; width:20px; height:20px; line-height:20px;
  text-align:center; background:#eef3f7; border-radius:50%; color:var(--muted);
  transition:background-color .15s, color .15s;
}
.chip__close:hover{ background-color:#e0e8ee; color:var(--text); }
.chip__dot{ width:9px; height:9px; border-radius:50%; display:inline-block; flex-shrink:0; }
.chip__dot[data-variant="Comportamental"]{ background:#6c9cff; }
.chip__dot[data-variant="Técnica"]{ background:#38b48e; }
.chip__dot[data-variant="Negócios"]{ background:#f7b733; }
.chip__dot[data-variant="RH"]{ background:#b381f0; }
.chip__dot[data-variant="Idiomas"]{ background:#ef6f6c; }
.chip__dot[data-variant="Outros"]{ background:#a0aec0; }
.chip__dot[data-variant="Erro"]{ background:#f56530; }

/*AÇÃO (SALVAR)*/
.action-bar{
  margin:16px 0; padding:12px 0; text-align:center;
  border-top:1px solid var(--outline); border-bottom:1px solid var(--outline);
  background:var(--surface); border-radius:14px; box-shadow:var(--shadow);
}
.save-button{
  all:unset; padding:12px 28px; border-radius:10px; background:var(--primary);
  color:var(--surface); cursor:pointer; font-weight:700; transition:background .2s, opacity .2s;
  box-shadow:0 4px 8px rgba(56,46,206,.2); font-size:15px;
}
.save-button:hover:not(:disabled){ background:#2a1fb8; }
.save-button:disabled{ background:var(--muted,#eef3f7); color:var(--text,#142032); cursor:not-allowed; opacity:.6; box-shadow:none; }

/*FILTROS*/
.loading-container{ text-align:center; padding:40px 20px; color:var(--muted); }
.loading-container h3{ font-size:16px; margin-bottom:8px; color:var(--text); }

.searchbar{ margin-top:16px; background:var(--surface); border:1px solid var(--outline); border-radius:14px; padding:14px; box-shadow:var(--shadow); }
.searchbar__box{ position:relative; margin-bottom:16px; }
.searchbar__icon{ position:absolute; left:14px; top:50%; transform:translateY(-50%); color:var(--muted); pointer-events:none; }
.searchbar__icon svg{ width:16px; height:16px; }
.searchbar__input{
  width:100%; padding:12px 40px; border:1px solid var(--outline); border-radius:10px;
  background:#f8fafd; font-size:14px; color:var(--text); outline:none;
  transition:border-color .2s, box-shadow .2s;
}
.searchbar__input:focus{ border-color:var(--primary); box-shadow:0 0 0 3px var(--ring); background:var(--surface); }
.searchbar__clear{
  all:unset; position:absolute; right:8px; top:50%; transform:translateY(-50%);
  width:28px; height:28px; line-height:28px; text-align:center; border-radius:50%; cursor:pointer;
  color:var(--muted); font-size:20px;
}
.searchbar__clear:hover{ background-color:#eef3f7; color:var(--text); }

.filters{ display:flex; flex-wrap:wrap; gap:8px; margin-top:12px; }
.filter{
  padding:8px 14px; border-radius:999px; border:1px solid var(--outline);
  background:#f3f9fa; cursor:pointer; font-weight:600; font-size:13px; color:var(--muted);
  transition:all .2s;
}
.filter:hover{ background:#eef7f8; color:var(--text); border-color:#d8e9e9; }
.filter[data-active="true"]{ background:#e6f7f6; border-color:var(--primary); color:var(--primary); }

/*LISTA DE DISPONÍVEIS*/
.available{ margin-top:20px; }
.available__head{ display:flex; align-items:baseline; gap:12px; flex-wrap:wrap; margin-bottom:12px; }
.available__head h3{ margin:0; font-size:16px; color:var(--text); }
.available__hint{ margin:0; color:var(--muted); font-size:13px; }

.grid{ display:grid; grid-template-columns:repeat(auto-fit,minmax(280px,1fr)); gap:12px; }

.card{
  background:var(--surface); border:1px solid var(--outline); border-radius:14px;
  padding:16px; box-shadow:var(--shadow); cursor:grab;
  transition:transform .2s ease, box-shadow .2s ease;
}
.card:hover{ transform:translateY(-2px); box-shadow:0 10px 20px rgba(18,53,70,.07); }
.card:active{ cursor:grabbing; }

.card__header{ display:flex; justify-content:space-between; gap:8px; align-items:start; }
.card__title{ margin:0; font-size:15px; font-weight:700; color:var(--text); line-height:1.3; }
.card__add{
  all:unset; box-sizing:border-box; width:30px; height:30px; display:grid; place-items:center;
  border-radius:8px; background:#eef7ff; border:1px solid var(--primary); color:var(--primary);
  cursor:pointer; font-weight:700; font-size:18px; flex-shrink:0; transition:background-color .15s;
}
.card__add:hover{ background-color:#dbeafe; }
.card__added-icon{ width:30px; height:30px; display:grid; place-items:center; color:#38b48e; flex-shrink:0; }
.card__added-icon svg{ width:20px; height:20px; }

.card__desc{ margin:8px 0 12px; color:var(--muted); font-size:13px; line-height:1.5; }

.card__tags{ display:flex; flex-wrap:wrap; gap:8px; margin-top:auto; padding-top:8px; }

/* Pílulas com bolinha minúscula (9px) — combinam com os chips */
.pill{
  display:inline-flex; align-items:center; gap:8px;
  padding:4px 10px; border-radius:999px; font-size:12px; font-weight:700;
  color:#2d3748; background:#f7fafc; border:1px solid #e2e8f0; line-height:1;
}
.pill::before{
  content:""; width:9px; height:9px; border-radius:50%; flex-shrink:0; background:currentColor;
}

/* Cores by variant — a cor tinge só a bolinha via currentColor */
.pill[data-variant="Comportamental"]{ color:#4a6fcc; background:#fff; }
.pill[data-variant="Técnica"]{         color:#2a7d68; background:#fff; }
.pill[data-variant="Negócios"]{        color:#b7791f; background:#fff; }
.pill[data-variant="Idiomas"]{         color:#cc5452; background:#fff; }
.pill[data-variant="Outros"]{          color:#4a5568; background:#fff; }
.pill[data-variant="Erro"]{            color:#c53030; background:#fff; }

.pill--muted{ background:#f7fafc; color:#718096; border:1px solid #e2e8f0; }

.no-results-available{
  text-align:center; padding:30px 20px; color:var(--muted); font-style:italic; font-size:14px;
  grid-column:1 / -1;
}

.sr-only{
  position:absolute; width:1px; height:1px; padding:0; margin:-1px; overflow:hidden;
  clip:rect(0,0,0,0); white-space:nowrap; border:0;
}
</style>
