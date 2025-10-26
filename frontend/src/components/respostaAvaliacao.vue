<template>
  <div class="page">
    <!-- Topbar -->
    <header class="topbar">
      <div class="topbar__inner">
        <button @click="handleVoltar" class="btn icon" aria-label="Voltar">
          <svg viewBox="0 0 24 24" class="icon__svg">
            <path d="M15 18 9 12l6-6"/>
          </svg>
        </button>

        <div class="topbar__title">
          <div>
            <h1 class="title__main">{{ avaliacao.titulo }}</h1>
            <p class="title__sub">{{ avaliacao.respostasRecebidas }} respostas recebidas</p>
          </div>
        </div>

        <button class="btn btn--primary">Salvar Revisão</button>
      </div>
    </header>

    <main class="container">
      <div class="respostas-lista">
        <article v-for="dest in destinatarios" :key="dest.id" class="destinatario-card">
          <header class="destinatario__header" @click="toggleExpand(dest.id)">
            <div class="destinatario__info">
              <div class="avatar" :style="{ backgroundColor: dest.cor }">
                {{ dest.iniciais }}
              </div>
              <div>
                <div class="destinatario__nome">{{ dest.nome }}</div>
                <div class="destinatario__meta">Enviado em {{ dest.dataEnvio }}</div>
              </div>
            </div>

            <div class="destinatario__actions">
              <span class="respostas-count">{{ dest.respostas }} / {{ dest.total }} corretas</span>
              <svg v-if="expandidos[dest.id]" viewBox="0 0 24 24" class="chevron">
                <path d="m18 15-6-6-6 6"/>
              </svg>
              <svg v-else viewBox="0 0 24 24" class="chevron">
                <path d="m6 9 6 6 6-6"/>
              </svg>
            </div>
          </header>

          <div v-if="expandidos[dest.id] && dest.perguntas.length > 0" class="perguntas-lista">
            <div v-for="pergunta in dest.perguntas" :key="pergunta.id" class="pergunta-item">
              <div class="pergunta__header">
                <span class="pergunta__numero">{{ pergunta.id }}.</span>
                <span class="pergunta__texto">{{ pergunta.texto }}</span>
              </div>

              <div class="pergunta__tipo">
                <span class="tipo-badge">{{ pergunta.tipo }}</span>
              </div>

              <!-- Resposta tipo Texto -->
              <div v-if="pergunta.tipo === 'Texto'" class="resposta-box">
                {{ pergunta.resposta }}
              </div>

              <!-- Resposta tipo Múltipla Escolha -->
              <div v-if="pergunta.tipo === 'Múltipla Escolha'" class="resposta-escolha">
                <div :class="['resposta-tag', pergunta.status === 'correto' ? 'tag--correto' : 'tag--incorreto']">
                  <svg v-if="pergunta.status === 'correto'" viewBox="0 0 24 24" class="tag__icon">
                    <path d="m5 12 5 5L20 7"/>
                  </svg>
                  <svg v-else viewBox="0 0 24 24" class="tag__icon">
                    <path d="m18 6-12 12M6 6l12 12"/>
                  </svg>
                  {{ pergunta.status === 'correto' ? 'Correta' : 'Incorreta' }}
                </div>
                <div class="resposta-valor">{{ pergunta.resposta }}</div>
                <div class="resposta-esperada">Resposta esperada: {{ pergunta.respostaEsperada }}</div>
              </div>

              <!-- Resposta tipo Múltipla Seleção -->
              <div v-if="pergunta.tipo === 'Múltipla Seleção'" class="resposta-escolha">
                <div :class="['resposta-tag', pergunta.status === 'correto' ? 'tag--correto' : 'tag--incorreto']">
                  <svg v-if="pergunta.status === 'correto'" viewBox="0 0 24 24" class="tag__icon">
                    <path d="m5 12 5 5L20 7"/>
                  </svg>
                  <svg v-else viewBox="0 0 24 24" class="tag__icon">
                    <path d="m18 6-12 12M6 6l12 12"/>
                  </svg>
                  {{ pergunta.status === 'correto' ? 'Correta' : 'Incorreta' }}
                </div>
                <div class="resposta-opcoes">
                  <span v-for="(op, i) in pergunta.resposta" :key="i" class="opcao-pill">{{ op }}</span>
                </div>
                <div class="resposta-esperada">Resposta esperada: {{ pergunta.respostaEsperada }}</div>
              </div>

              <!-- Resposta tipo Escala -->
              <div v-if="pergunta.tipo === 'Escala'" class="resposta-escala">
                <div class="escala-label">
                  <span>Nota: {{ pergunta.nota }}</span>
                  <span class="escala-max">/ {{ pergunta.escalaMax }}</span>
                </div>
                <div class="escala-bar">
                  <div
                    class="escala-fill"
                    :style="{ width: (pergunta.nota / pergunta.escalaMax * 100) + '%' }"
                  />
                </div>
              </div>
            </div>
          </div>

          <div v-if="expandidos[dest.id] && dest.perguntas.length === 0" class="sem-respostas">
            Ainda não há respostas deste destinatário
          </div>
        </article>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const avaliacao = reactive({
  titulo: 'Avaliação de Competências - Q4 2024',
  respostasRecebidas: 3,
  totalDestinatarios: 4
})

