<template>
  <div class="page">
    <!-- Cabeçalho branco -->
    <div class="topbar">
      <button class="icon-btn" aria-label="Voltar">
        <svg viewBox="0 0 24 24" width="20" height="20">
          <path d="M15 18l-6-6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
      <div class="topbar__title">
        <div class="app-title">Colaboradores</div>
        <div class="app-subtitle">Visualize e gerencie os colaboradores da empresa</div>
      </div>
      <div class="topbar__spacer"></div>
    </div>

    <!-- Título da página -->
    <div class="page-title">
      <h1>Lista de Colaboradores</h1>
      <p>Use os filtros para encontrar colaboradores específicos</p>
    </div>

    <!-- Filtros -->
    <section class="filters">
      <div class="filters__container">
        <button class="round-btn" aria-label="Anterior">
          <svg viewBox="0 0 24 24" width="18" height="18">
            <path d="M15 18l-6-6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <div class="filters__group">
          <div class="pill-select">
            <select v-model="deptSelected">
              <option value="">Todos os Departamentos</option>
              <option v-for="d in depts" :key="d">{{ d }}</option>
            </select>
            <span class="chev">
              <svg viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
            </span>
          </div>

          <div class="pill-select">
            <select v-model="roleSelected">
              <option value="">Todos os Cargos</option>
              <option v-for="r in roles" :key="r">{{ r }}</option>
            </select>
            <span class="chev">
              <svg viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
            </span>
          </div>

          <div class="pill-select">
            <select v-model="statusSelected">
              <option value="">Todos os Status</option>
              <option v-for="s in statuses" :key="s">{{ s }}</option>
            </select>
            <span class="chev">
              <svg viewBox="0 0 24 24" width="16" height="16"><path d="M6 9l6 6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
            </span>
          </div>
        </div>

        <button class="round-btn" aria-label="Próximo">
          <svg viewBox="0 0 24 24" width="18" height="18">
            <path d="M9 6l6 6-6 6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>

      <div class="filters__search">
        <div class="search-input">
          <span class="search-ico" aria-hidden="true">
            <svg viewBox="0 0 24 24" width="16" height="16">
              <circle cx="11" cy="11" r="7" fill="none" stroke="currentColor" stroke-width="2"/>
              <path d="M20 20l-3.5-3.5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </span>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Pesquisar"
            aria-label="Pesquisar colaboradores"
          />
          <button v-if="searchQuery" class="clear-btn" @click="searchQuery = ''" aria-label="Limpar busca">×</button>
        </div>
      </div>
    </section>
    <div v-if="isLoading" class="loading-state">
      Carregando lista de colaboradores...
    </div>
    <!-- Cards -->
    <main v-else class="cards">
      <article v-for="c in filtered" :key="c.email" class="card">
        <div class="card__left">
          <div class="avatar" :style="{ background: c.avatarColor }">
            <span>{{ initials(c.name) }}</span>
          </div>
        </div>

        <div class="card__body">
          <div class="card__header">
            <h3 class="emp-name">{{ c.name }}</h3>
            <span v-if="c.badge" class="badge">{{ c.badge }}</span>
          </div>

          <ul class="meta">
            <li><i class="ico"></i><span class="line">{{ c.role }}</span></li>
            <li><i class="ico"></i><span class="line">{{ c.department }}</span></li>
            <li><i class="ico"></i><span class="line email">{{ c.email }}</span></li>
            <li><i class="ico"></i><span class="line">{{ c.phone }}</span></li>
          </ul>
        </div>
      </article>
      <div v-if="!filtered.length && !isLoading" class="no-results">
        Nenhum colaborador encontrado com os filtros selecionados.
      </div>
    </main>

    <footer v-if="!isLoading" class="pager">
      Página 1 de 2 • {{ filtered.length }} colaboradores encontrados
    </footer>
  </div>
</template>

<script>

import axios from 'axios';

