<template>
  <div class="page">
    <!-- Topbar -->
    <header class="topbar">
      <div class="topbar__inner">
        <button @click="handleVoltar" class="btn icon" aria-label="Voltar">
          <svg viewBox="0 0 24 24" class="icon__svg"><path d="M15 18 9 12l6-6"/></svg>
        </button>

        <div class="title">
          <!-- ÍCONE DE PRANCHETA -->
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
            <h1 class="title__main">Status das Avaliações</h1>
            <p class="title__sub">Visualize todas as avaliações criadas</p>
          </div>
        </div>
      </div>
    </header>

    <main class="container">
      <!-- KPIs de resumo -->
      <section class="summary">
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Pendentes</div>
            <div class="kpi__icon icon--blue">
              <svg viewBox="0 0 24 24" class="kpi__svg">
                <circle cx="12" cy="12" r="9"/>
                <path d="M12 7v5l3 2"/>
              </svg>
            </div>
          </div>
          <div class="kpi__value">{{ totais.pendentes }}</div>
        </div>

        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Concluídas</div>
            <div class="kpi__icon icon--green">
              <svg viewBox="0 0 24 24" class="kpi__svg">
                <circle cx="12" cy="12" r="9"/>
                <path d="m8.5 12 2.5 2.5 4.5-5"/>
              </svg>
            </div>
          </div>
          <div class="kpi__value">{{ totais.concluidas }}</div>
        </div>

        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Atrasadas</div>
            <div class="kpi__icon icon--red">
              <svg viewBox="0 0 24 24" class="kpi__svg">
                <circle cx="12" cy="12" r="9"/>
                <path d="M12 8v5"/>
                <path d="M12 16h.01"/>
              </svg>
            </div>
          </div>
          <div class="kpi__value">{{ totais.atrasadas }}</div>
        </div>
      </section>

      <!-- Lista de avaliações -->
      <div class="avaliacoes-container">
        <h2 class="list__title">Todas as Avaliações</h2>

        <article v-for="a in avaliacoes" :key="a.id" class="card item">
          <header class="item__header">
            <div class="item__title">
              {{ a.titulo }}
              <span
                :class="[
                  'badge',
                  a.status==='pendente' ? 'badge--blue'
                  : a.status==='atrasada' ? 'badge--red'
                  : a.status==='concluida' ? 'badge--green'
                  : 'badge--muted'
                ]"
              >
                {{ labelStatus(a.status) }}
              </span>
            </div>

            <!-- BOTÃO DE VERIFICAR COM ÍCONE DE OLHO -->
            <button class="btn btn--primary btn--start" @click="iniciar(a)">
              <svg viewBox="0 0 24 24" class="btn__icon-eye">
                <path
                  d="M2 12s4-7 10-7 10 7 10 7-4 7-10 7-10-7-10-7z"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
                <circle cx="12" cy="12" r="2" fill="currentColor" />
              </svg>
              Verificar
            </button>
          </header>

          <div class="item__meta">
            Prazo: {{ formatarData(a.prazo) }} <span class="dot">•</span> Perguntas: {{ a.perguntas }}
          </div>

          <div class="item__splitter"></div>

          <div class="item__stats">
            <div class="stat">
              <div class="stat__icon">
                <svg viewBox="0 0 24 24" class="stat__svg"><path d="M5 7h14M5 12h14M5 17h10"/></svg>
              </div>
              <div class="stat__text">
                <div class="stat__title">Perguntas</div>
                <div class="stat__value">{{ a.perguntas }}</div>
              </div>
            </div>

            <div class="stat">
              <div class="stat__icon">
                <svg viewBox="0 0 24 24" class="stat__svg"><circle cx="12" cy="7.5" r="3.5"/><path d="M4 20a8 5 0 0 1 16 0"/></svg>
              </div>
              <div class="stat__text">
                <div class="stat__title">Destinatários</div>
                <div class="stat__value">{{ a.destinatarios }}</div>
              </div>
            </div>

            <div class="stat">
              <div class="stat__icon">
                <svg viewBox="0 0 24 24" class="stat__svg"><circle cx="12" cy="12" r="9"/><path d="m8.5 12 2.5 2.5 4.5-5"/></svg>
              </div>
              <div class="stat__text">
                <div class="stat__title">Respostas</div>
                <div class="stat__value">{{ a.respostasRespondidas }}/{{ a.respostasTotal }}</div>
              </div>
            </div>
          </div>
        </article>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'

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
function handleVoltar(){ window.history.back() }
function iniciar(a){ console.log('Verificar avaliação', a.id) }