const expandidos = ref({ 'MS': true })

const destinatarios = ref([
  {
    id: 'MS',
    nome: 'Maria Silva',
    iniciais: 'MS',
    cor: '#3a6ce1',
    dataEnvio: '14/12/2024',
    respostas: 1,
    total: 4,
    perguntas: [
      {
        id: 1,
        texto: 'Descreva suas principais conquistas neste trimestre',
        tipo: 'Texto',
        resposta: 'Implementei o novo sistema de gestão de projetos e aumentei a eficiência da equipe em 30%.'
      },
      {
        id: 2,
        texto: 'Qual é o principal pilar da cultura organizacional?',
        tipo: 'Múltipla Escolha',
        resposta: 'Todas as anteriores',
        status: 'correto',
        respostaEsperada: 'Todas as anteriores'
      },
      {
        id: 3,
        texto: 'Selecione as competências que você desenvolveu:',
        tipo: 'Múltipla Seleção',
        resposta: ['Liderança', 'Comunicação', 'Trabalho em equipe'],
        status: 'incorreto',
        respostaEsperada: 'Liderança, Comunicação, Trabalho em equipe, Resolução de problemas'
      },
      {
        id: 4,
        texto: 'Como você avalia sua produtividade?',
        tipo: 'Escala',
        resposta: '8',
        nota: 8,
        escalaMax: 10
      }
    ]
  },
  {
    id: 'JS',
    nome: 'João Santos',
    iniciais: 'JS',
    cor: '#5477d8',
    dataEnvio: '15/12/2024',
    respostas: 0,
    total: 4,
    perguntas: []
  },
  {
    id: 'AC',
    nome: 'Ana Costa',
    iniciais: 'AC',
    cor: '#11a966',
    dataEnvio: '16/12/2024',
    respostas: 2,
    total: 4,
    perguntas: []
  }
])

function toggleExpand(id) {
  expandidos.value[id] = !expandidos.value[id]
}

function handleVoltar() {
  window.history.back()
}
</script>

