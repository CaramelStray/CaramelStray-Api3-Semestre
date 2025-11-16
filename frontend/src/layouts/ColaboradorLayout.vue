<template>
  <div class="page">
    <SideMenuColaborador />

    <div class="main-panel">
      <header class="topbar">
        <div class="brand">
          <div class="brand__logo">
            <svg viewBox="0 0 24 24" width="18" height="18">
              <circle cx="12" cy="8" r="3.5" fill="#382eceff"/>
              <path d="M5 20c0-3.5 3-6.5 7-6.5s7 3 7 6.5" fill="none" stroke="#382eceff" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="brand__text">
            <div class="brand__title">{{ $route.meta.title || 'Sistema RH' }}</div>
            <div class="brand__subtitle">{{ $route.meta.subtitle || 'Colaborador' }}</div>
          </div>
        </div>
        <div class="actions">
          <button class="btn-outline" @click="logout">Sair</button>
        </div>
      </header>

      <div class="content-area">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const logout = () => {
  // 1. Remove o token (mesma chave usada no beforeEach)
  localStorage.removeItem('token');

  // 2. Redireciona para a tela de login
  router.push('/login');
};
</script>

<style scoped>
/* Estilos GLOBAIS do Layout */
.page {
  --bg:#f3f9f9; --card:#ffffff; --border:#e3eeee; --shadow:0 2px 12px rgba(0,0,0,.06); --title:#274b57; --subtitle:#6e7f89; --primary:#5cc2c1; --primary-600:#3fb2b2; --muted:#eaf5f5; --blue:#2563eb;

  /* === CORREÇÃO SCROLL: Usa Flexbox e permite rolagem no main-panel === */
  display: flex; /* Muda para Flexbox */
  width: 100%;
  min-height: 100vh; /* Garante altura mínima */
  background: var(--bg);
  color: var(--subtitle);
  font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, Arial;
  /* overflow: hidden; REMOVIDO */
}

/* Sidebar: Estilos vêm do componente. Ele precisa ter width e position sticky */

/* Nova Div para Área Principal */
.main-panel {
  flex: 1; /* Ocupa o restante da largura */
  display: flex;
  flex-direction: column;
  height: 100vh; /* Tenta ocupar a altura total */
  overflow-y: auto; /* PERMITE ROLAGEM AQUI */
  min-width: 0; /* Necessário para flexbox */
}

/* Topbar */
.topbar{
  background:var(--card);
  border-bottom:1px solid var(--border);
  box-shadow:var(--shadow);
  display: flex;
  justify-content: space-between;
  align-items:center;
  padding:12px 20px;
  position: sticky; /* Mantém no topo DENTRO do main-panel */
  top: 0;
  z-index: 10;
  box-sizing: border-box;
  flex-shrink: 0; /* Não encolhe */
}
.brand{ display:flex; align-items:center; gap:12px; min-width: 0; }
.brand__logo{ width:36px; height:36px; border-radius:10px; display:grid; place-items:center; background:#eaf5f5; border:1px solid var(--border); flex-shrink: 0; }
.brand__text { min-width: 0; }
.brand__title{ color:var(--title); font-weight:600; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.brand__subtitle{ font-size:13px; color:var(--subtitle); margin-top:2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.actions{ display:flex; gap:10px; flex-shrink: 0; }
.btn-outline{ display:inline-flex; align-items:center; height:36px; padding:0 16px; border-radius:10px; border:1px solid var(--border); background:var(--card); color:var(--title); cursor:pointer; white-space: nowrap; }
.btn-outline:hover { background-color: var(--muted); }

/* Área de Conteúdo */
.content-area {
  padding: 20px;
  background-color: var(--bg);
  box-sizing: border-box;
  flex-grow: 1; /* Ocupa o espaço restante abaixo do topbar */
}

/* Responsivo */
@media (max-width:768px){
  .page {
    display: flex; /* Mantém flex */
    flex-direction: column; /* Coloca topbar em cima */
  }
  /* Sidebar some (controlado externamente ou via JS/CSS global) */
  .main-panel {
    width: 100%; /* Ocupa largura total */
    height: 100vh; /* Continua tentando ocupar altura */
    overflow-y: auto; /* Mantém scroll */
  }
  .topbar {
    position: sticky; /* Pode continuar sticky */
  }
  .content-area { padding: 15px; }
}
</style>
