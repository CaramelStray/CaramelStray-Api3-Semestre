<template>
  <div class="page">

    <!-- Menu lateral -->
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
        <a href="#" class="nav-item active">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" fill="none" stroke="currentColor" stroke-width="2"/>
            <path d="M9 22V12h6v10" fill="none" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>Início</span>
        </a>
        <a href="#" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <circle cx="12" cy="8" r="3" fill="none" stroke="currentColor" stroke-width="2"/>
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>Colaboradores</span>
        </a>
        <a href="#" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" fill="none" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>Avaliações</span>
        </a>
        <a href="#" class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M4 18V6m5 12V9m5 9V12m5 6V8" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>Dashboard</span>
        </a>
      </nav>

      <div class="sidebar__footer">
        <button class="nav-item">
          <svg viewBox="0 0 24 24" width="20" height="20">
            <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4m7 14l5-5-5-5m5 5H9" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>Sair</span>
        </button>
      </div>
    </aside>

    <!-- Topbar branco -->
    <header class="topbar">
      <div class="brand">
        <div class="brand__logo">
          <svg viewBox="0 0 24 24" width="18" height="18">
            <circle cx="12" cy="8" r="3" fill="#382eceff"/>
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="#382eceff" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="brand__text">
          <div class="brand__title">Sistema Corporativo - Líder</div>
          <div class="brand__subtitle">Painel de Controle da Liderança</div>
        </div>
      </div>

      <div class="actions">
        <!-- Ícone de Configurações removido -->
        <button class="btn-outline">Sair</button>
      </div>
    </header>

    <!-- Title center -->
    <section class="hero">
      <h1>Bem-vindo ao Sistema de Liderança</h1>
      <p>Acesse as ferramentas de gestão e acompanhamento de equipes</p>

    <!-- Cards -->
    <main class="cards">
      <article class="card">
        <div class="card__icon">
          <div class="icon-circle">
            <svg viewBox="0 0 24 24" width="28" height="28">
              <circle cx="12" cy="8" r="3"/>
              <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="#fff" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
        </div>
        <h3 class="card__title">Visualizar Colaboradores</h3>
        <p class="card__desc">Consulte e gerencie os dados dos colaboradores da empresa</p>
        <button class="btn-primary">Acessar</button>
      </article>

      <article class="card">
        <div class="card__icon">
            <div class="icon-circle">
              <img src="@/assets/pencil-svgrepo-com.svg" alt="ícone de lápis" width="28" height="28" />
            </div>
        </div>
        <h3 class="card__title">Status das Avaliações</h3>
        <p class="card__desc">Monitore o progresso e status das avaliações dos colaboradores</p>
        <button class="btn-primary">Acessar</button>
      </article>

      <article class="card">
        <div class="card__icon">
          <div class="icon-circle">
            <svg viewBox="0 0 24 24" width="28" height="28">
              <path d="M4 18V6m5 12V9m5 9V12m5 6V8" fill="none" stroke="#fff" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
        </div>
        <h3 class="card__title">Dashboard</h3>
        <p class="card__desc">Visualize relatórios e métricas importantes da empresa</p>
        <button class="btn-primary">Acessar</button>
      </article>
    </main>
    </section>

  </div>
</template>

<script>
export default {
  name: "HomeLider"
};
</script>

<style scoped>
.page{
  --bg:#f3f9f9;
  --card:#ffffff;
  --border:#e3eeee;
  --shadow:0 2px 12px rgba(0,0,0,.06);
  --title:#274b57;
  --subtitle:#6e7f89;
  --primary:#5cc2c1;
  --primary-600:#3fb2b2;
  --muted:#eaf5f5;
  --blue:#2563eb;

  min-height:100vh;
  background:var(--bg);
  color:var(--subtitle);
  font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, Arial;
  display: grid;
  grid-template-columns: 240px 1fr;
  grid-template-rows: auto 1fr;
  grid-template-areas: 
    "sidebar topbar"
    "sidebar content";
}

/* Sidebar */

.sidebar {
  grid-area: sidebar;
  background: var(--card);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  height: 100vh;
  position: sticky;
  top: 0;
  padding: 20px 0;
}