const AVATAR_COLORS = [
  "#e57373", "#f06292", "#ba68c8", "#9575cd", "#7986cb", 
  "#64b5f6", "#4fc3f7", "#4dd0e1", "#4db6ac", "#81c784",
  "#aed581", "#dce775", "#fff176", "#ffd54f", "#ffb74d", 
  "#ff8a65", "#a1887f", "#90a4ae"
];

export default {
  name: "Colaboradores",
  data() {
    return {
      isLoading: true,
      deptSelected: "",
      roleSelected: "",
      statusSelected: "",
      searchQuery: "", // <-- NOVO
      depts:[],
      roles:[],
      statuses: ["Ativo", "Férias", "Inativo"],
      collaborators: []
    };
  },
  computed: {
    filtered() {
      // filtros por select
      let list = this.collaborators.filter(c => {
        const okDept = this.deptSelected ? c.department === this.deptSelected : true;
        const okRole = this.roleSelected ? c.role === this.roleSelected : true;
        const okStatus = this.statusSelected ? c.status === this.statusSelected : true;
        return okDept && okRole && okStatus;
      });

      // filtro por texto (insensível a acento/maiúsculas)
      const q = this.normalize(this.searchQuery);
      if (!q) return list;

      return list.filter(c => {
        const haystack = [
          c.name, c.role, c.department, c.email, c.phone
        ].map(this.normalize).join(" ");
        return haystack.includes(q);
      });
    }
  },
  async mounted() {
    await this.fetchData();
  },
  methods: {
    mapToCollaborator(dto, index) {
        return {
            name: dto.nomeCompleto,
            role: dto.tituloProfissional || dto.nomePerfil || "Não Definido", 
            department: dto.nomeArea || "Não Definido",
            email: dto.email,
            phone: dto.telefone,
            status: "Ativo",
            badge: dto.nomePerfil || null,
            avatarColor: AVATAR_COLORS[index % AVATAR_COLORS.length] 
        };
    },
    
    // ALTERADO: Função de busca agora usa Axios
    async fetchData() {
        this.isLoading = true;
        try {
            // Chamada com Axios: É mais concisa e já retorna o corpo da resposta em .data
            const response = await axios.get("/api/funcionario");
            
            // Axios não precisa de response.json(), os dados já estão em response.data
            const data = response.data; 
            console.log("Resposta bruta da API:", response);
            console.log("Array de Dados recebido (data):", data);
            this.collaborators = Array.isArray(data) 
            ? data.map(dto => this.mapToCollaborator(dto, data.indexOf(dto)))
            : [];
            // Popula os filtros de Departamento e Cargo
            this.depts = [...new Set(this.collaborators.map(c => c.department))].sort();
            this.roles = [...new Set(this.collaborators.map(c => c.role))].sort();

        } catch (error) {
            // A manipulação de erros com Axios é mais robusta
            console.error("Falha ao buscar dados da API com Axios:", error);
            // Se o erro tiver uma resposta, você pode acessar: error.response.status
        } finally {
            this.isLoading = false;
        }
    },  

initials(name) {
    const p = name.trim().split(" ");
    if (p.length === 0) return '';
    const first = p[0][0];
    const last = p.length > 1 ? p[p.length - 1][0] : '';
    return (first + last).toUpperCase();
},
    normalize(str) {
      return (str || "")
        .toString()
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, ""); // remove acentos
    }
  }
};
</script>

<style scoped>
.page {
  /* variáveis locais */
  --bg: #f3f9f9;
  --card: #ffffff;
  --border: #e3eeee;
  --shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  --title: #274b57;
  --subtitle: #6e7f89;
  --primary: #5cc2c1;
  --primary-600: #3fb2b2;
  --pill-bg: #f0f7f7;
  --pill-border: #d8e9e9;
  --pill-text: #4a626b;
  --badge-bg: #dff6f2;
  --badge-text: #2b8a78;

  min-height: 100vh;
  background: var(--bg);
  color: var(--subtitle);
  font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans";
}

