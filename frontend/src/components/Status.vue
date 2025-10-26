<template>
  <div> <header class="topbar-local">
    <div class="topbar-local__inner">
      <button @click="handleVoltar" class="btn icon" aria-label="Voltar">
        <svg viewBox="0 0 24 24" class="icon__svg"><path d="M15 18 9 12l6-6"/></svg>
      </button>
      <div class="title">
        <div class="title__icon">
          <svg viewBox="0 0 24 24" class="title__icon-svg">
            <rect x="4" y="3" width="16" height="18" rx="2" fill="none" stroke="currentColor"/>
            <path d="M9 3v2h6V3" fill="none" stroke="currentColor"/>
            <path d="M9 9h6" fill="none" stroke="currentColor"/>
            <path d="M9 13h6" fill="none" stroke="currentColor"/>
            <path d="M9 17h6" fill="none" stroke="currentColor"/>
          </svg>
        </div>
        <div>
          <h1 class="title__main">Status das Avaliações (Líder)</h1>
          <p class="title__sub">Visualize o progresso das avaliações criadas</p>
        </div>
      </div>
    </div>
  </header>
    <main class="container">
      <section class="summary">
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Pendentes</div>
            <div class="kpi__icon icon--blue"><svg viewBox="0 0 24 24" class="kpi__svg"><circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/></svg></div>
          </div>
          <div class="kpi__value">{{ totais.pendentes }}</div>
        </div>
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Concluídas</div>
            <div class="kpi__icon icon--green"><svg viewBox="0 0 24 24" class="kpi__svg"><circle cx="12" cy="12" r="9"/><path d="m8.5 12 2.5 2.5 4.5-5"/></svg></div>
          </div>
          <div class="kpi__value">{{ totais.concluidas }}</div>
        </div>
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Atrasadas</div>
            <div class="kpi__icon icon--red"><svg viewBox="0 0 24 24" class="kpi__svg"><circle cx="12" cy="12" r="9"/><path d="M12 8v5"/><path d="M12 16h.01"/></svg></div>
          </div>
          <div class="kpi__value">{{ totais.atrasadas }}</div>
        </div>
      </section>

      <div class="avaliacoes-container">
        <h2 class="list__title">Todas as Avaliações</h2>
        <article v-for="a in avaliacoes" :key="a.id" class="card item">
          <header class="item__header">
            <div class="item__title">
              {{ a.titulo }}
              <span :class="['badge', a.status==='pendente' ? 'badge--blue' : a.status==='atrasada' ? 'badge--red' : a.status==='concluida' ? 'badge--green' : 'badge--muted']">
                {{ labelStatus(a.status) }}
              </span>
            </div>
            <button class="btn btn--primary btn--start" @click="verificar(a)">
              Verificar
            </button>
          </header>
          <div class="item__meta"> Prazo: {{ formatarData(a.prazo) }} <span class="dot">•</span> Perguntas: {{ a.perguntas }} </div>
          <div class="item__splitter"></div>
          <div class="item__stats">
            <div class="stat">
              <div class="stat__icon"><svg viewBox="0 0 24 24" class="stat__svg"><path d="M5 7h14M5 12h14M5 17h10"/></svg></div>
              <div class="stat__text"><div class="stat__title">Perguntas</div><div class="stat__value">{{ a.perguntas }}</div></div>
            </div>
            <div class="stat">
              <div class="stat__icon"><svg viewBox="0 0 24 24" class="stat__svg"><circle cx="12" cy="7.5" r="3.5"/><path d="M4 20a8 5 0 0 1 16 0"/></svg></div>
              <div class="stat__text"><div class="stat__title">Destinatários</div><div class="stat__value">{{ a.destinatarios }}</div></div>
            </div>
            <div class="stat">
              <div class="stat__icon"><svg viewBox="0 0 24 24" class="stat__svg"><circle cx="12" cy="12" r="9"/><path d="m8.5 12 2.5 2.5 4.5-5"/></svg></div>
              <div class="stat__text"><div class="stat__title">Respostas</div><div class="stat__value">{{ a.respostasRespondidas }}/{{ a.respostasTotal }}</div></div>
            </div>
          </div>
        </article>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const avaliacoes = ref([])

