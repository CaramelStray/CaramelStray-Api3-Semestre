<template>
  <div class="page">
    <!-- Topbar -->
    <header class="topbar">
      <div class="brand">
        <span class="brand-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
            <rect x="3" y="3" width="7" height="7" rx="1.5"></rect>
            <rect x="14" y="3" width="7" height="7" rx="1.5"></rect>
            <rect x="3" y="14" width="7" height="7" rx="1.5"></rect>
            <rect x="14" y="14" width="7" height="7" rx="1.5"></rect>
          </svg>
        </span>
        <div class="brand-text">
          <strong>Sistema Corporativo</strong>
          <small>Painel de Controle</small>
        </div>
      </div>

      <button class="btn-ghost" type="button" @click="onLogout">
        <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor" aria-hidden="true">
          <path d="M16 17l1.41-1.41L14.83 13H21v-2h-6.17l2.58-2.59L16 7l-5 5 5 5z"></path>
          <path d="M3 5h8v2H5v10h6v2H3z"></path>
        </svg>
        <span>Sair</span>
      </button>
    </header>

    <!-- Conteúdo -->
    <main class="content">
      <section class="hero">
        <h1>Bem-vindo ao Sistema</h1>
        <p>Selecione uma das opções abaixo para acessar as funcionalidades do sistema</p>
      </section>

      <section class="cards">
        <article class="card">
          <div class="card-icon">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="currentColor" aria-hidden="true">
              <path d="M12 12a5 5 0 10-5-5 5 5 0 005 5zm0 2c-4.33 0-8 1.92-8 4.29V21h16v-2.71C20 15.92 16.33 14 12 14z"/>
            </svg>
          </div>
          <h3 class="card-title">Visualizar Colaboradores</h3>
          <p class="card-desc">Consulte e gerencie os dados dos colaboradores da empresa</p>
          <button class="btn" @click="go('colaboradores')">Acessar</button>
        </article>

        <article class="card">
          <div class="card-icon">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="currentColor" aria-hidden="true">
              <path d="M11 17h2v-2h-2zm0-4h2V7h-2zm8-9H5a2 2 0 00-2 2v16l4-4h12a2 2 0 002-2V6a2 2 0 00-2-2z"/>
            </svg>
          </div>
          <h3 class="card-title">Cadastrar Perguntas</h3>
          <p class="card-desc">Adicione e configure perguntas para avaliações e formulários</p>
          <button class="btn" @click="go('perguntas')">Acessar</button>
        </article>

        <article class="card">
          <div class="card-icon">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="currentColor" aria-hidden="true">
              <path d="M3 13h6v8H3zm12-6h6v14h-6zM8 3h6v18H8z"/>
            </svg>
          </div>
          <h3 class="card-title">Dashboard</h3>
          <p class="card-desc">Visualize relatórios e métricas importantes da empresa</p>
          <button class="btn" @click="go('dashboard')">Acessar</button>
        </article>
      </section>
    </main>
  </div>
</template>

<script>
export default {
  name: "Inicio",
  methods: {
    go(destino) { console.log("Ir para:", destino); },
    onLogout() { console.log("Sair clicado"); },
  },
};
</script>

<style scoped>
/* CORES FIXAS (claro) */
.page {
  min-height: 100vh;
  background-color: #f4f9f8; /* fundo verdinho bem claro */
  display: flex;
  flex-direction: column;
}

/* Topbar branca */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  background: #ffffff;
  border-bottom: 1px solid #e6edf2; /* cinza bem claro */
}

.brand { display: flex; align-items: center; gap: 12px; }
.brand-icon {
  display: grid; place-items: center;
  width: 36px; height: 36px;
  color: #0d9488;               /* teal */
  background: #e8f7f5;          /* círculo claro */
  border: 1px solid #cfecea;
  border-radius: 10px;
}
.brand-text strong { display:block; font-size:15px; line-height:1.1; color:#0f172a; }
.brand-text small { display:block; margin-top:2px; color:#64748b; font-size:12px; }

.btn-ghost {
  display:inline-flex; align-items:center; gap:8px;
  padding:8px 12px; border-radius:10px;
  background:#ffffff; color:#0f172a;
  border:1px solid #e6edf2; cursor:pointer;
}
.btn-ghost:hover { background:#f7fafc; }

/* Conteúdo */
.content { width:min(1100px, 92vw); margin: 28px auto 60px; }
.hero { text-align:center; margin-bottom:26px; }
.hero h1 { margin:0 0 10px; font-size:28px; color:#0f172a; }
.hero p { margin:0; color:#64748b; }

/* Cards: BRANCOS */
.cards {
  display:grid;
  grid-template-columns: repeat(3, 1fr);
  gap:18px;
}

.card {
  background:#ffffff;                /* BRANCO */
  border:1px solid #e6edf2;          /* borda suave */
  border-radius:14px;
  padding:22px 18px 16px;
  box-shadow: 0 6px 18px rgba(15,23,42,0.06);
  display:flex; flex-direction:column; text-align:center;
}

.card-icon {
  margin: 4px auto 10px;
  width:58px; height:58px; border-radius:999px;
  background:#d9f2f0;                /* bolinha verde clara */
  color:#0d9488;                     /* ícone teal */
  display:grid; place-items:center;
  border:1px solid #c7e8e5;
}

.card-title { margin:8px 0 6px; font-size:16px; font-weight:600; color:#0f172a; }
.card-desc { margin:0 0 16px; color:#64748b; min-height:36px; }

/* Botão principal em teal claro */
.btn {
  margin-top:auto;
  border:1px solid #9fdad6;
  background:#dff4f2;
  color:#0f766e;
  border-radius:10px;
  padding:10px 14px;
  font-weight:600; cursor:pointer;
}
.btn:hover { background:#d3efec; }

/* Responsivo */
@media (max-width: 960px) { .cards { grid-template-columns: 1fr 1fr; } }
@media (max-width: 600px) { .cards { grid-template-columns: 1fr; } }
</style>
