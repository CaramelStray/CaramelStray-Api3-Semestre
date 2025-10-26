<template>
  <div class="page">
    <!-- Header -->
    <header class="topbar">
      <div class="topbar__inner">
        <button
          @click="handleVoltar"
          class="btn icon"
          aria-label="Voltar"
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="icon__svg">
            <path fill-rule="evenodd" d="M10.03 4.97a.75.75 0 0 1 0 1.06L6.06 10l3.97 3.97a.75.75 0 1 1-1.06 1.06l-4.5-4.5a.75.75 0 0 1 0-1.06l4.5-4.5a.75.75 0 0 1 1.06 0Z" clip-rule="evenodd" />
          </svg>
        </button>
        <div class="title">
          <h1 class="title__main">Avaliação de Competências - {{ periodoTitulo }}</h1>
          <p class="title__sub">Avaliação trimestral sobre suas competências e desempenho</p>
        </div>
      </div>
      <div class="progress">
        <div class="progress__row">
          <div class="muted">Pergunta {{ indiceAtual + 1 }} de {{ totalPerguntas }}</div>
          <div class="muted">{{ progressoFormatado }}</div>
        </div>
        <div class="progress__bar">
          <div class="progress__bar__fill" :style="{ width: porcentagem + '%' }"></div>
        </div>
      </div>
    </header>

    <!-- Card -->
    <main class="container">
      <section v-if="carregando" class="card card--loading">
        <div class="skeleton skeleton--sm"></div>
        <div class="skeleton skeleton--md"></div>
        <div class="skeleton skeleton--lg"></div>
      </section>

      <section v-else class="card">
        <div class="badges">
          <span class="badge">{{ perguntaAtual?.categoria || 'Competência' }}</span>
          <span class="badge">{{ tipoBadge(perguntaAtual) }}</span>
          <span v-if="perguntaAtual?.obrigatoria" class="badge badge--warn">Obrigatória</span>
        </div>

        <h2 class="question">{{ perguntaAtual?.enunciado }}</h2>

        <div class="content">
          <!-- TEXTO -->
          <div v-if="perguntaAtual?.tipo === 'texto'">
            <textarea
              v-model="respostasLocal[perguntaAtual.id]"
              class="textarea"
              :placeholder="perguntaAtual.placeholder || 'Digite sua resposta...'"
            />
          </div>

          <!-- MULTIPLA ESCOLHA -->
          <div v-else-if="perguntaAtual?.tipo === 'multipla'" class="options">
            <label
              v-for="op in perguntaAtual.opcoes"
              :key="op.valor"
              class="option"
              :class="{ 'option--active': isSelecionado(op.valor) }"
            >
              <input
                v-if="perguntaAtual.multiplas"
                type="checkbox"
                class="option__check"
                :checked="isSelecionado(op.valor)"
                @change="toggleOpcao(op.valor)"
              />
              <input
                v-else
                type="radio"
                name="radio"
                class="option__radio"
                :checked="isSelecionado(op.valor)"
                @change="selecionarOpcaoSimples(op.valor)"
              />
              <span class="option__label">{{ op.rotulo }}</span>
            </label>
          </div>

          <!-- ESCALA -->
          <div v-else-if="perguntaAtual?.tipo === 'escala'" class="scale">
            <div class="scale__labels">
              <span>{{ perguntaAtual.minRotulo || 'Baixo' }}</span>
              <span class="scale__value">{{ valorEscalaAtual }}</span>
              <span>{{ perguntaAtual.maxRotulo || 'Alto' }}</span>
            </div>
            <input
              type="range"
              class="range"
              :min="perguntaAtual.min || 0"
              :max="perguntaAtual.max || 10"
              :step="perguntaAtual.step || 1"
              v-model.number="respostasLocal[perguntaAtual.id]"
            />
            <div class="hint">{{ perguntaAtual?.escalaLegenda || 'Arraste para selecionar' }}</div>
          </div>
        </div>

        <p v-if="erro" class="error">{{ erro }}</p>

        <div class="actions">
          <button class="btn" :disabled="indiceAtual === 0 || bloqueado" @click="anterior">Anterior</button>
          <button
            v-if="!naUltima"
            class="btn btn--primary"
            :disabled="bloqueado"
            @click="proximo"
          >
            Próximo
          </button>
          <button
            v-else
            class="btn btn--primary btn--icon"
            :disabled="bloqueado"
            @click="enviar"
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="icon__svg">
              <path d="M2.515 3.424a.75.75 0 0 1 .84-.163l18 8a.75.75 0 0 1 0 1.378l-18 8A.75.75 0 0 1 2 20.999v-6.21a.75.75 0 0 1 .513-.712L14.5 12 2.513 9.923A.75.75 0 0 1 2 9.21V4.001a.75.75 0 0 1 .515-.577Z" />
            </svg>
            Enviar Avaliação
          </button>
        </div>
      </section>

      <section v-if="enviado" class="feedback">
        <div class="feedback__card">
          <p class="feedback__title">Avaliação enviada com sucesso!</p>
          <p class="feedback__text">Obrigado por completar o formulário.</p>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import axios from 'axios';