<style scoped>
/* ===== Base ===== */
.page { background: #f6f9ff; color: #0f172a; min-height: 100vh; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; }
.container { max-width: 1100px; margin: 0 auto; padding: 20px 16px; }

/* ===== Topbar ===== */
.topbar { background: rgba(255, 255, 255, 0.8); backdrop-filter: saturate(1.2) blur(2px); border-bottom: 1px solid #e6effa; position: sticky; top: 0; z-index: 100; }
.topbar__inner { display: flex; align-items: center; gap: 12px; padding: 12px 16px; max-width: 1100px; margin: 0 auto; }
.topbar__title { flex: 1; }
.title__main { font-size: 18px; font-weight: 600; color: #1f2d3d; margin: 0; }
.title__sub { font-size: 13px; color: #6b7a90; margin-top: 2px; }

.btn { height: 36px; padding: 0 12px; border: 1px solid #e6effa; background: #fff; border-radius: 10px; cursor: pointer; color: #1f2d3d; font-size: 14px; }
.icon { width: 36px; height: 36px; display: grid; place-items: center; border-radius: 10px; padding: 0; }
.icon__svg { width: 20px; height: 20px; stroke: #3a4b63; fill: none; stroke-width: 2; }
.btn:hover { background: #f8fafc; }
.btn--primary { background: #3a6ce1; color: #fff; border-color: #3a6ce1; font-weight: 500; }
.btn--primary:hover { background: #315fc6; }

/* ===== Lista de Respostas ===== */
.respostas-lista { display: flex; flex-direction: column; gap: 16px; }

/* ===== Card Destinatário ===== */
.destinatario-card { background: #fff; border: 1px solid #e3edfb; border-radius: 16px; overflow: hidden; box-shadow: 0 2px 4px rgba(18, 39, 76, 0.04); }

.destinatario__header { display: flex; justify-content: space-between; align-items: center; padding: 16px; cursor: pointer; transition: background 0.2s; }
.destinatario__header:hover { background: #f9fbff; }

.destinatario__info { display: flex; align-items: center; gap: 12px; }
.avatar { width: 40px; height: 40px; border-radius: 50%; display: grid; place-items: center; color: #fff; font-weight: 600; font-size: 14px; }
.destinatario__nome { font-weight: 600; color: #1f2d3d; font-size: 15px; }
.destinatario__meta { font-size: 13px; color: #6b7a90; margin-top: 2px; }

.destinatario__actions { display: flex; align-items: center; gap: 12px; color: #6b7a90; }
.respostas-count { font-size: 13px; font-weight: 500; }
.chevron { width: 20px; height: 20px; stroke: currentColor; fill: none; stroke-width: 2; }

/* ===== Lista de Perguntas ===== */
.perguntas-lista { border-top: 1px solid #e6effa; padding: 16px; background: #fafcff; }

.pergunta-item { background: #fff; border: 1px solid #e6effa; border-radius: 12px; padding: 16px; margin-bottom: 12px; }
.pergunta-item:last-child { margin-bottom: 0; }

.pergunta__header { display: flex; gap: 8px; margin-bottom: 8px; }
.pergunta__numero { font-weight: 600; color: #3a6ce1; }
.pergunta__texto { font-weight: 500; color: #1f2d3d; flex: 1; }

.pergunta__tipo { margin-bottom: 12px; }
.tipo-badge { display: inline-block; background: #eef2f6; color: #64758b; font-size: 12px; padding: 4px 10px; border-radius: 6px; font-weight: 500; }

/* ===== Tipos de Resposta ===== */
.resposta-box { background: #f3f8ff; border: 1px solid #e1ebfb; border-radius: 10px; padding: 12px; color: #27436b; line-height: 1.5; }

.resposta-escolha { display: flex; flex-direction: column; gap: 10px; }

.resposta-tag { display: inline-flex; align-items: center; gap: 6px; padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; width: fit-content; }
.tag--correto { background: #e7fbef; color: #11a966; }
.tag--incorreto { background: #feecef; color: #d33b53; }
.tag__icon { width: 14px; height: 14px; stroke: currentColor; fill: none; stroke-width: 2.5; }

.resposta-valor { background: #f3f8ff; border: 1px solid #e1ebfb; border-radius: 10px; padding: 10px 12px; color: #27436b; }

.resposta-opcoes { display: flex; flex-wrap: wrap; gap: 8px; }
.opcao-pill { background: #eef4ff; color: #5477d8; padding: 6px 12px; border-radius: 8px; font-size: 13px; font-weight: 500; border: 1px solid #d6e4ff; }

.resposta-esperada { font-size: 12px; color: #6b7a90; font-style: italic; }

.resposta-escala { display: flex; flex-direction: column; gap: 8px; }
.escala-label { display: flex; align-items: baseline; gap: 4px; font-weight: 600; color: #1f2d3d; }
.escala-max { color: #6b7a90; font-size: 13px; }
.escala-bar { height: 8px; background: #e6effa; border-radius: 999px; overflow: hidden; }
.escala-fill { height: 100%; background: linear-gradient(90deg, #3a6ce1, #5477d8); border-radius: 999px; transition: width 0.3s; }

.sem-respostas { padding: 32px 16px; text-align: center; color: #6b7a90; font-size: 14px; }

@media (max-width: 768px) {
  .title__main { font-size: 16px; }
  .btn--primary { padding: 0 12px; font-size: 13px; }
}
</style>
