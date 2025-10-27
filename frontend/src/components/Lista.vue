<template>
  <div>
    <div class="page-title">
      <h1>Lista de Colaboradores</h1>
      <p>Use os filtros para encontrar colaboradores específicos</p>
    </div>

    <section class="filters">
      <div class="filters__container">
        <button class="round-btn" aria-label="Anterior" @click="prevPage" :disabled="currentPage === 1 || isLoading || !filtered.length">
          <svg viewBox="0 0 24 24" width="18" height="18"><path d="M15 18l-6-6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </button>
        <div class="filters__group">
          <div class="pill-select">
            <select v-model="deptSelected">
              <option value="">Todos os Departamentos</option>
              <option v-for="d in depts" :key="d">{{ d }}</option>
            </select>
            <span class="chev"><svg viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></span>
          </div>
          <div class="pill-select">
            <select v-model="roleSelected">
              <option value="">Todos os Cargos</option>
              <option v-for="r in roles" :key="r">{{ r }}</option>
            </select>
            <span class="chev"><svg viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></span>
          </div>
          <div class="pill-select">
            <select v-model="statusSelected">
              <option value="">Todos os Status</option>
              <option v-for="s in statuses" :key="s">{{ s }}</option>
            </select>
            <span class="chev"><svg viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></span>
          </div>
        </div>
        <button class="round-btn" aria-label="Próximo" @click="nextPage" :disabled="currentPage >= totalPages || isLoading || !filtered.length">
          <svg viewBox="0 0 24 24" width="18" height="18"><path d="M9 6l6 6-6 6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </button>
      </div>
      <div class="filters__search">
        <div class="search-input">
          <span class="search-ico" aria-hidden="true"><svg viewBox="0 0 24 24" width="16" height="16"><circle cx="11" cy="11" r="7" fill="none" stroke="currentColor" stroke-width="2"/><path d="M20 20l-3.5-3.5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg></span>
          <input v-model="searchQuery" type="text" placeholder="Pesquisar" aria-label="Pesquisar colaboradores"/>
          <button v-if="searchQuery" class="clear-btn" @click="searchQuery = ''" aria-label="Limpar busca">×</button>
        </div>
      </div>
    </section>

    <div v-if="isLoading" class="loading-state">Carregando lista de colaboradores...</div>

    <main v-else class="cards">
      <article v-for="c in paginated" :key="c.id" class="card">
        <div class="card__left">
          <div class="avatar" :style="{ background: c.avatarColor }">
            <span>{{ initials(c.name) }}</span>
          </div>
        </div>
        <div class="card__body">
          <div class="card__header">
            <router-link :to="{ name: 'LiderVerPerfil', params: { id: c.id } }" class="emp-name-link">
              <h3 class="emp-name">{{ c.name }}</h3>
            </router-link>
            <span v-if="c.badge" class="badge">{{ c.badge }}</span>
          </div>
          <ul class="meta">
            <li><img class="ico" src="@/assets/task-svgrepo-com.svg" alt="cargo" /><span class="line">{{ c.role }}</span></li>
            <li><img class="ico" src="@/assets/job-search-symbol-of-suitcase-and-curriculum-paper-svgrepo-com.svg" alt="departamento" /><span class="line">{{ c.department }}</span></li>
            <li><img class="ico" src="@/assets/telephone-svgrepo-com.svg" alt="telefone" /><span class="line email">{{ c.email }}</span></li>
            <li><img class="ico" src="@/assets/email-1-svgrepo-com.svg" alt="email" /><span class="line">{{ c.phone }}</span></li>
          </ul>
        </div>
      </article>

      <div v-if="!filtered.length && !isLoading" class="no-results">
        Nenhum colaborador encontrado com os filtros selecionados.
      </div>
    </main>

    <footer v-if="!isLoading" class="pager">
      Página {{ currentPage }} de {{ totalPages }} •
      Mostrando {{ rangeStart }}–{{ rangeEnd }} de {{ filtered.length }} colaboradores
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const AVATAR_COLORS = [ "#e57373", "#f06292", "#ba68c8", "#9575cd", "#7986cb", "#64b5f6", "#4fc3f7", "#4dd0e1", "#4db6ac", "#81c784", "#aed581", "#dce775", "#fff176", "#ffd54f", "#ffb74d", "#ff8a65", "#a1887f", "#90a4ae" ];

const isLoading = ref(true);
const deptSelected = ref("");
const roleSelected = ref("");
const statusSelected = ref("");
const searchQuery = ref("");
const depts = ref([]);
const roles = ref([]);
const statuses = ref(["Ativo", "Férias", "Inativo"]);
const collaborators = ref([]);
const currentPage = ref(1);
const pageSize = ref(6);