const props = defineProps({
  avaliacaoId: { // Nome da prop deve bater com o parâmetro da rota (:avaliacaoId)
    type: [String, Number], // Aceita string ou número
    required: true
  }
});
console.log('Teste.vue: Props object recebido:', props);
console.log('Teste.vue: Valor de props.avaliacaoId:', props.avaliacaoId, '| Tipo:', typeof props.avaliacaoId);
const periodoTitulo = ref('Carregando Título...');
const salvandoResposta = ref(false);
const carregando = ref(true)
const bloqueado = ref(false)
const enviado = ref(false)
const erro = ref('')

const perguntas = ref([])
const indiceAtual = ref(0)
const respostasLocal = reactive({})

const totalPerguntas = computed(() => perguntas.value.length || 0)
const naUltima = computed(() => indiceAtual.value === totalPerguntas.value - 1)
const perguntaAtual = computed(() => perguntas.value[indiceAtual.value] || null)

const porcentagem = computed(() => {
  if (!totalPerguntas.value) return 0
  const done = indiceAtual.value / (totalPerguntas.value - 1)
  return Math.round(Math.min(100, Math.max(0, done * 100)))
})
const progressoFormatado = computed(() => `${porcentagem.value}% concluído`)

const valorEscalaAtual = computed(() => {
  const q = perguntaAtual.value
  if (!q || q.tipo !== 'escala') return ''
  const val = respostasLocal[q.id]
  return typeof val === 'number' ? val : q.min ?? 0
})

function tipoBadge(q) {
  if (!q) return 'Pergunta'
  if (q.tipo === 'texto') return 'Texto'
  if (q.tipo === 'multipla') return q.multiplas ? 'Múltipla Escolha' : 'Escolha Única'
  if (q.tipo === 'escala') return 'Escala'
  return 'Pergunta'
}

function isSelecionado(valor) {
  const q = perguntaAtual.value
  if (!q) return false
  const ans = respostasLocal[q.id]
  if (q.multiplas) return Array.isArray(ans) && ans.includes(valor)
  return ans === valor
}

function toggleOpcao(valor) {
  const q = perguntaAtual.value
  if (!q) return
  const atual = respostasLocal[q.id]
  if (!Array.isArray(atual)) respostasLocal[q.id] = []
  const arr = respostasLocal[q.id]
  const idx = arr.indexOf(valor)
  if (idx >= 0) arr.splice(idx, 1)
  else arr.push(valor)
}

function selecionarOpcaoSimples(valor) {
  const q = perguntaAtual.value
  if (!q) return
  respostasLocal[q.id] = valor
}

