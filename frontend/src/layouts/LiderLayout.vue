<template>
  <div class="page">
    <aside class="sidebar">
      <div class="sidebar__header">
        <div class="sidebar__logo">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <rect x="3" y="3" width="18" height="18" rx="2" fill="none" stroke="currentColor" stroke-width="2"/>
            <path d="M9 3v18M15 3v18M3 9h18M3 15h18" stroke="currentColor" stroke-width="2"/>
          </svg>
        </div>
        <div class="sidebar__text">
          <div class="sidebar__title">Sistema RH</div>
          <div class="sidebar__subtitle">Liderança</div>
        </div>
      </div>
      <div class="sidebar__user">
        <div class="user-avatar">L</div>
        <div class="user-info">
          <div class="user-name">Líder</div>
          <div class="user-email">lider@empresa.com</div>
        </div>
      </div>

      <nav class="sidebar__nav">
        <router-link :to="{ name: 'LiderInicio' }" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" fill="none" stroke="currentColor" stroke-width="2"/>
            <path d="M9 22V12h6v10" fill="none" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>Início</span>
        </router-link>

        <router-link :to="{ name: 'LiderFuncionarios' }" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <circle cx="12" cy="8" r="3" fill="none" stroke="currentColor" stroke-width="2"/>
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>Colaboradores</span>
        </router-link>

        <router-link :to="{ name: 'LiderAvaliacoes' }" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" fill="none" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>Avaliações</span>
        </router-link>

        <router-link :to="{ name: 'LiderDashboard' }" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M4 18V6m5 12V9m5 9V12m5 6V8" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>Dashboard</span>
        </router-link>

        <router-link :to="{ name: 'LiderCadastrarColaborador' }" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        <span>Cadastrar Colaborador</span>
        </router-link>
      </nav>

      <div class="sidebar__footer">
        <button class="nav-item" @click="logout">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4m7 14l5-5-5-5m5 5H9" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>Sair</span>
        </button>
      </div>
    </aside>

    <header class="topbar">
      <div class="brand">
        <div class="brand__logo">
          <svg viewBox="0 0 24 24" width="18" height="18">
            <circle cx="12" cy="8" r="3" fill="#382eceff"/>
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="#382eceff" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="brand__text">
          <div class="brand__title">{{ $route.meta.title || 'Sistema RH' }}</div>
          <div class="brand__subtitle">{{ $route.meta.subtitle || 'Liderança' }}</div>
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
</template>

<script setup>
// Script setup simples, sem importação do SideMenu
import { useRoute } from 'vue-router';
const route = useRoute();

const logout = () => { console.log("Sair LiderLayout"); };
</script>

<style scoped>
/* Estilos como estavam no seu código (incluindo os de sidebar) */
.page {
  --bg:#f3f9f9; --card:#ffffff; --border:#e3eeee; --shadow:0 2px 12px rgba(0,0,0,.06); --title:#274b57; --subtitle:#6e7f89; --primary:#5cc2c1; --primary-600:#3fb2b2; --muted:#eaf5f5; --blue:#2563eb;
  height: 100vh; overflow: hidden; background:var(--bg); color:var(--subtitle); font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, Arial;
  display: grid; grid-template-columns: 240px 1fr; grid-template-rows: auto 1fr;
  grid-template-areas: "sidebar topbar" "sidebar content";
}
/* Sidebar */
.sidebar { grid-area: sidebar; background: var(--card); border-right: 1px solid var(--border); display: flex; flex-direction: column; height: 100vh; position: sticky; top: 0; padding: 20px 0; box-sizing: border-box; z-index: 20; overflow-y: auto; }
.sidebar__header { padding: 0 20px 20px; border-bottom: 1px solid var(--border); margin-bottom: 16px; display: flex; align-items: center; gap: 12px; flex-shrink: 0; }
.sidebar__logo { width: 40px; height: 40px; background: var(--blue); border-radius: 8px; display: flex; align-items: center; justify-content: center; color: #fff; flex-shrink: 0; }
.sidebar__text { display: flex; flex-direction: column; flex-grow: 1; min-width: 0; }
.sidebar__title { font-size: 15px; font-weight: 700; color: var(--title); margin-bottom: 2px; }
.sidebar__subtitle { font-size: 12px; color: var(--subtitle); }
.sidebar__user { padding: 12px 20px; display: flex; align-items: center; gap: 12px; margin-bottom: 8px; flex-shrink: 0; }
.user-avatar { width: 40px; height: 40px; border-radius: 50%; background: var(--blue); color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 16px; flex-shrink: 0; }
.user-info { flex: 1; min-width: 0; }
.user-name { font-size: 14px; font-weight: 600; color: var(--title); }
.user-email { font-size: 12px; color: var(--subtitle); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.sidebar__nav { flex: 1; padding: 8px 12px; overflow-y: auto; padding-bottom: 10px; }
.nav-item { display: flex; align-items: center; gap: 12px; padding: 10px 12px; border-radius: 8px; color: var(--subtitle); text-decoration: none; transition: all 0.2s; margin-bottom: 4px; cursor: pointer; border: none; background: transparent; width: 100%; font-size: 14px; font-family: inherit; box-sizing: border-box; }
.nav-item:hover { background: var(--muted); color: var(--title); }
/* Classe active para Vue Router */
.nav-item.router-link-exact-active { background: var(--blue); color: #fff; }
.nav-item.router-link-exact-active:hover { background: var(--blue); color: #fff; }
.sidebar__footer { padding: 12px 12px 0; border-top: 1px solid var(--border); margin-top: auto; flex-shrink: 0; }
.sidebar__footer .nav-item { margin-bottom: 0; }
/* Topbar */
.topbar{ grid-area: topbar; background:var(--card); border-bottom:1px solid var(--border); box-shadow:var(--shadow); display: flex; justify-content: space-between; align-items:center; padding:12px 20px; position: sticky; top: 0; z-index: 10; box-sizing: border-box; width: 100%; }
.brand{ display:flex; align-items:center; gap:12px; min-width: 0; }
.brand__logo{ width:36px; height:36px; border-radius:10px; display:grid; place-items:center; background:#eaf5f5; border:1px solid var(--border); flex-shrink: 0; }
.brand__text { min-width: 0; }
.brand__title{ color:var(--title); font-weight:600; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.brand__subtitle{ font-size:13px; color:var(--subtitle); margin-top:2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.actions{ display:flex; gap:10px; flex-shrink: 0; }
.btn-outline{ display:inline-flex; align-items:center; height:36px; padding:0 16px; border-radius:10px; border:1px solid var(--border); background:var(--card); color:var(--title); cursor:pointer; white-space: nowrap; }
.btn-outline:hover { background-color: var(--muted); }
/* Área de Conteúdo */
.content-area { grid-area: content; overflow-y: auto; padding: 20px; background-color: var(--bg); box-sizing: border-box; }
/* Responsivo */
@media (max-width:768px){ .page{ grid-template-columns: 1fr; grid-template-rows: auto 1fr; grid-template-areas: "topbar" "content"; height: 100vh; overflow: hidden; } .sidebar{ display: none; } .content-area { grid-area: content; padding: 15px; overflow-y: auto; } .topbar { position: static; } }
</style>