const filtered = computed(() => {
  let list = collaborators.value.filter(c => {
    const okDept = deptSelected.value ? c.department === deptSelected.value : true;
    const okRole = roleSelected.value ? c.role === roleSelected.value : true;
    const okStatus = statusSelected.value ? c.status === statusSelected.value : true;
    return okDept && okRole && okStatus;
  });
  const q = normalize(searchQuery.value);
  if (!q) return list;
  return list.filter(c => {
    const haystack = [ c.name, c.role, c.department, c.email, c.phone ].map(normalize).join(" ");
    return haystack.includes(q);
  });
});

const totalPages = computed(() => {
  const t = Math.ceil(filtered.value.length / pageSize.value);
  return Math.max(1, t || 1);
});

const paginated = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filtered.value.slice(start, start + pageSize.value);
});

const rangeStart = computed(() => {
  if (!filtered.value.length) return 0;
  return (currentPage.value - 1) * pageSize.value + 1;
});

const rangeEnd = computed(() => {
  return Math.min(currentPage.value * pageSize.value, filtered.value.length);
});

watch([deptSelected, roleSelected, statusSelected, searchQuery, collaborators], () => { currentPage.value = 1; });
watch(filtered, () => { if (currentPage.value > totalPages.value) { currentPage.value = totalPages.value; } });

// ==============================================
// CORREÇÃO FINAL AQUI: Usando 'id: dto.codigo'
// ==============================================
function mapToCollaborator(dto, index) {
  console.log('API DTO:', dto); // Log extra para ver todos os dados recebidos
  return {
    id: dto.codigo, // <-- CORRIGIDO AQUI (usa o campo 'codigo' da API)
    name: dto.nomeCompleto,
    role: dto.tituloProfissional || dto.nomePerfil || "Não Definido",
    department: dto.nomeArea || "Não Definido",
    email: dto.email,
    phone: dto.telefone,
    status: "Ativo", // Assumindo 'Ativo', pode precisar ajustar com dados reais da API
    badge: dto.nomePerfil || null,
    avatarColor: AVATAR_COLORS[index % AVATAR_COLORS.length]
  };
}
// ==============================================

async function fetchData() {
  isLoading.value = true;
  try {
    const response = await axios.get("/api/funcionario");
    const data = response.data;
    collaborators.value = Array.isArray(data) ? data.map((dto, i) => mapToCollaborator(dto, i)) : [];

    // Log para confirmar que o mapeamento funcionou
    console.log("Colaboradores Mapeados:", collaborators.value);

    depts.value = [...new Set(collaborators.value.map(c => c.department))].sort();
    roles.value = [...new Set(collaborators.value.map(c => c.role))].sort();
  } catch (error) {
    console.error("Falha ao buscar dados da API com Axios:", error);
    collaborators.value = [];
  } finally {
    isLoading.value = false;
  }
}

function initials(name) {
  const p = (name || "").trim().split(" ");
  if (!p.length || !p[0]) return '';
  const first = p[0][0] || '';
  const last = p.length > 1 && p[p.length - 1] ? (p[p.length - 1][0] || '') : '';
  return (first + last).toUpperCase();
}