function validarAtual() {
  erro.value = ''
  const q = perguntaAtual.value
  if (!q) return true
  if (!q.obrigatoria) return true
  const val = respostasLocal[q.id]
  if (q.tipo === 'texto') {
    if (!val || String(val).trim() === '') {
      erro.value = 'Esta resposta é obrigatória.'
      return false
    }
  }
  if (q.tipo === 'multipla') {
    if (q.multiplas) {
      if (!Array.isArray(val) || val.length === 0) {
        erro.value = 'Selecione pelo menos uma opção.'
        return false
      }
    } else {
      if (!val) {
        erro.value = 'Selecione uma opção.'
        return false
      }
    }
  }
  if (q.tipo === 'escala') {
    const v = Number(val)
    if (Number.isNaN(v)) {
      erro.value = 'Selecione um valor na escala.'
      return false
    }
  }
  return true
}

function proximo() {
  if (!validarAtual()) return
  if (indiceAtual.value < totalPerguntas.value - 1) {
    indiceAtual.value++
  }
}

function anterior() {
  if (indiceAtual.value > 0) {
    indiceAtual.value--
    erro.value = ''
  }
}

async function enviar() {
  if (!validarAtual()) return;
  bloqueado.value = true;
  erro.value = '';
  const idInstancia = Number(props.avaliacaoId); // <<< Corrigido aqui

  try {
    console.log("Iniciando envio em lote das respostas...");
    const savePromises = []; // Array para guardar as promessas de salvar

    // Itera sobre as respostas locais
    for (const perguntaIdStr in respostasLocal) {
       // Converte a chave (que é string) para número
       const perguntaId = parseInt(perguntaIdStr, 10);
       if (isNaN(perguntaId)) continue; // Pula se a chave não for um ID numérico

       const valorResposta = respostasLocal[perguntaIdStr];

       // Encontra a pergunta correspondente para saber o tipo
       const pergunta = perguntas.value.find(p => p.id === perguntaId);
       if (!pergunta) {
           console.warn(`Não foi possível encontrar detalhes da pergunta com ID ${perguntaId} para montar payload.`);
           continue; // Pula esta resposta se não encontrar a pergunta
       }

       // Monta o payload individual (lógica similar a salvarRespostaIndividual)
       let payload = {
         funcionarioAvaliacaoCodigo: idInstancia,
         perguntaCodigo: perguntaId,
         respostaTexto: null,
         opcaoSelecionadaCodigo: null
       };

       if (pergunta.tipo === 'texto' || pergunta.tipo === 'escala') {
         payload.respostaTexto = String(valorResposta ?? '');
       } else if (pergunta.tipo === 'multipla') {
         if (pergunta.multiplas) { // Checkbox
           if (Array.isArray(valorResposta) && valorResposta.length > 0) {
             // *** ATENÇÃO: Ainda temos o problema de como salvar múltiplas opções ***
             //     Esta linha salva apenas a ÚLTIMA. Você precisaria ajustar o backend
             //     ou fazer múltiplas chamadas aqui para cada valor no array.
             payload.opcaoSelecionadaCodigo = valorResposta[valorResposta.length - 1];
           } else {
             continue; // Não salva se array estiver vazio
           }
         } else { // Radio
           payload.opcaoSelecionadaCodigo = valorResposta;
         }
       } else {
           continue; // Pula tipo desconhecido
       }

       console.log("Adicionando promessa para salvar:", payload);
       // Adiciona a promessa da chamada POST ao array
       savePromises.push(axios.post('http://localhost:8080/api/avaliacoes/respostas', payload));
    } // Fim do loop for...in

    // Espera todas as chamadas de salvar resposta terminarem
    await Promise.all(savePromises);
    console.log("Todas as respostas foram enviadas para o backend.");

    // Se chegou aqui, todas as respostas foram salvas (ou não deram erro crítico)
    // Agora, finaliza a avaliação
    console.log(`Finalizando avaliação para instância ID: ${idInstancia}...`);
    await axios.put(`http://localhost:8080/api/avaliacoes/instancias/${idInstancia}/finalizar`);

    console.log("Avaliação finalizada com sucesso.");
    enviado.value = true;

  } catch (err) {
     console.error("Erro durante o envio em lote ou finalização:", err);
     // Adapte o tratamento de erro para lidar com falhas no Promise.all ou na finalização
     erro.value = "Ocorreu um erro ao enviar as respostas ou finalizar. Tente novamente.";
     if (axios.isAxiosError(err) && err.response) {
         // Tenta dar uma mensagem mais específica
          erro.value = `Erro ${err.response.status}: ${err.response.data?.message || 'Falha no servidor'}`;
     }
  } finally {
    bloqueado.value = false;
  }
}

