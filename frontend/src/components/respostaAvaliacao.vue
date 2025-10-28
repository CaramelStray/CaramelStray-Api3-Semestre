<template>
  <div class="page">
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
            <p class="title__sub">{{ avaliacao.respostasRecebidas }} de {{ avaliacao.totalDestinatarios }} respostas recebidas</p>
          </div>
        </div>
      </div>
    </header>

    <main class="container">
      
      <div v-if="loading" class="text-center py-10 text-slate-600" style="text-align: center; padding: 2.5rem 0; color: #475569;">
        Carregando respostas da avaliação...
      </div>
      
      <div v-else-if="error" class="text-center py-10 text-red-600 bg-red-50 rounded-lg p-4" style="text-align: center; padding: 2.5rem 0; color: #dc2626; background-color: #fef2f2; border-radius: 0.5rem; padding: 1rem;">
         {{ error }}
      </div>

      <template v-else>
        <div class="respostas-lista">
          
          <p v-if="destinatarios.length === 0" class="sem-respostas" style="padding: 32px 16px; text-align: center; color: #6b7a90; font-size: 14px;">
              Nenhum colaborador foi associado a esta avaliação.
          </p>
          
          <article v-else v-for="dest in destinatarios" :key="dest.id" class="destinatario-card">
            <header class="destinatario__header" @click="toggleExpand(dest.id)">
              <div class="destinatario__info">
                <div class="avatar" :style="{ backgroundColor: dest.cor }">
                  {{ dest.iniciais }}
                </div>
                <div>
                  <div class="destinatario__nome">{{ dest.nome }}</div>
                  <div class="destinatario__meta">{{ labelStatus(dest.status) }}</div>
                </div>
              </div>
              <div class="destinatario__actions">
                <span :class="['badge', statusClass(dest.status)]">{{ labelStatus(dest.status) }}</span>
                <svg v-if="expandidos[dest.id]" viewBox="0 0 24 24" class="chevron"><path d="m18 15-6-6-6 6"/></svg>
                <svg v-else viewBox="0 0 24 24" class="chevron"><path d="m6 9 6 6 6-6"/></svg>
              </div>
            </header>

            <div v-if="expandidos[dest.id]">
              
              <div v-if="dest.status === 'PENDENTE'" class="sem-respostas">
                Colaborador ainda não respondeu.
              </div>

              <div v-else-if="dest.perguntas.length === 0 && dest.status !== 'PENDENTE'" class="sem-respostas">
                Colaborador enviou, mas nenhuma resposta foi encontrada.
              </div>
              
              <div v-else>
                
                <div class="perguntas-lista">
                  <div v-for="pergunta in dest.perguntas" :key="pergunta.id" class="pergunta-item">
                    <div class="pergunta__header">
                      <span class="pergunta__numero">{{ pergunta.id }}.</span>
                      <span class="pergunta__texto">{{ pergunta.texto }}</span>
                    </div>
                    <div class="pergunta__tipo">
                      <span class="tipo-badge">{{ pergunta.tipo }}</span>
                    </div>
                    <div v-if="pergunta.tipo === 'Texto'" class="resposta-box">
                      {{ pergunta.resposta }}
                    </div>
                    <div v-if="pergunta.tipo === 'Múltipla Escolha'" class="resposta-escolha">
                      <div :class="['resposta-tag', pergunta.status === 'correto' ? 'tag--correto' : 'tag--incorreto']">
                        <svg v-if="pergunta.status === 'correto'" viewBox="0 0 24 24" class="tag__icon"><path d="m5 12 5 5L20 7"/></svg>
                        <svg v-else viewBox="0 0 24 24" class="tag__icon"><path d="m18 6-12 12M6 6l12 12"/></svg>
                        {{ pergunta.status === 'correto' ? 'Correta' : 'Incorreta' }}
                      </div>
                      <div class="resposta-valor">{{ pergunta.resposta }}</div>
                    </div>
                    <div v-if="pergunta.tipo === 'Escala'" class="resposta-escala">
                      <div class="escala-label">
                        <span>Nota: {{ pergunta.nota }}</span>
                        <span class="escala-max">/ {{ pergunta.escalaMax }}</span>
                      </div>
                      <div class="escala-bar">
                        <div class="escala-fill" :style="{ width: (pergunta.nota / pergunta.escalaMax * 100) + '%' }"/>
                      </div>
                    </div>
                  </div> </div> <div class="revisao-wrapper"> 
                  
                  <div v-if="dest.status === 'AGUARDANDO_REVISAO'" class="pergunta-item revisao-box">
                    <h4 class="revisao-titulo">Revisão do Supervisor</h4>
                    <div class="revisao-grid">
                        <div class="revisao-campo">
                            <label :for="'nota-' + dest.id">Nota Final (1-5)</label>
                            <input type="number" :id="'nota-' + dest.id" min="1" max="5" class="revisao-input" v-model.number="dest.revisaoNota" />
                        </div>
                        <div class="revisao-campo">
                            <label :for="'status-' + dest.id">Status Final</label>
                            <select :id="'status-' + dest.id" v-model="dest.revisaoStatus" class="revisao-input">
                                <option value="APROVADO">Aprovado</option>
                                <option value="REPROVADO">Reprovado</option>
                            </select>
                        </div>
                    </div>
                    <div class="revisao-campo">
                        <label :for="'comentario-supervisao-' + dest.id">Comentário (Privado - Supervisor)</label>
                        <textarea :id="'comentario-supervisao-' + dest.id" rows="3" class="revisao-textarea" placeholder="Feedback interno, visível apenas para gestão..." v-model="dest.revisaoComentarioSupervisor"></textarea>
                    </div>
                    <div class="revisao-campo">
                        <label :for="'comentario-colaborador-' + dest.id">Comentário (Visível ao Colaborador)</label>
                        <textarea :id="'comentario-colaborador-' + dest.id" rows="3" class="revisao-textarea" placeholder="Feedback e plano de ação para o colaborador..." v-model="dest.revisaoComentarioColaborador"></textarea>
                    </div>
                    
                    <button class="btn btn--primary" @click="handleSalvarRevisao(dest)" :disabled="dest.isLoading">
                        {{ dest.isLoading ? 'Salvando...' : 'Salvar e Enviar Revisão' }}
                    </button>
                  </div>

                  <div v-if="dest.status === 'APROVADO' || dest.status === 'REPROVADO'" class="pergunta-item revisao-box-salva">
                    <h4 class="revisao-titulo">Revisão Concluída</h4>
                    <p class="revisao-texto"><strong>Status:</strong> {{ labelStatus(dest.status) }}</p>
                    <p class="revisao-texto"><strong>Nota:</strong> {{ dest.revisaoNota || 'N/A' }}</p>
                    <p class="revisao-texto"><strong>Comentário Privado:</strong> {{ dest.revisaoComentarioSupervisor || 'N/A' }}</p>
                    <p class="revisao-texto"><strong>Comentário para Colaborador:</strong> {{ dest.revisaoComentarioColaborador || 'Nenhum' }}</p>
                  </div>
                </div> </div> </div> </article>
        </div>
      </template> </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { useAuth } from '../auth'; // <<< Importação necessária

