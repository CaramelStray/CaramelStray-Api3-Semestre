<template>
  <div class="min-h-screen bg-slate-50 text-slate-800">

    <!-- Main Content -->
    <main class="mx-auto max-w-7xl p-6">
      <div v-if="loading" class="text-center py-10 text-slate-600">
        Carregando respostas da avaliação...
      </div>
      
      <div v-else-if="error" class="text-center py-10 text-red-600 bg-red-50 rounded-lg p-4">
         {{ error }}
      </div>

      <template v-else>
        <div class="space-y-4">
          
          <p v-if="destinatarios.length === 0" class="text-center py-10 text-slate-600 bg-white rounded-xl border border-slate-200 shadow-sm">
            Nenhum colaborador foi associado a esta avaliação.
          </p>
          
          <article v-else v-for="dest in destinatarios" :key="dest.id" class="rounded-xl bg-white shadow-sm border border-slate-200 overflow-hidden">
            <!-- Header do Colaborador -->
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

            <!-- Conteúdo Expandido -->
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
                  </div>
                </div>
                
                <div class="revisao-wrapper"> 
                  
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
                </div>
              </div>
            </div>
          </article>
        </div>
      </template>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { useAuth } from '../auth';

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
.min-h-screen { min-height: 100vh; }
.bg-slate-50 { background: #f8fafc; }
.bg-white { background: #ffffff; }
.bg-blue-50 { background: #eff6ff; }
.bg-blue-100 { background: #dbeafe; }
.bg-blue-600 { background: #2563eb; }
.bg-red-50 { background: #fef2f2; }

/* Cores de texto */
.text-slate-500 { color: #64748b; }
.text-slate-600 { color: #475569; }
.text-slate-700 { color: #334155; }
.text-slate-800 { color: #1e293b; }
.text-blue-600 { color: #2563eb; }
.text-red-600 { color: #dc2626; }
.text-white { color: #ffffff; }

/* Bordas */
.border { border: 1px solid #e2e8f0; }
.border-slate-200 { border-color: #e2e8f0; }
.border-t-0 { border-top-width: 0; }
.border-x-0 { border-left-width: 0; border-right-width: 0; }
.border-t { border-top-width: 1px; }

/* Bordas arredondadas */
.rounded-lg { border-radius: 0.5rem; }
.rounded-xl { border-radius: 0.75rem; }
.rounded-full { border-radius: 9999px; }

/* Sombras */
.shadow-sm { box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05); }

/* Layout */
.mx-auto { margin-left: auto; margin-right: auto; }
.max-w-7xl { max-width: 80rem; }

/* Padding */
.p-4 { padding: 1rem; }
.p-6 { padding: 1.5rem; }
.px-3 { padding-left: 0.75rem; padding-right: 0.75rem; }
.px-4 { padding-left: 1rem; padding-right: 1rem; }
.py-1 { padding-top: 0.25rem; padding-bottom: 0.25rem; }
.py-2 { padding-top: 0.5rem; padding-bottom: 0.5rem; }
.py-3 { padding-top: 0.75rem; padding-bottom: 0.75rem; }
.py-4 { padding-top: 1rem; padding-bottom: 1rem; }
.py-10 { padding-top: 2.5rem; padding-bottom: 2.5rem; }
.pt-6 { padding-top: 1.5rem; }

/* Gap */
.gap-1 { gap: 0.25rem; }
.gap-2 { gap: 0.5rem; }
.gap-3 { gap: 0.75rem; }

/* Flexbox */
.flex { display: flex; }
.items-center { align-items: center; }

/* Grid */
.grid { display: grid; }
.place-items-center { place-items: center; }

/* Tamanhos */
.h-5 { height: 1.25rem; }
.h-8 { height: 2rem; }
.h-9 { height: 2.25rem; }
.w-5 { width: 1.25rem; }
.w-8 { width: 2rem; }
.w-9 { width: 2.25rem; }

/* Tipografia */
.text-xs { font-size: 0.75rem; line-height: 1rem; }
.text-sm { font-size: 0.875rem; line-height: 1.25rem; }
.text-base { font-size: 1rem; line-height: 1.5rem; }
.font-medium { font-weight: 500; }
.font-semibold { font-weight: 600; }
.font-bold { font-weight: 700; }

/* Miscelânea */
.text-center { text-align: center; }
.space-y-4 > * + * { margin-top: 1rem; }
.overflow-hidden { overflow: hidden; }

/* Hovers */
.hover\:bg-slate-100:hover { background: #f1f5f9; }

/* ===== Card Destinatário ===== */
.destinatario__header { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  padding: 1rem 1.5rem; 
  cursor: pointer; 
  transition: background 0.2s; 
}
.destinatario__header:hover { background: #f9fbff; }

.destinatario__info { display: flex; align-items: center; gap: 0.75rem; }
.avatar { 
  width: 2.5rem; 
  height: 2.5rem; 
  border-radius: 50%; 
  display: grid; 
  place-items: center; 
  color: #fff; 
  font-weight: 600; 
  font-size: 0.875rem; 
}
.destinatario__nome { font-weight: 600; color: #1e293b; font-size: 0.9375rem; }
.destinatario__meta { font-size: 0.8125rem; color: #64748b; margin-top: 0.125rem; }

.destinatario__actions { display: flex; align-items: center; gap: 0.75rem; color: #64748b; }
.chevron { width: 1.25rem; height: 1.25rem; stroke: currentColor; fill: none; stroke-width: 2; }

/* Badges */
.badge { 
  display: inline-flex; 
  align-items: center; 
  padding: 0.25rem 0.75rem; 
  border-radius: 0.375rem; 
  font-size: 0.75rem; 
  font-weight: 500; 
  border: 1px solid;
}
.badge--blue { background: #eff6ff; color: #2563eb; border-color: #bfdbfe; }
.badge--yellow { background: #fffbeb; color: #b45309; border-color: #fde68a; }
.badge--green { background: #f0fdf4; color: #16a34a; border-color: #bbf7d0; }
.badge--red { background: #fef2f2; color: #dc2626; border-color: #fecaca; }
.badge--muted { background: #f8fafc; color: #64748b; border-color: #e2e8f0; }

/* ===== Lista de Perguntas ===== */
.perguntas-lista { 
  border-top: 1px solid #e6effa; 
  padding: 1rem 1.5rem; 
  background: #fafcff; 
  padding-bottom: 0; 
}
.perguntas-lista .pergunta-item:last-child {
   margin-bottom: 0;
}

.pergunta-item { 
  background: #fff; 
  border: 1px solid #e6effa; 
  border-radius: 0.75rem; 
  padding: 1rem; 
  margin-bottom: 0.75rem; 
}
.pergunta-item:last-child { margin-bottom: 0; }

.pergunta__header { display: flex; gap: 0.5rem; margin-bottom: 0.5rem; }
.pergunta__numero { font-weight: 600; color: #2563eb; }
.pergunta__texto { font-weight: 500; color: #1e293b; flex: 1; }

.pergunta__tipo { margin-bottom: 0.75rem; }
.tipo-badge { 
  display: inline-block; 
  background: #f1f5f9; 
  color: #475569; 
  font-size: 0.75rem; 
  padding: 0.25rem 0.625rem; 
  border-radius: 0.375rem; 
  font-weight: 500; 
}

/* ===== Tipos de Resposta ===== */
.resposta-box { 
  background: #f3f8ff; 
  border: 1px solid #e1ebfb; 
  border-radius: 0.625rem; 
  padding: 0.75rem; 
  color: #27436b; 
  line-height: 1.5; 
}

.resposta-escolha { display: flex; flex-direction: column; gap: 0.625rem; }

.resposta-tag { 
  display: inline-flex; 
  align-items: center; 
  gap: 0.375rem; 
  padding: 0.25rem 0.625rem; 
  border-radius: 0.375rem; 
  font-size: 0.75rem; 
  font-weight: 600; 
  width: fit-content; 
}
.tag--correto { background: #f0fdf4; color: #16a34a; }
.tag--incorreto { background: #fef2f2; color: #dc2626; }
.tag__icon { width: 0.875rem; height: 0.875rem; stroke: currentColor; fill: none; stroke-width: 2.5; }

.resposta-valor { 
  background: #f3f8ff; 
  border: 1px solid #e1ebfb; 
  border-radius: 0.625rem; 
  padding: 0.625rem 0.75rem; 
  color: #27436b; 
}

.resposta-escala { display: flex; flex-direction: column; gap: 0.5rem; }
.escala-label { display: flex; align-items: baseline; gap: 0.25rem; font-weight: 600; color: #1e293b; }
.escala-max { color: #64748b; font-size: 0.8125rem; }
.escala-bar { height: 0.5rem; background: #e6effa; border-radius: 999px; overflow: hidden; }
.escala-fill { 
  height: 100%; 
  background: linear-gradient(90deg, #2563eb, #3b82f6); 
  border-radius: 999px; 
  transition: width 0.3s; 
}

.sem-respostas { 
  padding: 2rem 1rem; 
  text-align: center; 
  color: #64748b; 
  font-size: 0.875rem; 
}

/* ===== Bloco de Revisão ===== */
.revisao-wrapper {
  padding: 1rem 1.5rem;
  background: #fafcff;
}

.perguntas-lista + .revisao-wrapper {
  padding-top: 0; 
}

.sem-respostas + .revisao-wrapper {
   border-top: 1px solid #e6effa;
}

.revisao-box {
  background-color: #f9fbff;
  border-top: 2px dashed #cfe0fb;
  margin-top: 1rem;
}

.revisao-titulo {
  font-size: 1rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 1rem;
}

.revisao-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.revisao-campo {
  display: flex;
  flex-direction: column;
  gap: 0.375rem;
  margin-bottom: 1rem;
}

.revisao-grid .revisao-campo {
  margin-bottom: 0;
}

.revisao-campo label {
  font-size: 0.8125rem;
  font-weight: 500;
  color: #334155;
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
  min-height: 5rem;
}

/* Box de revisão salva (read-only) */
.revisao-box-salva {
    background-color: #f8fafc;
    border-top: 1px solid #e2e8f0;
    margin-top: 1rem;
    color: #334155;
}

.revisao-texto {
    font-size: 0.875rem;
    margin: 0.5rem 0 0;
    line-height: 1.5;
}

.revisao-texto strong {
    color: #1e293b;
}

/* Botão */
.btn { 
  height: 2.5rem; 
  padding: 0 1rem; 
  border: 1px solid #e2e8f0; 
  background: #fff; 
  border-radius: 0.5rem; 
  cursor: pointer; 
  color: #1e293b; 
  font-size: 0.875rem; 
  display: inline-flex; 
  align-items: center; 
  gap: 0.5rem; 
  font-weight: 500;
  transition: background 0.2s;
}

.btn:hover { background: #f8fafc; }

.btn--primary { 
  background: #2563eb; 
  color: #fff; 
  border-color: #2563eb; 
  font-weight: 600; 
}

.btn--primary:hover { background: #1d4ed8; }

.btn:disabled { opacity: .6; cursor: not-allowed; }

@media (max-width: 768px) {
  .revisao-grid { grid-template-columns: 1fr; }
}
</style>