/*function montarPayload() {
  return perguntas.value.map(q => ({ id: q.id, resposta: respostasLocal[q.id] ?? null }))
}*/

function handleVoltar() {
  window.history.back()
}

onMounted(async () => {
  carregando.value = true;
  erro.value = '';
  enviado.value = false;

  console.log('onMounted: props.avaliacaoId recebido:', props.avaliacaoId, '| Tipo:', typeof props.avaliacaoId);
  
  // 1. Tenta limpar e converter usando parseInt
  let id = NaN; // Inicializa como NaN
  const idStringOriginal = props.avaliacaoId?.toString(); // Garante que é string ou undefined

  if (idStringOriginal) {
      const trimmedIdString = idStringOriginal.trim();
      console.log('onMounted: props.avaliacaoId após trim():', `'${trimmedIdString}'`); // Mostra com aspas para ver espaços
      id = parseInt(trimmedIdString, 10); // Usa base 10
      console.log('onMounted: Resultado de parseInt():', id, '| Tipo:', typeof id);
  } else {
      console.error('onMounted: props.avaliacaoId é nulo ou indefinido.');
  }

  // 2. Verifica se o resultado da conversão é NaN
  if (isNaN(id)) {
      console.error('onMounted: A conversão resultou em NaN. String original:', idStringOriginal);
      erro.value = "ID da avaliação inválido na URL."; // <<< Define o erro 1
      carregando.value = false;
      return;
  }

  // 3. Se chegou aqui, 'id' é um número válido. Continua...
  console.log(`Buscando dados para a instância de avaliação ID: ${id}`);

  try {
    // === FAZ A CHAMADA AXIOS (usando a variável 'id' que agora é um número) ===
    const response = await axios.get(`http://localhost:8080/api/avaliacoes/instancias/${id}/responder`);
    const dadosAvaliacao = response.data;
    console.log("Dados recebidos:", dadosAvaliacao);
    // ... (Resto do mapeamento das perguntas como estava antes) ...
    periodoTitulo.value = dadosAvaliacao.tituloAvaliacao || 'Avaliação Sem Título';
     perguntas.value = dadosAvaliacao.perguntas.map(p => ({
        id: p.codigo,
        enunciado: p.pergunta, // <<< Propriedade usada no template
        tipo: mapTipoPergunta(p.tipoPergunta),
        obrigatoria: true,
        categoria: 'Competência',
        opcoes: p.opcoes ? p.opcoes.map(op => ({
            valor: op.codigo,
            rotulo: op.descricaoOpcao
        })) : [],
        multiplas: p.tipoPergunta === 'multipla escolha' ? false : undefined,
        min: p.tipoPergunta === 'escala/nota' ? 1 : undefined,
        max: p.tipoPergunta === 'escala/nota' ? 5 : undefined,
        step: p.tipoPergunta === 'escala/nota' ? 1 : undefined,
        minRotulo: p.tipoPergunta === 'escala/nota' ? 'Discordo Totalmente' : undefined,
        maxRotulo: p.tipoPergunta === 'escala/nota' ? 'Concordo Totalmente' : undefined,
        escalaLegenda: p.tipoPergunta === 'escala/nota' ? 'Selecione sua avaliação' : undefined,
     }));
     perguntas.value.forEach(q => {
       if (q.tipo === 'escala' && respostasLocal[q.id] == null) {
         respostasLocal[q.id] = q.min ?? 1;
       }
     });
     console.log("Perguntas mapeadas:", perguntas.value);
     console.log("Computed perguntaAtual após mapeamento:", perguntaAtual.value);
    if (perguntas.value.length > 0) {
        console.log("Primeira pergunta mapeada (perguntas.value[0]):", JSON.parse(JSON.stringify(perguntas.value[0]))); // Loga uma cópia limpa
        console.log("Primeira pergunta .enunciado:", perguntas.value[0]?.enunciado); // Verifica a propriedade específica
    } else {
        console.log("Array perguntas.value está vazio após mapeamento.");
    }
    await nextTick();
    console.log("Computed perguntaAtual APÓS nextTick:", perguntaAtual.value);
  } catch (err) {
    // ... (Tratamento de erro como estava antes) ...
     console.error("Erro ao buscar dados da avaliação:", err);
     if (axios.isAxiosError(err) && err.response?.status === 404) {
          erro.value = "Avaliação não encontrada. Verifique o link ou entre em contato com o suporte.";
     } else {
          erro.value = "Não foi possível carregar a avaliação. Tente novamente mais tarde.";
     }
  } finally {
    carregando.value = false;
  }
});