/* ===== Cabeçalho ===== */
.topbar {
  background: var(--card);
  border-bottom: 1px solid var(--border);
  box-shadow: var(--shadow);
  padding: 14px 18px;
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 12px;
}
.icon-btn {
  width: 36px; height: 36px;
  border-radius: 10px;
  border: 1px solid var(--pill-border);
  background: var(--pill-bg);
  color: var(--pill-text);
  display: grid; place-items: center;
  cursor: pointer;
}
.topbar__title .app-title { font-weight: 600; color: var(--title); }
.topbar__title .app-subtitle { font-size: 13px; color: var(--subtitle); opacity: .9; }
.topbar__spacer { width: 36px; }

/* ===== Título principal ===== */
.page-title { text-align: center; margin: 18px 0 6px; }
.page-title h1 { color: var(--title); font-size: 28px; margin: 6px 0; }
.page-title p { margin: 0; font-size: 14px; }

/* ===== Filtros centralizados com setas laterais ===== */
.filters { margin: 12px 0 8px; }
.filters__container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 16px;
  display: grid;
  grid-template-columns: 36px 1fr 36px; /* seta esquerda | filtros | seta direita */
  align-items: center;
  gap: 12px;
}
.filters__group {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
}
.round-btn {
  width: 36px; height: 36px;
  border-radius: 50%;
  border: 1px solid var(--pill-border);
  background: var(--pill-bg);
  color: var(--pill-text);
  display: grid; place-items: center;
  cursor: pointer;
}

.pill-select {
  position: relative;
  background: var(--pill-bg);
  border: 1px solid var(--pill-border);
  border-radius: 10px;
  padding-right: 34px;
  height: 36px;
  display: flex; align-items: center;
}
.pill-select select {
  appearance: none; border: none; background: transparent;
  padding: 0 12px; height: 100%;
  font-size: 14px; color: var(--pill-text); outline: none;
}
.pill-select .chev {
  position: absolute; right: 8px; top: 50%;
  transform: translateY(-50%); color: var(--pill-text);
}

.filters__search {
  margin-top: 10px;
  display: flex;
  justify-content: center;
}
.search-input {
  background: var(--pill-bg);
  border: 1px solid var(--pill-border);
  border-radius: 12px;
  height: 40px;
  display: grid;
  grid-template-columns: 30px 1fr 30px; /* ícone | input | limpar */
  align-items: center;
  gap: 6px;
  padding: 0 8px;
  min-width: 320px;
  max-width: 620px;
  width: 60%;
}
.search-input input {
  border: none; outline: none;
  background: transparent;
  font-size: 14px;
  color: var(--pill-text);
}
.search-ico {
  display: grid; place-items: center;
  color: var(--pill-text);
}
.clear-btn {
  border: none; background: transparent; color: var(--pill-text);
  font-size: 20px; line-height: 1; cursor: pointer;
}

/* ===== Cards ===== */
.cards {
  max-width: 1240px;
  margin: 6px auto 14px;
  padding: 0 20px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
.card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: var(--shadow);
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 16px;
  padding: 20px;
  align-items: start;
}
@media (max-width: 1024px) { .cards { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .cards { grid-template-columns: 1fr; } }

.avatar { width: 52px; height: 52px; border-radius: 50%; display: grid; place-items: center; color: #fff; font-weight: 700; letter-spacing: .3px; }
.card__header { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; }
.emp-name { margin: 0; color: var(--title); font-size: 17px; font-weight: 700; }
.badge { background: var(--badge-bg); color: var(--badge-text); border: 1px solid #bfece5; padding: 4px 8px; border-radius: 999px; font-size: 11px; }
.meta { list-style: none; padding: 0; margin: 0; display: grid; gap: 6px; }
.meta li { display: grid; grid-template-columns: 16px 1fr; align-items: center; column-gap: 8px; color: var(--subtitle); font-size: 13px; }
.ico { width: 16px; height: 16px; border-radius: 50%; background: linear-gradient(180deg, var(--primary) 0%, var(--primary-600) 100%); }

.pager { text-align: center; color: var(--subtitle); font-size: 13px; margin: 8px 0 22px; }
</style>