// === Refs Essenciais ===
const loading = ref(true);
const error = ref(null);
// ======================

const avaliacao = reactive({
  titulo: 'Carregando...',
  respostasRecebidas: 0,
  totalDestinatarios: 0
});
const route = useRoute();
const router = useRouter();
const expandidos = ref({})

const destinatarios = ref([]);

function toggleExpand(id) {
  expandidos.value[id] = !expandidos.value[id]
}

function handleVoltar() {
  router.go(-1);
}

// ===== onMounted Completo (com try/catch/finally) =====
onMounted(async () => {
    loading.value = true;
    error.value = null;
    const avaliacaoMestreId = route.params.avaliacaoId;

    if (!avaliacaoMestreId) {
        error.value = "ID da avaliação mestre não fornecido na URL.";
        loading.value = false;
        return;
    }

    console.log(`Buscando detalhes completos da avaliação mestre ID: ${avaliacaoMestreId}`);

    try {
        // === PASSO 1: Busca os detalhes da Avaliação Mestre (para pegar título e perguntas) ===
        const detalheResponse = await axios.get(`http://localhost:8080/api/avaliacoes/${avaliacaoMestreId}`);
        const dadosAvaliacao = detalheResponse.data; // AvaliacaoDetalhadaDTO
        console.log("Dados da Avaliação Mestre:", dadosAvaliacao);

        // Atualiza o header
        avaliacao.titulo = dadosAvaliacao.titulo;
        const instancias = dadosAvaliacao.instancias || []; // Garante que é um array
        avaliacao.totalDestinatarios = instancias.length;
        avaliacao.respostasRecebidas = instancias.filter(
            inst => inst.resultadoStatus !== 'PENDENTE'
        ).length;

        // Mapeia as perguntas mestres para referência (ID -> Texto/Tipo)
        const perguntasMestreMap = new Map();
        (dadosAvaliacao.perguntas || []).forEach(p => { // Garante que é um array
            perguntasMestreMap.set(p.codigo, {
                texto: p.pergunta,
                tipo: mapTipoPergunta(p.tipoPergunta), // Usa a mesma função auxiliar
                opcoesMap: new Map(p.opcoes ? p.opcoes.map(op => [op.codigo, op.descricaoOpcao]) : [])
            });
        });
        console.log("Mapa de Perguntas Mestre:", perguntasMestreMap);

        // === PASSO 2: Processa as instâncias (destinatários) ===
        if (instancias.length === 0) {
             console.log("Nenhuma instância de funcionário encontrada para esta avaliação.");
             destinatarios.value = [];
        } else {
            // Array para guardar as promessas de busca de respostas
            const promessasDestinatarios = instancias.map(async (instancia) => {
                // instancia é AvaliacaoFuncionarioResponseDTO
                let perguntasComRespostas = [];
                let respostasCorretas = 0; // Lógica de "corretas" não implementada

                // Só busca respostas se o funcionário já enviou (Não está PENDENTE)
                if (instancia.resultadoStatus !== 'PENDENTE') {
                    try {
                        const respostasResponse = await axios.get(`http://localhost:8080/api/avaliacoes/instancias/${instancia.codigo}/respostas`);
                        const respostas = respostasResponse.data; // Lista de RespostaColaboradorResponseDTO
                        console.log(`Respostas recebidas para Instância ${instancia.codigo}:`, respostas);

                        perguntasComRespostas = respostas.map((r, index) => {
                            const perguntaMestre = perguntasMestreMap.get(r.perguntaCodigo);
                            if (!perguntaMestre) return null;

                            const tipo = perguntaMestre.tipo;
                            let respostaFormatada = r.respostaTexto;
                            
                            if (r.opcaoSelecionadaCodigo && perguntaMestre.opcoesMap.has(r.opcaoSelecionadaCodigo)) {
                                respostaFormatada = perguntaMestre.opcoesMap.get(r.opcaoSelecionadaCodigo);
                            }

                            const status = (index % 2 === 0) ? 'correto' : 'incorreto'; // Mocado
                            if (status === 'correto') respostasCorretas++;
                            
                            return {
                                id: r.perguntaCodigo,
                                texto: perguntaMestre.texto,
                                tipo: tipo,
                                resposta: respostaFormatada,
                                status: status, // Mocado
                                respostaEsperada: "Exemplo Resposta Esperada", // Mocado
                                nota: tipo === 'Escala' ? parseInt(r.respostaTexto, 10) : undefined,
                                escalaMax: tipo === 'Escala' ? 5 : undefined // Mocado (ajustar)
                            };
                        }).filter(p => p != null);

                    } catch (respostasErr) {
                        console.error(`Falha ao buscar respostas para instância ${instancia.codigo}:`, respostasErr);
                    }
                } // Fim if (status !== 'PENDENTE')
                
                // Retorna o objeto formatado para o destinatário
                return {
                    id: instancia.codigo, // ID da INSTÂNCIA
                    nome: instancia.funcionarioNome,
                    iniciais: getInitials(instancia.funcionarioNome),
                    cor: getRandomColor(),
                    dataEnvio: 'Não disponível', // Dado não vem da API
                    respostas: respostasCorretas, // Mocado
                    total: perguntasMestreMap.size,
                    perguntas: perguntasComRespostas,
                    status: instancia.resultadoStatus,
                    
                    // Pré-popula campos de revisão com dados existentes do banco
                    revisaoNota: instancia.nota || null,
                    revisaoComentarioSupervisor: instancia.comentarioSupervisao || '',
                    revisaoComentarioColaborador: instancia.comentarioColaborador || '',
                    // Define um status padrão para o formulário se ainda não foi revisado
                    revisaoStatus: (instancia.resultadoStatus !== 'PENDENTE' && instancia.resultadoStatus !== 'AGUARDANDO_REVISAO') ? instancia.resultadoStatus : 'APROVADO'
                };
            });

            destinatarios.value = await Promise.all(promessasDestinatarios);

            // Expande o primeiro destinatário que tiver respostas ou estiver aguardando revisão
            const primeiroParaRevisar = destinatarios.value.find(d => d.status === 'AGUARDANDO_REVISAO' || d.perguntas.length > 0);
            if (primeiroParaRevisar) {
                expandidos.value[primeiroParaRevisar.id] = true;
            }
            
            console.log("Destinatários Mapeados:", destinatarios.value);
        } // Fim do else (instancias.length > 0)

    } catch (err) {
        console.error("Erro fatal ao carregar tela de revisão:", err);
        error.value = "Não foi possível carregar os dados desta avaliação.";
    } finally {
        loading.value = false; // <<< Define o loading como falso no final
    }
});