function mapTipoPergunta(tipoBackend) {
  switch (tipoBackend?.toLowerCase()) {
    case 'texto': return 'texto';
    case 'multipla escolha': return 'multipla';
    case 'escala/nota': return 'escala';
    default:
      console.warn(`Tipo de pergunta não mapeado: ${tipoBackend}. Usando 'texto'.`);
      return 'texto'; // Fallback
  }
}

watch(respostasLocal, async (novasRespostas, respostasAntigas) => {
    // Não faz nada se estiver carregando ou já salvando
    if (carregando.value || salvandoResposta.value || !perguntaAtual.value) {
        return;
    }

    const perguntaId = perguntaAtual.value.id;
    const respostaAtual = novasRespostas[perguntaId];
    const respostaAnterior = respostasAntigas ? respostasAntigas[perguntaId] : undefined;

    // Salva apenas se a resposta para a pergunta ATUAL mudou
    if (JSON.stringify(respostaAtual) !== JSON.stringify(respostaAnterior)) {
        console.log(`Resposta para pergunta ${perguntaId} mudou para:`, respostaAtual);
        await salvarRespostaIndividual(perguntaId, respostaAtual);
    }
}, { deep: true });

async function salvarRespostaIndividual(perguntaCodigo, valorResposta) {
    salvandoResposta.value = true;
    erro.value = ''; // Limpa erro anterior
    console.log(`Salvando resposta para pergunta ${perguntaCodigo}... Valor:`, valorResposta);

    const idInstancia = Number(props.avaliacaoId);
    const tipo = perguntaAtual.value?.tipo; // Pega o tipo da pergunta atual

    let payload = {
        funcionarioAvaliacaoCodigo: idInstancia,
        perguntaCodigo: perguntaCodigo,
        respostaTexto: null,
        opcaoSelecionadaCodigo: null
    };

    // Monta o payload conforme o tipo da pergunta e o valor
    if (tipo === 'texto' || tipo === 'escala') {
        payload.respostaTexto = String(valorResposta ?? ''); // Converte para string
    } else if (tipo === 'multipla') {
        if (perguntaAtual.value?.multiplas) { // Checkbox (múltiplas opções)
            // O backend espera UMA chamada por CADA opção selecionada?
            // Ou espera um array? A API espera apenas UMA opcaoSelecionadaCodigo.
            // **Simplificação:** Salvar apenas a ÚLTIMA opção clicada para múltipla seleção.
            // **Ideal:** Ajustar backend ou frontend para lidar com array de opções.
            if (Array.isArray(valorResposta) && valorResposta.length > 0) {
                 payload.opcaoSelecionadaCodigo = valorResposta[valorResposta.length - 1]; // Pega a última
            } else {
                 // Se desmarcou todas, talvez enviar uma resposta "vazia"? Ou não enviar nada?
                 // Por enquanto, não enviaremos se estiver vazio.
                 console.log("Nenhuma opção múltipla selecionada para salvar.");
                 salvandoResposta.value = false;
                 return;
            }
        } else { // Radio (escolha única)
            payload.opcaoSelecionadaCodigo = valorResposta; // O valor já é o código da opção
        }
    } else {
         console.error(`Tipo de pergunta desconhecido para salvar: ${tipo}`);
         salvandoResposta.value = false;
         return; // Não salva se o tipo for inválido
    }

    console.log("Payload para POST /api/avaliacoes/respostas:", payload);

    try {
        await axios.post('http://localhost:8080/api/avaliacoes/respostas', payload);
        console.log(`Resposta para pergunta ${perguntaCodigo} salva com sucesso.`);
        // Poderia adicionar um feedback visual rápido aqui (ex: um check verde)
    } catch (err) {
        console.error(`Erro ao salvar resposta para pergunta ${perguntaCodigo}:`, err);
        erro.value = `Falha ao salvar a resposta. Verifique sua conexão.`;
        // Ideal: Tratar erros específicos (400, 404, 500)
    } finally {
        salvandoResposta.value = false;
    }
}