.sidebar__header {
  padding: 0 20px 20px;
  border-bottom: 1px solid var(--border);
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.sidebar__logo {
  width: 40px;
  height: 40px;
  background: var(--blue);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 0;
}

.sidebar__title {
  font-size: 15px;
  font-weight: 700;
  color: var(--title);
  margin-bottom: 2px;
}

.sidebar__text {
  display: flex;
  flex-direction: column;
}

.sidebar__subtitle {
  font-size: 12px;
  color: var(--subtitle);
}

.sidebar__user {
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--blue);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 16px;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--title);
}

.user-email {
  font-size: 12px;
  color: var(--subtitle);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sidebar__nav {
  flex: 1;
  padding: 8px 12px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: 8px;
  color: var(--subtitle);
  text-decoration: none;
  transition: all 0.2s;
  margin-bottom: 4px;
  cursor: pointer;
  border: none;
  background: transparent;
  width: 100%;
  font-size: 14px;
  font-family: inherit;
}

.nav-item:hover {
  background: var(--muted);
  color: var(--title);
}

.nav-item.active {
  background: var(--blue);
  color: #fff;
}

.sidebar__footer {
  padding: 8px 12px;
  border-top: 1px solid var(--border);
  padding-top: 12px;
}

/* Topbar */
.topbar{
  grid-area: topbar;
  background:var(--card);
  border-bottom:1px solid var(--border);
  box-shadow:var(--shadow);
  display:grid;
  grid-template-columns: 1fr auto;
  align-items:center;
  padding:12px 20px;
}
.brand{
  display:flex; align-items:center; gap:12px;
}
.brand__logo{
  width:36px; height:36px; border-radius:10px;
  display:grid; place-items:center;
  background:#eaf5f5; color:var(--primary-600);
  border:1px solid var(--border);
}
.brand__title{ color:var(--title); font-weight:600; }
.brand__subtitle{ font-size:13px; color:var(--subtitle); margin-top:2px; }

.actions{ display:flex; gap:10px; }
.btn-outline{
  display:inline-flex; align-items:center;
  height:36px; padding:0 16px; border-radius:10px;
  border:1px solid var(--border);
  background:var(--card); color:var(--title);
  cursor:pointer;
}

/* Hero */
.hero{
  grid-column: 2;
  text-align:center;
  padding:24px 16px 16px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: calc(100vh - 60px);
}
.hero h1{
  margin:0 0 6px; 
  font-size:28px; 
  color:var(--title);
}
.hero p{
  margin:0 0 20px; 
  font-size:14px; 
}

/* Cards */
.cards{
  grid-column: 2;
  max-width:1100px;
  margin:0 auto 20px;
  padding:0 16px;
  display:grid;
  grid-template-columns: repeat(3, 1fr);
  gap:20px;
}
.card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 14px;
  box-shadow: var(--shadow);
  padding: 40px 24px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  justify-content: space-between;
  min-height: 350px;
  position: relative;
}
.card__icon{ margin-bottom:14px; }

.icon-circle {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: grid;
  place-items: center;
  background: var(--blue);
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}
.icon-circle svg {
  fill: currentColor;
  stroke: currentColor;
}

.card__title{
  margin:6px 0 8px;
  color:var(--title);
  font-size:18px;
  font-weight:700;
}
.card__desc{
  font-size:14px;
  color:var(--subtitle);
  max-width:260px;
  line-height:1.5;
  margin:0 auto 40px auto;
  flex-grow:1;
  display:flex;
  align-items:center;
  justify-content:center;
  text-align:center;
}

/* Botão largo no rodapé do card */
.btn-primary {
  width: 90%;
  height: 42px;
  border: 2px solid var(--blue);
  border-radius: 10px;
  background: transparent;
  color: var(--blue);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}
.btn-primary:hover {
  background: var(--blue);
  color: #fff;
}

/* Responsivo */
@media (max-width:1024px){
  .cards{ grid-template-columns: repeat(2, 1fr); }
}
@media (max-width:768px){
  .page{
    grid-template-columns: 1fr;
    grid-template-areas: 
      "topbar"
      "content";
  }
  .sidebar{ display: none; }
  .hero, .cards{ grid-column: 1; }
}
@media (max-width:640px){
  .cards{ grid-template-columns: 1fr; }
}
</style>