onMounted(() => {
  avaliacoes.value = [
    { id:'a1', titulo:'Avaliação de Competências - Q4 2024', status:'pendente', prazo:'2024-12-19', perguntas:8, destinatarios:1, respostasRespondidas:0, respostasTotal:0 },
    { id:'a2', titulo:'Feedback Trimestral - Equipe de Tecnologia', status:'pendente', prazo:'2024-12-17', perguntas:5, destinatarios:1, respostasRespondidas:0, respostasTotal:0 },
    { id:'a3', titulo:'Autoavaliação de Liderança', status:'atrasada', prazo:'2024-12-01', perguntas:6, destinatarios:1, respostasRespondidas:0, respostasTotal:0 },
    { id:'a4', titulo:'Pesquisa de Engajamento - 2024', status:'concluida', prazo:'2024-11-05', perguntas:10, destinatarios:1, respostasRespondidas:1, respostasTotal:1 },
  ]
})
</script>

<style scoped>
.page{background:#f6f9ff; color:#0f172a; min-height:100vh;}
.container{max-width:1100px; margin:0 auto; padding:16px;}
.topbar{background:#ffffffcc; border-bottom:1px solid #e6effa; backdrop-filter:saturate(1.2) blur(2px);} 
.topbar__inner{display:flex; align-items:center; gap:12px; padding:12px 16px;}
.title{display:flex; align-items:center; gap:10px;}
.title__icon{background:#3a6ce1;width:36px;height:36px;border-radius:10px;display:grid;place-items:center;}
.title__icon-svg{width:20px;height:20px;color:#fff;stroke-width:2;}
.title__main{font-size:20px;font-weight:600;margin:0;}
.title__sub{font-size:13px;color:#6b7a90;margin:2px 0 0;}
.btn{height:36px;padding:0 12px;border:1px solid #e6effa;background:#fff;border-radius:10px;cursor:pointer;color:#1f2d3d;display:flex;align-items:center;gap:8px;}
.icon{width:36px;height:36px;display:grid;place-items:center;border-radius:10px;}
.icon__svg{width:20px;height:20px;stroke:#3a4b63;fill:none;stroke-width:2;}
.btn--primary{background:#3a6ce1;color:#fff;border-color:#3a6ce1;}
.btn--primary:hover{background:#315fc6;}
.btn--start{min-width:110px;}
.btn__icon-eye{width:18px;height:18px;stroke:currentColor;fill:none;}
.summary{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:16px;margin-top:20px;}
.kpi{background:#fff;border:1px solid #e6effa;border-radius:16px;padding:16px;box-shadow:0 1px 2px rgba(20,40,80,.04);} 
.kpi__row{display:flex;align-items:center;justify-content:space-between;}
.kpi__title{color:#3a4b63;font-weight:600;}
.kpi__icon{width:36px;height:36px;border-radius:10px;display:grid;place-items:center;}
.kpi__svg{width:20px;height:20px;stroke:currentColor;fill:none;stroke-width:2;}
.icon--blue{background:#eaf2ff;color:#3a6ce1;}
.icon--green{background:#e7fbef;color:#11a966;}
.icon--red{background:#ffe8ec;color:#d33b53;}
.kpi__value{font-size:26px;font-weight:700;margin-top:8px;}
.avaliacoes-container{background:#ffffff;border:1px solid #e3edfb;border-radius:20px;padding:20px;margin-top:24px;box-shadow:0 2px 8px rgba(18,39,76,.05);} 
.list__title{font-size:16px;font-weight:600;margin:0 0 14px;color:#3a4b63;}
.card{background:#f3f8ff;border:1px solid #cfe0fb;border-radius:14px;padding:14px 16px;margin-bottom:14px;box-shadow:0 1px 0 rgba(38,86,170,.05);} 
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
.item__stats{display:grid;grid-template-columns:repeat(3,1fr);gap:18px;}
.stat{display:flex;align-items:center;gap:10px;}
.stat__icon{width:28px;height:28px;border-radius:8px;display:grid;place-items:center;background:#eef4ff;color:#5477d8;}
.stat__svg{width:18px;height:18px;stroke:currentColor;fill:none;stroke-width:2;}
.stat__title{font-size:12px;color:#6b7a90;}
.stat__value{font-weight:700;color:#1f2d3d;}
@media (max-width:900px){.item__stats{grid-template-columns:1fr;}}
</style>