const totais = computed(() => ({
  pendentes:  avaliacoes.value.filter(a => a.status === 'pendente').length,
  concluidas: avaliacoes.value.filter(a => a.status === 'concluida').length,
  atrasadas:  avaliacoes.value.filter(a => a.status === 'atrasada').length,
}))

function labelStatus(s){
  if (s === 'pendente')  return 'Pendente'
  if (s === 'atrasada')  return 'Atrasada'
  if (s === 'concluida') return 'Concluída'
  return 'Status'
}
function formatarData(iso){
  if (!iso) return '-'
  const d = new Date(iso)
  return d.toLocaleDateString('pt-BR')
}

// Função Voltar conectada ao botão no novo header
function handleVoltar(){
  router.go(-1); // Usar router.go(-1) é geralmente preferível a window.history
}

function verificar(a){
  router.push({
    name: 'LiderVerRespostas',
    params: { avaliacaoId: a.id }
  })
}

onMounted(() => {
  // Dados mocados originais
  avaliacoes.value = [
    { id:'a1', titulo:'Avaliação de Competências - Q4 2024', status:'pendente', prazo:'2024-12-19', perguntas:8, destinatarios:1, respostasRespondidas:0, respostasTotal:0 },
    { id:'a2', titulo:'Feedback Trimestral - Equipe de Tecnologia', status:'pendente', prazo:'2024-12-17', perguntas:5, destinatarios:1, respostasRespondidas:0, respostasTotal:0 },
    { id:'a3', titulo:'Autoavaliação de Liderança', status:'atrasada', prazo:'2024-12-01', perguntas:6, destinatarios:1, respostasRespondidas:0, respostasTotal:0 },
    { id:'a4', titulo:'Pesquisa de Engajamento - 2024', status:'concluida', prazo:'2024-11-05', perguntas:10, destinatarios:1, respostasRespondidas:1, respostasTotal:1 },
  ]
})
</script>

<style scoped>
/* REMOVIDO .page */
/* Adicionadas variáveis fallback */
:root {
  --bg: #f6f9ff; --card: #ffffff; --border: #e6effa; --shadow: 0 1px 2px rgba(20,40,80,.04);
  --title: #274b57; --subtitle: #6b7a90; --blue: #3a6ce1;
  --text-primary: #3a4b63; --text-secondary: #1f2d3d;
  --blue-light: #eaf2ff; --green-light: #e7fbef; --green: #11a966;
  --red-light: #ffe8ec; --red: #d33b53; --muted: #eef2f6;
  --pill-bg: #f3f8ff; --pill-border: #cfe0fb;
}