function normalize(str) {
  return (str || "").toString().toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

function prevPage() { if (currentPage.value > 1) currentPage.value--; }
function nextPage() { if (currentPage.value < totalPages.value) currentPage.value++; }
function handleVoltar() { router.go(-1); }

onMounted(async () => { await fetchData(); });
</script>


<style scoped>
/* Estilos anteriores mantidos */
.page-title { text-align: center; margin: 18px 0 6px; }
.page-title h1 { color: var(--title); font-size: 28px; margin: 6px 0; }
.page-title p { margin: 0; font-size: 14px; color: var(--subtitle); }
.filters { margin: 12px 0 8px; }
.filters__container { max-width: 1100px; margin: 0 auto; padding: 0 16px; display: grid; grid-template-columns: 36px 1fr 36px; align-items: center; gap: 12px; }
.filters__group { display: flex; justify-content: center; gap: 10px; flex-wrap: wrap; }
.round-btn { width: 36px; height: 36px; border-radius: 50%; border: 1px solid var(--pill-border, #d8e9e9); background: var(--pill-bg, #f0f7f7); color: var(--pill-text, #4a626b); display: grid; place-items: center; cursor: pointer; transition: background-color 0.15s ease; }
.round-btn:hover:not(:disabled) { background-color: #e0eaea; }
.round-btn:disabled { opacity: .5; cursor: not-allowed; }
.pill-select { position: relative; background: var(--pill-bg, #f0f7f7); border: 1px solid var(--pill-border, #d8e9e9); border-radius: 10px; padding-right: 34px; height: 36px; display: flex; align-items: center; transition: border-color 0.15s ease; }
.pill-select:hover { border-color: #b0c7c7; }
.pill-select select { appearance: none; border: none; background: transparent; padding: 0 12px; height: 100%; width: 100%; font-size: 14px; color: var(--pill-text, #4a626b); outline: none; cursor: pointer; }
.pill-select .chev { position: absolute; right: 8px; top: 50%; transform: translateY(-50%); color: var(--pill-text, #4a626b); pointer-events: none; }
.filters__search { margin-top: 10px; display: flex; justify-content: center; }
.search-input { background: var(--pill-bg, #f0f7f7); border: 1px solid var(--pill-border, #d8e9e9); border-radius: 12px; height: 40px; display: grid; grid-template-columns: 30px 1fr 30px; align-items: center; gap: 6px; padding: 0 8px; min-width: 320px; max-width: 620px; width: 60%; transition: border-color 0.15s ease, box-shadow 0.15s ease; }
.search-input:focus-within { border-color: var(--primary, #382eceff); box-shadow: 0 0 0 2px rgba(56, 46, 206, 0.2); }
.search-input input { border: none; outline: none; background: transparent; font-size: 14px; color: var(--pill-text, #4a626b); }
.search-input input::placeholder { color: #9ab0b8; }
.search-ico { display: grid; place-items: center; color: var(--pill-text, #4a626b); }
.clear-btn { border: none; background: transparent; color: var(--pill-text, #4a626b); font-size: 20px; line-height: 1; cursor: pointer; padding: 0; display: grid; place-items: center; opacity: 0.7; }
.clear-btn:hover { opacity: 1; }
.cards { max-width: 1240px; margin: 16px auto 14px; padding: 0 20px; display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; }
.card { background: var(--card); border: 1px solid var(--border); border-radius: 12px; box-shadow: var(--shadow); display: grid; grid-template-columns: auto 1fr; gap: 16px; padding: 20px; align-items: start; transition: transform 0.2s ease, box-shadow 0.2s ease; }
.card:hover { transform: translateY(-3px); box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08); }
.avatar { width: 52px; height: 52px; border-radius: 50%; display: grid; place-items: center; color: #fff; font-weight: 700; letter-spacing: .3px; flex-shrink: 0; font-size: 18px; }
.card__left { display: flex; justify-content: center; padding-top: 4px; }
.card__body { min-width: 0; }
.card__header { display: flex; align-items: baseline; gap: 10px; margin-bottom: 8px; flex-wrap: wrap; }
.emp-name-link { text-decoration: none; color: inherit; flex-grow: 1; min-width: 0; cursor: pointer; }
.emp-name-link:hover .emp-name { color: var(--primary, #382eceff); text-decoration: underline; }
.emp-name { margin: 0; color: var(--title); font-size: 17px; font-weight: 700; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; transition: color 0.15s ease; }
.badge { background: var(--badge-bg, #dff6f2); color: var(--badge-text, #2b8a78); border: 1px solid #bfece5; padding: 4px 8px; border-radius: 999px; font-size: 11px; white-space: nowrap; flex-shrink: 0; font-weight: 600; }
.meta { list-style: none; padding: 0; margin: 0; display: grid; gap: 6px; }
.meta li { display: grid; grid-template-columns: 16px 1fr; align-items: center; column-gap: 8px; color: var(--subtitle); font-size: 13px; min-width: 0; }
.ico { width: 16px; height: 16px; display: block; object-fit: contain; flex-shrink: 0; opacity: 0.7; }
.line { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.loading-state, .no-results { grid-column: 1 / -1; text-align: center; padding: 40px 20px; color: var(--subtitle); font-style: italic; min-height: 200px; display: flex; align-items: center; justify-content: center; }
.pager { text-align: center; color: var(--subtitle); font-size: 13px; margin: 8px 0 22px; }
:root { --bg: #f3f9f9; --card: #ffffff; --border: #e3eeee; --shadow: 0 2px 10px rgba(0, 0, 0, 0.06); --title: #274b57; --subtitle: #6e7f89; --primary: #382eceff; --primary-600: #382eceff; --pill-bg: #f0f7f7; --pill-border: #d8e9e9; --pill-text: #4a626b; --badge-bg: #dff6f2; --badge-text: #2b8a78; --blue: #2563eb; --muted: #eaf5f5; }
@media (max-width: 1024px) { .cards { grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); } }
</style>
