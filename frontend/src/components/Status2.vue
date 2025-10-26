<template>
  <div>
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
        <h2 class="list__title">Avaliações Atribuídas</h2>
        <article v-for="a in avaliacoes" :key="a.id" class="card item">
          <header class="item__header">
            <div class="item__title">
              {{ a.titulo }}
              <span :class="['badge', a.status==='pendente' ? 'badge--blue' : a.status==='atrasada' ? 'badge--red' : a.status==='concluida' ? 'badge--green' : 'badge--muted']">
                {{ labelStatus(a.status) }}
              </span>
            </div>
            <button class="btn btn--primary btn--start" @click="iniciar(a)">
              Iniciar
            </button>
          </header>
          <div class="item__meta"> Prazo: {{ formatarData(a.prazo) }} <span class="dot">•</span> Perguntas: {{ a.perguntas }} </div>
          <div class="item__splitter"></div>
          <div class="item__stats">
            <div class="stat">
              <div class="stat__icon"><svg viewBox="0 0 24 24" class="stat__svg"><path d="M5 7h14M5 12h14M5 17h10"/></svg></div>
              <div class="stat__text"><div class="stat__title">Perguntas</div><div class="stat__value">{{ a.perguntas }}</div></div>
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
  // CORRIGIDO: "Pendentes"
  pendentes:  avaliacoes.value.filter(a => a.status === 'pendente').length,
  concluidas: avaliacoes.value.filter(a => a.status === 'concluida').length,
  atrasadas:  avaliacoes.value.filter(a => a.status === 'atrasada').length,
}))

function labelStatus(s){
  if (s === 'pendente')  return 'Pendente' // CORRIGIDO
  if (s === 'atrasada')  return 'Atrasada'
  if (s === 'concluida') return 'Concluída'
  return 'Status'
}
function formatarData(iso){
  if (!iso) return '-'
  const d = new Date(iso)
  return d.toLocaleDateString('pt-BR')
}

function iniciar(a){
  // Colaborador vai para a tela de RESPONDER
  router.push({
    name: 'ColaboradorResponderAvaliacao',
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
/* Estilos mantidos para o design interno (KPIs e Cards) */
.container{
  max-width:1100px;
  margin: 20px auto 0 auto;
  padding:16px;
}
.btn{height:36px;padding:0 12px;border:1px solid #e6effa;background:#fff;border-radius:10px;cursor:pointer;color:#1f2d3d;display:flex;align-items:center;gap:8px;}
.btn--primary{background:#3a6ce1;color:#fff;border-color:#3a6ce1;}
.btn--primary:hover{background:#315fc6;}
.btn--start{min-width:110px; justify-content: center;}
.summary{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:16px;margin-top:20px;}
.kpi{background:#fff;border:1px solid #e6effa;border-radius:16px;padding:16px;box-shadow:0 1px 2px rgba(20,40,80,.04);}
.kpi__row{display:flex;align-items:center;justify-content:space-between;}
.kpi__title{color:#3a4b63;font-weight:600;}
.kpi__icon{width:36px;height:36px;border-radius:10px;display:grid;place-items:center;}
.kpi__svg{width:20px;height:20px;stroke:currentColor;fill:none;stroke-width:2;}
.icon--blue{background:#eaf2ff;color:#3a6ce1;}
.icon--green{background:#e7fbef;color:#11a966;}
.icon--red{background:#ffe8ec;color:#d33b53;}
.kpi__value{font-size:26px;font-weight:700;margin-top:8px; color: #142032;}
.avaliacoes-container{background:#ffffff;border:1px solid #e3edfb;border-radius:20px;padding:20px;margin-top:24px;box-shadow:0 2px 8px rgba(18,39,76,.05);}
.list__title{font-size:16px;font-weight:600;margin:0 0 14px;color:#3a4b63;}
.card.item{background:#f3f8ff;border:1px solid #cfe0fb;border-radius:14px;padding:14px 16px;margin-bottom:14px;box-shadow:0 1px 0 rgba(38,86,170,.05);}
.item__header{display:flex;justify-content:space-between;align-items:center;gap:12px;}
.item__title{font-weight:600;color:#27436b;display:flex;align-items:center;gap:10px;}
.badge{display:inline-flex;align-items:center;gap:6px;font-size:12px;padding:4px 10px;border-radius:999px;border:1px solid transparent;font-weight:600;}
.badge--blue{background:#eaf2ff;color:#3a6ce1;}
.badge--green{background:#e7fbef;color:#11a966;}
.badge--muted{background:#eef2f6;color:#64758b;}
.badge--red{background:#feecef;color:#d33b53;}
.item__meta{margin-top:4px;font-size:13px;color:#6b7a90;}
.item__meta .dot{margin:0 8px;opacity:.5;}
.item__splitter{height:1px;background:#e1ebfb;margin:12px 0;}
.item__stats{display:grid;grid-template-columns:1fr;gap:10px;}
.stat{display:flex;align-items:center;gap:10px;}
.stat__icon{width:28px;height:28px;border-radius:8px;display:grid;place-items:center;background:#eef4ff;color:#5477d8;}
.stat__svg{width:18px;height:18px;stroke:currentColor;fill:none;stroke-width:2;}
.stat__title{font-size:12px;color:#6b7a90;}
.stat__value{font-weight:700;color:#1f2d3d;}
</style>