// ===== Funções Auxiliares (AGORA DEFINIDAS) =====

function mapTipoPergunta(tipoBackend) {
    switch (tipoBackend?.toLowerCase()) {
        case 'texto': return 'Texto';
        case 'multipla escolha': return 'Múltipla Escolha';
        case 'escala/nota': return 'Escala';
        default: return 'Texto';
    }
}

function getInitials(fullName) {
    if (!fullName) return '??';
    const names = fullName.trim().split(/\s+/);
    const initials = names.length > 1
        ? `${names[0][0]}${names[names.length - 1][0]}`
        : names[0].substring(0, 2);
    return initials.toUpperCase();
}

function getRandomColor() {
    const colors = ['#3a6ce1', '#11a966', '#d33b53', '#92400e', '#5477d8'];
    return colors[Math.floor(Math.random() * colors.length)];
}

function labelStatus(statusBackend) {
  // Mapeia o status INDIVIDUAL da instância
  switch (statusBackend) {
      case 'PENDENTE': return 'Pendente';
      case 'AGUARDANDO_REVISAO': return 'Enviada (Aguardando Revisão)';
      case 'APROVADO': return 'Concluída (Aprovado)';
      case 'REPROVADO': return 'Concluída (Reprovado)';
      default: return statusBackend || 'Desconhecido';
  }
}

