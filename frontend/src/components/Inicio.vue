<template>
  <div class="page">
    <!-- Topbar branco -->
    <header class="topbar">
      <div class="brand">
        <div class="brand__logo">
          <!-- ícone placeholder -->
          <svg viewBox="0 0 24 24" width="18" height="18">
            <circle cx="12" cy="8" r="3" fill="currentColor"/>
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
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
    </section>

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
            <svg viewBox="0 0 24 24" width="28" height="28">
              <rect x="7" y="3" width="10" height="14" rx="2" ry="2" fill="#fff" />
              <path d="M7 7h10" stroke="#fff" stroke-width="2"/>
              <path d="M9 12l2 2 4-4" fill="none" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
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

    <!-- Box inferior -->
    <section class="help-box">
      <div class="help-card">
        <div class="help-title">Precisa de Ajuda?</div>
      </div>
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

  min-height:100vh;
  background:var(--bg);
  color:var(--subtitle);
  font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, Arial;
}

/* Topbar */
.topbar{
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
  text-align:center;
  padding:26px 16px 10px;
}
.hero h1{
  margin:8px 0 6px; font-size:28px; color:var(--title);
}
.hero p{ margin:0; font-size:14px; }

/* Cards */
.cards{
  max-width:1100px;
  margin:18px auto 20px;
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
  background: var(--primary-600); /* azul forte */
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
  border: 2px solid var(--primary-600);
  border-radius: 10px;
  background: transparent;
  color: var(--primary-600);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}
.btn-primary:hover {
  background: var(--primary-600);
  color: #fff;
}

/* Help box */
.help-box{
  max-width:1100px;
  margin:6px auto 40px;
  padding:0 16px;
}
.help-card{
  background:var(--card);
  border:1px solid var(--border);
  border-radius:12px;
  box-shadow:var(--shadow);
  height:70px;
  display:grid;
  place-items:center;
  color:#6d7e87;
}
.help-title{ font-weight:600; }

/* Responsivo */
@media (max-width:1024px){
  .cards{ grid-template-columns: repeat(2, 1fr); }
}
@media (max-width:640px){
  .cards{ grid-template-columns: 1fr; }
}
</style>
