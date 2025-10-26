<template>
  <div> <section class="hero">
    <h1>Bem-vindo ao Sistema de Liderança</h1>
    <p>Acesse as ferramentas de gestão e acompanhamento de equipes</p>
  </section>

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
        <button class="btn-primary" @click="irParaColaboradores">Acessar</button>
      </article>

      <article class="card">
        <div class="card__icon">
          <div class="icon-circle">
            <img src="@/assets/pencil-svgrepo-com.svg" alt="ícone de lápis" width="28" height="28" />
          </div>
        </div>
        <h3 class="card__title">Status das Avaliações</h3>
        <p class="card__desc">Monitore o progresso e status das avaliações dos colaboradores</p>
        <button class="btn-primary" @click="irParaStatus">Acessar</button>
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
        <button class="btn-primary" @click="irParaDashboard">Acessar</button>
      </article>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

const irParaColaboradores = () => {
  router.push({ name: 'LiderFuncionarios' }); // Rota que carrega Lista.vue
};
const irParaStatus = () => {
  router.push({ name: 'LiderAvaliacoes' }); // Rota do Hub de Avaliações
};
const irParaDashboard = () => {
  router.push({ name: 'LiderDashboard' });
};
</script>

<style scoped>
/* ESTILOS ESPECÍFICOS DESTA PÁGINA */
/* (Sem :root, pois as variáveis vêm do LiderLayout) */

/* Hero */
.hero{
  text-align:center;
  padding:24px 16px 16px;
}
.hero h1{
  margin:0 0 6px;
  font-size:28px;
  color:var(--title); /* Herdado */
}
.hero p{
  margin:0 0 20px;
  font-size:14px;
  color: var(--subtitle); /* Herdado */
}

/* Cards */
.cards{
  max-width:1100px;
  margin:0 auto 20px;
  padding:0 16px;
  display:grid;
  grid-template-columns: repeat(3, 1fr);
  gap:20px;
}
.card {
  background: var(--card); /* Herdado */
  border: 1px solid var(--border); /* Herdado */
  border-radius: 14px;
  box-shadow: var(--shadow); /* Herdado */
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
  background: var(--blue); /* Herdado */
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}
.icon-circle svg, .icon-circle img {
  display: block; /* Garante alinhamento */
}

.card__title{
  margin:6px 0 8px;
  color:var(--title); /* Herdado */
  font-size:18px;
  font-weight:700;
}
.card__desc{
  font-size:14px;
  color:var(--subtitle); /* Herdado */
  max-width:260px;
  line-height:1.5;
  margin:0 auto 40px auto;
  flex-grow:1;
  display:flex;
  align-items:center;
  justify-content:center;
  text-align:center;
}

.btn-primary {
  width: 90%;
  height: 42px;
  border: 2px solid var(--blue); /* Herdado */
  border-radius: 10px;
  background: transparent;
  color: var(--blue); /* Herdado */
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: auto; /* Empurra para baixo */
}
.btn-primary:hover {
  background: var(--blue); /* Herdado */
  color: #fff;
}

/* Responsivo */
@media (max-width:1024px){
  .cards{ grid-template-columns: repeat(2, 1fr); }
}
@media (max-width:640px){
  .cards{ grid-template-columns: 1fr; }
}
</style>
