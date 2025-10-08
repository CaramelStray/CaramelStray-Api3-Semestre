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

    <!-- Cards -->
    <main class="cards">
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
    </main>

    <footer class="pager">
      Página 1 de 2 • {{ filtered.length }} colaboradores encontrados
    </footer>
  </div>
</template>

<script>
export default {
  name: "Colaboradores",
  data() {
    return {
      deptSelected: "",
      roleSelected: "",
      statusSelected: "",
      searchQuery: "", // <-- NOVO
      depts: ["Recursos Humanos", "Tecnologia", "Marketing", "Financeiro", "Vendas", "Comercial", "Design"],
      roles: ["Analista", "Desenvolvedor Frontend", "Gerente", "Designer UX/UI", "Coordenador", "Analista Financeiro"],
      statuses: ["Ativo", "Férias", "Inativo"],
      collaborators: [
        { name: "Ana Silva Santos", role: "Analista de Recursos Humanos", department: "Recursos Humanos", email: "ana.santos@empresa.com", phone: "(11) 99999-0001", status: "Ativo", avatarColor: "#63c7c5" },
        { name: "Carlos Eduardo Lima", role: "Desenvolvedor Frontend", department: "Tecnologia", email: "carlos.lima@empresa.com", phone: "(11) 99999-0002", status: "Ativo", avatarColor: "#5bb7c8" },
        { name: "Mariana Costa Ferreira", role: "Gerente de Marketing", department: "Marketing", email: "mariana.ferreira@empresa.com", phone: "(11) 99999-0003", status: "Ativo", avatarColor: "#7cc0cb" },
        { name: "João Pedro Oliveira", role: "Analista Financeiro", department: "Financeiro", email: "joao.oliveira@empresa.com", phone: "(11) 99999-0004", status: "Ativo", avatarColor: "#61b2c0" },
        { name: "Fernanda Ribeiro Cruz", role: "Designer UX/UI", department: "Tecnologia", email: "fernanda.cruz@empresa.com", phone: "(11) 99999-0005", status: "Ativo", avatarColor: "#60c0b4" },
        { name: "Ricardo Almeida Souza", role: "Coordenador de Vendas", department: "Vendas", email: "ricardo.souza@empresa.com", phone: "(11) 99999-0006", status: "Ativo", avatarColor: "#7db5b9" }
      ]
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
  methods: {
    initials(name) {
      const p = name.trim().split(" ");
      return (p[0][0] + p[p.length - 1][0]).toUpperCase();
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
