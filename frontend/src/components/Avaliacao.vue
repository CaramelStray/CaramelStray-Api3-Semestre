<template>



    <main class="container">
      <h1 class="page-title">Sistema de Avaliações</h1>
      <p class="page-subtitle">Escolha como deseja criar ou gerenciar as avaliações</p>

      <section class="cards">
        <article class="card">
          <div class="card__icon card__icon--plus">
            <svg viewBox="0 0 24 24" width="28" height="28" aria-hidden="true">
              <circle cx="12" cy="12" r="9" fill="currentColor" opacity="0.12"/>
              <path d="M12 8v8M8 12h8" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <h2 class="card__title">Criar Avaliação</h2>
          <p class="card__text">Crie uma avaliação usando filtros avançados</p>

          <RouterLink :to="createHref" class="btn btn--primary">Criar Agora</RouterLink>
        </article>

        <article class="card">
          <div class="card__icon card__icon--clip">
            <svg viewBox="0 0 24 24" width="28" height="28" aria-hidden="true">
              <rect x="6" y="5" width="12" height="14" rx="2" ry="2" fill="none" stroke="currentColor" stroke-width="2"/>
              <path d="M9 5V4a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v1" fill="none" stroke="currentColor" stroke-width="2"/>
              <path d="M8 10h8M8 14h8" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <h2 class="card__title">Status das Avaliações</h2>
          <p class="card__text">Visualize o progresso e status de todas as avaliações</p>

          <RouterLink :to="statusHref" class="btn">Ver Status</RouterLink>
        </article>
      </section>
    </main>
</template>

<script setup>
// Importa o router para o botão "Voltar"
import { useRouter } from 'vue-router';
const router = useRouter();

// As props vêm do router/index.js
const props = defineProps({
  createHref: { type: [String, Object], default: null },
  statusHref: { type: [String, Object], default: null }
})

// Função para o botão Voltar
function goBack() {
  router.go(-1);
}
</script>

<style scoped>
/* Estilos originais do GitHub */
.page { min-height: 100vh; background: radial-gradient(1200px 600px at 50% -200px, #e8f1ff 0%, #f5f8ff 40%, #f8fbff 100%); color: #0f172a; display: flex; flex-direction: column; }
.topbar { display: flex; align-items: center; gap: 12px; padding: 14px 18px; border-bottom: 1px solid #e5e7eb; background: #ffffffcc; backdrop-filter: saturate(1.2) blur(3px); }
.icon-btn { height: 38px; width: 38px; display: grid; place-items: center; border: 1px solid #e5e7eb; background: white; color: #1f2937; border-radius: 10px; cursor: pointer; transition: background .15s ease, transform .05s ease; }
.icon-btn:hover { background: #f3f4f6; }
.icon-btn:active { transform: translateY(1px); }
.topbar__title { display: grid; gap: 2px; }
.app-title { display: flex; align-items: center; gap: 8px; font-weight: 700; font-size: 16px; }
.app-title__icon { height: 28px; width: 28px; display: grid; place-items: center; background: #eaf1ff; color: #1d4ed8; border-radius: 10px; }
.app-subtitle { font-size: 13px; color: #6b7280; }
.container { width: 100%; max-width: 980px; margin: 32px auto; padding: 0 18px 40px; text-align: center; }
.page-title { font-size: 30px; font-weight: 800; color: #0f172a; margin: 6px 0 4px; }
.page-subtitle { color: #64748b; margin-bottom: 26px; }
.cards { display: grid; grid-template-columns: 1fr; gap: 22px; }
@media (min-width: 860px) { .cards { grid-template-columns: 1fr 1fr; } }
.card { background: #ffffff; border: 1px solid #eef2ff; border-radius: 16px; padding: 28px 22px; box-shadow: 0 6px 16px rgba(37, 99, 235, 0.06), 0 1px 0 rgba(0,0,0,0.02); display: grid; justify-items: center; gap: 10px; }
.card__icon { height: 60px; width: 60px; display: grid; place-items: center; border-radius: 999px; background: #eef2ff; color: #1d4ed8; box-shadow: inset 0 0 0 8px #f5f7ff; }
.card__title { margin-top: 4px; font-size: 18px; font-weight: 700; color: #1f2937; }
.card__text { color: #6b7280; margin-bottom: 8px; }
.btn { display: inline-flex; align-items: center; justify-content: center; height: 44px; padding: 0 20px; border-radius: 10px; border: 1px solid #dbeafe; background: #f8fbff; color: #1d4ed8; font-weight: 700; cursor: pointer; transition: background .15s ease, transform .05s ease, box-shadow .15s; box-shadow: 0 1px 0 rgba(0,0,0,0.02); text-decoration: none; /* Adicionado para <RouterLink> */ }
.btn:hover { background: #f1f6ff; }
.btn:active { transform: translateY(1px); }
.btn--primary { background: #1d4ed8; color: white; border-color: #1d4ed8; box-shadow: 0 8px 20px rgba(29, 78, 216, 0.25), 0 1px 0 rgba(0,0,0,0.03); }
.btn--primary:hover { background: #1942bd; }
</style>
