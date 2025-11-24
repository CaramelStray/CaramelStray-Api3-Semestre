<template>
  <div class="supervisor-container">
    <section class="hero">
      <h1>Bem-vindo ao Sistema de Liderança</h1>
      <p>Acesse as ferramentas de gestão e acompanhamento de equipes</p>
    </section>

    <main class="cards">
      <article class="card">
        <div class="card__icon">
          <div class="icon-circle">
            <svg viewBox="0 0 24 24" width="28" height="28">
              <rect x="3" y="3" width="18" height="18" rx="2" fill="none" stroke="#fff" stroke-width="2"/>
              <line x1="8" y1="12" x2="16" y2="12" stroke="#fff" stroke-width="2"/>
              <line x1="8" y1="8" x2="16" y2="8" stroke="#fff" stroke-width="2"/>
              <line x1="8" y1="16" x2="16" y2="16" stroke="#fff" stroke-width="2"/>
            </svg>
          </div>
        </div>
        <h3 class="card__title">Minha Equipe</h3>
        <p class="card__desc">Consulte e gerencie os dados dos colaboradores</p>
        <button class="btn-primary" @click="irParaColaboradores">Acessar</button>
      </article>

      <article class="card">
        <div class="card__icon">
          <div class="icon-circle">
            <svg viewBox="0 0 24 24" width="28" height="28">
              <path d="M18 20V10M12 20V4M6 20v-6" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
        </div>
        <h3 class="card__title">Status das Avaliações</h3>
        <p class="card__desc">Monitore o progresso das avaliações</p>
        <button class="btn-primary" @click="irParaStatus">Acessar</button>
      </article>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

// Navegação segura para as rotas do Supervisor
const irParaColaboradores = () => {
  router.push({ name: 'SupervisorFuncionarios' });
};

const irParaStatus = () => {
  router.push({ name: 'SupervisorAvaliacoes' });
};
</script>

<style scoped>
/* Container principal para garantir espaçamento */
.supervisor-container {
  width: 100%;
  padding-bottom: 40px;
}

/* Hero Section */
.hero{
  text-align:center;
  padding:24px 16px 16px;
}
.hero h1{
  margin:0 0 6px;
  font-size:28px;
  color:var(--title); /* Agora funciona graças ao Layout novo! */
}
.hero p{
  margin:0 0 20px;
  font-size:14px;
  color: var(--subtitle);
}

/* Grid de Cards */
.cards{
  max-width:1100px;
  margin:0 auto 20px;
  padding:0 16px;
  display:grid;
  grid-template-columns: repeat(2, 1fr); /* 2 colunas para 2 cards */
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
  transition: transform 0.2s ease;
}

.card:hover {
  transform: translateY(-5px); /* Efeito suave ao passar o mouse */
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
  box-shadow: 0 4px 10px rgba(37, 99, 235, 0.2);
}
.icon-circle svg {
  display: block;
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
}

/* Botões */
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
@media (max-width: 768px){
  .cards{ grid-template-columns: 1fr; }
}
</style>