function statusClass(statusBackend) {
  // Mapeia o status INDIVIDUAL para a cor do badge
  switch (statusBackend) {
      case 'PENDENTE': return 'badge--blue';
      case 'AGUARDANDO_REVISAO': return 'badge--yellow';
      case 'APROVADO': return 'badge--green';
      case 'REPROVADO': return 'badge--red';
      default: return 'badge--muted';
  }
}

// ===== Função para Salvar a Revisão (FINALMENTE CONECTADA) =====

async function handleSalvarRevisao(destinatario) {
    console.log("Salvando revisão para:", destinatario.nome, destinatario.id);

    // 1. Validação (simples)
    if (!destinatario.revisaoNota || destinatario.revisaoNota < 1 || destinatario.revisaoNota > 5) { // Assumindo nota 1-5
       alert('Por favor, insira uma nota válida (1-5).');
       return;
    }
    if (!destinatario.revisaoComentarioSupervisor || !destinatario.revisaoComentarioSupervisor.trim()) {
       alert('Por favor, preencha o comentário privado do supervisor.');
       return;
    }
    if (!destinatario.revisaoStatus) {
       alert('Por favor, selecione um status final.');
       return;
    }

    // 2. Montar Payload (igual ao RevisaoSupervisorRequestDTO)
    const payload = {
      comentarioSupervisao: destinatario.revisaoComentarioSupervisor,
      comentarioParaColaborador: destinatario.revisaoComentarioColaborador,
      nota: destinatario.revisaoNota,
      resultadoStatus: destinatario.revisaoStatus
    };

    console.log("Enviando payload para /revisar:", payload);
    // Opcional: Adicionar um 'loading' no objeto destinatário para feedback visual
    destinatario.isLoading = true; 

    // 3. Chamar API
    try {
      const response = await axios.put(`http://localhost:8080/api/avaliacoes/instancias/${destinatario.id}/revisar`, payload);

      // 4. Sucesso
      console.log("Revisão salva com sucesso:", response.data);
      alert(`Revisão para ${destinatario.nome} salva com sucesso!`);

      // Atualiza o status localmente para o card mudar (de AGUARDANDO_REVISAO para APROVADO, etc.)
      destinatario.status = response.data.resultadoStatus;
      destinatario.nota = response.data.nota; // Pega a nota salva
      destinatario.comentarioSupervisao = response.data.comentarioSupervisao; // Pega o comentário salvo
      destinatario.comentarioColaborador = payload.comentarioParaColaborador; // Atualiza com o que foi enviado

    } catch (err) {
      console.error("Erro ao salvar revisão:", err);
      if (axios.isAxiosError(err) && err.response) {
           alert(`Erro ${err.response.status}: ${err.response.data || 'Falha ao salvar'}`);
      } else {
           alert("Erro desconhecido ao salvar revisão.");
      }
    } finally {
        destinatario.isLoading = false; // Para o 'loading'
    }
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

.btn { height: 36px; padding: 0 12px; border: 1px solid #e6effa; background: #fff; border-radius: 10px; cursor: pointer; color: #1f2d3d; font-size: 14px; display: inline-flex; align-items: center; gap: 8px; }
.icon { width: 36px; height: 36px; display: grid; place-items: center; border-radius: 10px; padding: 0; }
.icon__svg { width: 20px; height: 20px; stroke: #3a4b63; fill: none; stroke-width: 2; }
.btn:hover { background: #f8fafc; }
.btn--primary { background: #3a6ce1; color: #fff; border-color: #3a6ce1; font-weight: 500; }
.btn--primary:hover { background: #315fc6; }
.btn:disabled { opacity: .6; cursor: not-allowed; }


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
.perguntas-lista { 
  border-top: 1px solid #e6effa; 
  padding: 16px; 
  background: #fafcff; 
  /* Remove o padding de baixo se for seguido pela revisão */
  padding-bottom: 0; 
}
/* Remove a margem da última pergunta dentro da lista */
.perguntas-lista .pergunta-item:last-child {
   margin-bottom: 0;
}

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

/* ===== Bloco de Revisão ===== */
.revisao-box {
  background-color: #f9fbff;
  border-top: 2px dashed #cfe0fb;
  margin-top: 16px;
}
.revisao-titulo {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 16px;
}
.revisao-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 16px;
}
.revisao-campo {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}
.revisao-grid .revisao-campo {
  margin-bottom: 0;
}
.revisao-campo label {
  font-size: 13px;
  font-weight: 500;
  color: #3a4b63;
}
.revisao-input, .revisao-textarea, .revisao-campo select {
  width: 100%;
  padding: 0.625rem 1rem;
  font-size: 0.875rem;
  border: 1px solid #cbd5e1;
  border-radius: 0.5rem;
  background-color: #ffffff;
  color: #0f172a;
  outline: none;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
  -webkit-appearance: none;
  appearance: none;
}
.revisao-campo select {
   background-image: url('data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 24 24%22 fill=%22none%22 stroke=%22%23334155%22 stroke-width=%222%22%3E%3Cpolyline points=%226 9 12 15 18 9%22%3E%3C/polyline%3E%3C/svg%3E');
   background-repeat: no-repeat;
   background-position: right 0.75rem center;
   background-size: 1.25rem;
   padding-right: 2.5rem;
}
.revisao-input:focus, .revisao-textarea:focus, .revisao-campo select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59,130,246,.3);
}
.revisao-textarea {
  resize: vertical;
  min-height: 80px;
}

/* Box de revisão salva (read-only) */
.revisao-box-salva {
    background-color: #f8fafc;
    border-top: 1px solid #e2e8f0;
    margin-top: 16px;
    color: #334155;
}
.revisao-texto {
    font-size: 14px;
    margin: 8px 0 0;
    line-height: 1.5;
}
 .revisao-texto strong {
    color: #1e293b;
 }
.revisao-wrapper {
  padding: 16px;
  background: #fafcff; /* Mesmo fundo */
  
  /* Adiciona borda superior se NÃO for precedido pela lista de perguntas 
     (caso de usuário PENDENTE ou sem respostas) - 
     MAS nós já tratamos isso com v-if, então esse wrapper só aparece
     com a lista. Vamos simplificar: */
}

.perguntas-lista + .revisao-wrapper {
  padding-top: 0; 
}

.sem-respostas + .revisao-wrapper {
   border-top: 1px solid #e6effa;
}
/* Badge Amarelo */
.badge--yellow { background:#fffbeb; color:#b45309; border-color: #fde68a; }

/* Estilos de Loading/Erro (para v-if) */
.text-center { text-align: center; }
.py-10 { padding-top: 2.5rem; padding-bottom: 2.5rem; }
.text-slate-600 { color: #475569; }
.text-red-600 { color: #dc2626; }
.bg-red-50 { background: #fef2f2; }
.rounded-lg { border-radius: 0.5rem; }
.p-4 { padding: 1rem; }

@media (max-width: 768px) {
  .title__main { font-size: 16px; }
  .btn--primary { padding: 0 12px; font-size: 13px; }
  .revisao-grid { grid-template-columns: 1fr; } /* Coloca nota e status um sobre o outro em telas pequenas */
}
</style>