watch(indiceAtual, () => {
  erro.value = ''
})
</script>

<style scoped>
/* ====== Base / Layout ====== */
.page {
  /* Variáveis de cor */
  --bg: #f8fafc; 
  --fg: #0f172a; 
  --muted: #64748b;
  --card: #ffffff;
  --border: #e2e8f0; 
  --border-strong: #cbd5e1;
  --primary: #2563eb; 
  --primary-hover: #1d4ed8;
  --warn-bg: #fffbeb;   
  --warn-bd: #fde68a;   
  --warn-fg: #92400e; 
  --ok-bg: #ecfdf5;   
  --ok-bd: #a7f3d0;    
  --ok-fg: #065f46; 
  --error: #e11d48;  

  min-height: 100vh;
  background: var(--bg);
  color: var(--fg);
}

.container { max-width: 900px; margin: 0 auto; padding: 16px; }

/* ====== Topbar ====== */
.topbar { background: #fff; border-bottom: 1px solid var(--border); }
.topbar__inner { max-width: 1120px; margin: 0 auto; padding: 12px 16px; display: flex; align-items: center; gap: 12px; }
.title__main { font-weight: 600; font-size: 18px; margin: 0; }
.title__sub { margin: 2px 0 0; color: var(--muted); font-size: 13px; }

.progress { border-top: 1px solid var(--border); background: #fff; }
.progress__row { max-width: 1120px; margin: 0 auto; padding: 10px 16px; display: flex; justify-content: space-between; font-size: 13px; color: #334155; }
.muted { color: #475569; }
.progress__bar { max-width: 1120px; margin: 0 auto 12px; padding: 0 16px; height: 6px; background: var(--border); border-radius: 999px; overflow: hidden; }
.progress__bar__fill { height: 6px; width: 0; background: var(--primary); border-radius: 999px; transition: width .25s ease; }

/* ====== Card ====== */
.card { background: var(--card); border: 1px solid var(--border); border-radius: 16px; padding: 24px; }
.card--loading .skeleton { border-radius: 12px; background: #e5e7eb; animation: pulse 1.4s ease-in-out infinite; }
.skeleton--sm { height: 14px; width: 100px; margin-bottom: 10px; }
.skeleton--md { height: 18px; width: 70%; margin-bottom: 16px; }
.skeleton--lg { height: 44px; width: 100%; }
@keyframes pulse { 0%,100%{opacity:.5} 50%{opacity:1} }

.badges { display: flex; flex-wrap: wrap; gap: 8px; }
.badge { display: inline-flex; align-items: center; padding: 6px 10px; border: 1px solid var(--border); background: #f8fafc; color: #334155; border-radius: 999px; font-size: 12px; font-weight: 600; }
.badge--warn { background: var(--warn-bg); border-color: var(--warn-bd); color: var(--warn-fg); }

.question { margin: 14px 0 0; font-size: 22px; font-weight: 700; }
.content { margin-top: 18px; display: grid; gap: 12px; }

/* ====== Inputs ====== */
.textarea { width: 100%; min-height: 120px; resize: vertical; border-radius: 12px; border: 1px solid var(--border-strong); padding: 12px; background: #f8fafc; font-size: 14px; color: #0f172a; }
.textarea:focus { outline: none; background: #fff; box-shadow: 0 0 0 3px rgba(37,99,235,.12); border-color: #bfdbfe; }

.options { display: grid; gap: 10px; }
.option { display: flex; align-items: center; gap: 10px; border: 1px solid var(--border); background: #f8fafc; padding: 12px 14px; border-radius: 12px; cursor: pointer; transition: background .2s, border-color .2s, box-shadow .2s; }
.option:hover { background: #f1f5f9; }
.option--active { background: #fff; border-color: #bfdbfe; box-shadow: 0 0 0 3px rgba(37,99,235,.12); }
.option__label { color: #0f172a; font-size: 14px; }
.option__radio, .option__check { width: 16px; height: 16px; }

/* ====== escala ====== */
.scale { border: 1px solid var(--border); background: #f8fafc; border-radius: 12px; padding: 14px; }
.scale__labels { display: flex; justify-content: space-between; align-items: center; font-size: 12px; color: var(--muted); margin-bottom: 6px; }
.scale__value { font-weight: 700; color: #334155; }
.hint { margin-top: 6px; font-size: 12px; color: var(--muted); }

.range { -webkit-appearance: none; appearance: none; width: 100%; height: 6px; border-radius: 999px; background: var(--border); outline: none; }
.range::-webkit-slider-thumb { -webkit-appearance: none; appearance: none; width: 18px; height: 18px; border-radius: 50%; background: var(--primary); border: 2px solid #fff; box-shadow: 0 0 0 2px var(--primary); cursor: pointer; }
.range::-moz-range-thumb { width: 18px; height: 18px; border-radius: 50%; background: var(--primary); border: 2px solid #fff; box-shadow: 0 0 0 2px var(--primary); cursor: pointer; }

/* ====== Actions ====== */
.actions { margin-top: 22px; display: flex; justify-content: space-between; align-items: center; gap: 10px; }
.btn { height: 40px; padding: 0 14px; border: 1px solid var(--border); background: #fff; color: #0f172a; border-radius: 10px; cursor: pointer; }
.btn:hover { background: #f1f5f9; }
.btn:disabled { opacity: .6; cursor: not-allowed; }
.btn--primary { background: var(--primary); border-color: var(--primary); color: #fff; }
.btn--primary:hover { background: var(--primary-hover); }
.btn--icon { display: inline-flex; align-items: center; gap: 8px; }

.icon { width: 36px; height: 36px; display: grid; place-items: center; border-radius: 10px; }
.icon__svg { width: 20px; height: 20px; }

/* ====== Feedback ====== */
.feedback { margin-top: 16px; }
.feedback__card { border: 1px solid var(--ok-bd); background: var(--ok-bg); color: var(--ok-fg); padding: 16px; border-radius: 14px; }
.feedback__title { margin: 0; font-weight: 600; }
.feedback__text { margin: 6px 0 0; font-size: 14px; }

/* ====== Errors ====== */
.error { margin-top: 8px; color: var(--error); font-size: 14px; }
</style>