/* ============================================== */
/* ADIÇÃO: Estilos para o Cabeçalho Local */
/* ============================================== */
.topbar-local {
  background: var(--card, #ffffff);
  border-bottom: 1px solid var(--border, #e6effa);
}
.topbar-local__inner{
  display:flex;
  align-items:center;
  gap:12px;
  padding:12px 16px;
  max-width: 1100px; /* Mesma largura do container */
  margin: 0 auto; /* Centraliza */
}
.title{display:flex; align-items:center; gap:10px;}
.title__icon{
  background: var(--blue-light, #eaf2ff);
  color: var(--blue, #3a6ce1);
  width:36px;height:36px;border-radius:10px;display:grid;place-items:center;
}
.title__icon-svg{width:20px;height:20px; stroke-width: 1.5;} /* Ajuste stroke-width se necessário */
.title__main{font-size:20px;font-weight:600;margin:0; color: var(--title, #274b57);}
.title__sub{font-size:13px;color: var(--subtitle, #6b7a90);margin:2px 0 0;}
.btn{height:36px;padding:0 12px;border:1px solid var(--border, #e6effa);background:var(--card, #fff);border-radius:10px;cursor:pointer;color: var(--text-secondary, #1f2d3d);display:flex; align-items:center; gap: 8px;}
.icon{width:36px;height:36px;display:grid;place-items:center;border-radius:10px;}
.icon__svg{width:20px;height:20px;stroke:var(--text-primary, #3a4b63);fill:none;stroke-width:2;}
.btn:hover { background-color: var(--muted, #f3f4f6); }
/* ============================================== */

/* Ajuste na margem do container principal */
.container{
  max-width:1100px;
  margin: 20px auto 0 auto; /* Adicionada margem superior */
  padding:16px;
}

/* Estilos restantes mantidos */
.btn--primary{background:var(--blue, #3a6ce1);color:#fff;border-color:var(--blue, #3a6ce1);}
.btn--primary:hover{background:#315fc6;}
.btn--start{min-width:92px; justify-content: center;}
.summary{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:16px;} /* Removido margin-top */
.kpi{background:#fff;border:1px solid var(--border, #e6effa);border-radius:16px;padding:16px;box-shadow:var(--shadow);}
.kpi__row{display:flex;align-items:center;justify-content:space-between;}
.kpi__title{color:var(--text-primary, #3a4b63);font-weight:600;}
.kpi__icon{width:36px;height:36px;border-radius:10px;display:grid;place-items:center;}
.kpi__svg{width:20px;height:20px;stroke:currentColor;fill:none;stroke-width:2;}
.icon--blue{background:var(--blue-light, #eaf2ff);color:var(--blue, #3a6ce1);}
.icon--green{background:var(--green-light, #e7fbef);color:var(--green, #11a966);}
.icon--red{background:var(--red-light, #ffe8ec);color:var(--red, #d33b53);}
.kpi__value{font-size:26px;font-weight:700;margin-top:8px; color: var(--title);} /* Adicionado color */
.avaliacoes-container{background:#ffffff;border:1px solid var(--border, #e3edfb);border-radius:20px;padding:20px;margin-top:24px;box-shadow:0 2px 8px rgba(18,39,76,.05);}
.list__title{font-size:16px;font-weight:600;margin:0 0 14px;color:var(--text-primary, #3a4b63);}
.card.item{background:var(--pill-bg, #f3f8ff);border:1px solid var(--pill-border, #cfe0fb);border-radius:14px;padding:14px 16px;margin-bottom:14px;box-shadow:0 1px 0 rgba(38,86,170,.05);}
.item__header{display:flex;justify-content:space-between;align-items:center;gap:12px;}
.item__title{font-weight:600;color:var(--title, #27436b);display:flex;align-items:center;gap:10px;}
.badge{display:inline-flex;align-items:center;gap:6px;font-size:12px;padding:4px 10px;border-radius:999px;border:1px solid transparent;font-weight:600;}
.badge--blue{background:var(--blue-light, #eaf2ff);color:var(--blue, #3a6ce1);}
.badge--green{background:var(--green-light, #e7fbef);color:var(--green, #11a966);}
.badge--muted{background:var(--muted, #eef2f6);color:var(--subtitle, #64758b);}
.badge--red{background:var(--red-light, #feecef);color:var(--red, #d33b53);}
.item__meta{margin-top:4px;font-size:13px;color:var(--subtitle, #6b7a90);}
.item__meta .dot{margin:0 8px;opacity:.5;}
.item__splitter{height:1px;background:var(--pill-border, #e1ebfb);margin:12px 0;}
.item__stats{display:grid;grid-template-columns:repeat(3,1fr);gap:18px;}
.stat{display:flex;align-items:center;gap:10px;}
.stat__icon{width:28px;height:28px;border-radius:8px;display:grid;place-items:center;background:var(--blue-light, #eef4ff);color:#5477d8;}
.stat__svg{width:18px;height:18px;stroke:currentColor;fill:none;stroke-width:2;}
.stat__title{font-size:12px;color:var(--subtitle, #6b7a90);}
.stat__value{font-weight:700;color:var(--text-secondary, #1f2d3d);}
@media (max-width:900px){.item__stats{grid-template-columns:1fr;}}
</style>
