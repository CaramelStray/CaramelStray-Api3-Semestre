<template>
  <div class="min-h-screen bg-slate-50 text-slate-800">
    <header class="bg-white border border-slate-200 border-t-0 border-x-0">
      <div class="mx-auto max-w-7xl px-4 py-4 flex items-center gap-3">
        <button 
          @click="handleVoltar"
          class="h-9 w-9 grid place-items-center rounded-lg border border-slate-200 hover:bg-slate-100" 
          aria-label="Voltar"
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5">
            <path fill-rule="evenodd" d="M10.03 4.97a.75.75 0 0 1 0 1.06L5.06 11l4.97 4.97a.75.75 0 1 1-1.06 1.06l-5.5-5.5a.75.75 0 0 1 0-1.06l5.5-5.5a.75.75 0 0 1 1.06 0Zm4.94 0a.75.75 0 0 1 1.06 0l5.5 5.5a.75.75 0 0 1 0 1.06l-5.5 5.5a.75.75 0 1 1-1.06-1.06L18.94 11l-4.97-4.97a.75.75 0 0 1 1.06-1.06Z" clip-rule="evenodd"/>
          </svg>
        </button>
        
        <div class="flex items-center gap-2">
          <div class="h-8 w-8 rounded-lg bg-blue-100 text-blue-600 grid place-items-center">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5">
              <path fill-rule="evenodd" d="M5.625 1.5H9a3.75 3.75 0 0 1 3.75 3.75v1.875c0 1.036.84 1.875 1.875 1.875H16.5a3.75 3.75 0 0 1 3.75 3.75v7.875c0 1.035-.84 1.875-1.875 1.875H5.625a1.875 1.875 0 0 1-1.875-1.875V3.375c0-1.036.84-1.875 1.875-1.875Z" clip-rule="evenodd" />
            </svg>
          </div>
          <div>
            <h1 class="text-base font-semibold text-slate-800">Cadastro de Perguntas</h1>
            <p class="text-xs text-slate-500">Avaliador {{ usuarioLogado?.nomeCompleto || 'Carregando...' }}</p>
          </div>
        </div>

        <div class="ml-auto flex items-center gap-2">
          <button 
            @click="handleSalvarPergunta"
            class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
          >
            Salvar Pergunta
          </button>
        </div>
      </div>
    </header>

    <main class="mx-auto max-w-4xl p-6">
      <div v-if="loading" class="text-center py-10 text-slate-600">
        <p>Carregando informações do colaborador...</p>
      </div>

      <div v-else-if="error" class="text-center py-10 text-red-600 bg-red-50 rounded-lg p-4">
        <p class="font-semibold">Ocorreu um erro</p>
        <p class="text-sm">{{ error }}</p>
      </div>

      <template v-else-if="usuarioLogado">
        <!-- ====== PERGUNTAS CADASTRADAS ====== -->
        <div v-if="perguntas.length > 0" class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6">
          <h3 class="text-base font-semibold text-slate-800 mb-4">
            Perguntas Cadastradas ({{ perguntas.length }})
          </h3>

          <div class="space-y-4">
            <div 
              v-for="(p, index) in perguntas" 
              :key="index"
              class="border border-slate-200 rounded-lg p-4 bg-slate-50"
            >
              <!-- Cabeçalho com competência e tipo -->
              <div class="flex items-center gap-2 mb-2 flex-wrap">
                <span class="inline-block px-3 py-1 rounded-full text-xs font-medium border border-blue-300 bg-blue-50 text-blue-700">
                  {{ obterNomeCompetencia(p.competenciaId) }}
                </span>
                <span v-if="p.tipoPergunta === 'multipla escolha'" class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-slate-300 bg-white text-slate-700">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="h-3 w-3">
                    <polyline points="12 3 20 7.5 20 16.5 12 21 4 16.5 4 7.5 12 3"></polyline>
                  </svg>
                  Múltipla Escolha
                </span>
                <span v-else-if="p.tipoPergunta === 'escala/nota'" class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-slate-300 bg-white text-slate-700">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="h-3 w-3">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                  </svg>
                  Escala/Nota
                </span>
                <span v-else class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-slate-300 bg-white text-slate-700">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="h-3 w-3">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                  </svg>
                  Texto
                </span>
                <span class="ml-auto text-xs text-slate-500">Pergunta {{ index + 1 }}</span>
              </div>

              <!-- Texto da pergunta -->
              <p class="text-sm font-medium text-slate-800 mb-3">{{ p.pergunta }}</p>

              <!-- Opções (se houver) -->
              <div v-if="p.tipoPergunta === 'multipla escolha' && p.opcoes && p.opcoes.length">
                <p class="text-xs font-medium text-slate-600 mb-2">Opções (seleção múltipla):</p>
                <ul class="list-disc list-inside space-y-1">
                  <li v-for="(opcao, idx) in p.opcoes" :key="idx" class="text-sm text-slate-700">
                    {{ opcao }}
                  </li>
                </ul>
              </div>

              <!-- Botão deletar -->
              <div class="mt-4 flex justify-end">
                <button
                  @click="handleDeletarPergunta(index)"
                  class="h-9 w-9 grid place-items-center rounded-lg border border-red-200 bg-red-50 text-red-600 hover:bg-red-100"
                  aria-label="Deletar pergunta"
                  title="Deletar pergunta"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4">
                    <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- ====== Card Filtros ====== -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6">
          <div class="flex items-center gap-2 mb-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" viewBox="0 0 24 24" fill="currentColor">
              <path d="M3 5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v1.586a2 2 0 0 1-.586 1.414l-5.121 5.121a2 2 0 0 0-.586 1.414V18l-4 2v-4.465a2 2 0 0 0-.586-1.414L3.586 8A2 2 0 0 1 3 6.586V5z"/>
            </svg>
            <h3 class="text-base font-semibold text-slate-800">Filtros de Avaliação</h3>
          </div>
          <p class="text-sm text-slate-600 mb-4">
            Selecione os filtros para definir quais colaboradores receberão esta avaliação
          </p>

          <div class="grid gap-4 md:grid-cols-3">
            <!-- Colaboradores (typeahead) -->
            <div>
              <label class="block mb-1 text-sm font-medium text-slate-700">Colaboradores</label>
              <div class="search-select" ref="wrapColab">
                <input
                  v-model="qColab"
                  @focus="open.colab = true"
                  type="text"
                  class="search-input"
                  placeholder="Digite para buscar colaboradores..."
                  aria-label="Buscar colaboradores"
                />
                <ul v-if="open.colab && filteredColabs.length" class="search-list">
                  <li
                    v-for="c in filteredColabs"
                    :key="c.id"
                    class="search-item"
                    @mousedown.prevent="addColab(c)"
                  >
                    <span class="item-title">{{ c.nome }}</span>
                    <span class="item-sub">{{ c.cargo }}</span>
                  </li>
                </ul>
              </div>
              <p class="text-xs text-slate-500 mt-1">Digite e clique para adicionar</p>
            </div>

            <!-- Competências (typeahead + seleção) -->
            <div>
              <label class="block mb-1 text-sm font-medium text-slate-700">Competências</label>
              <div class="search-select" ref="wrapComp">
                <input
                  v-model="qComp"
                  @focus="open.comp = true"
                  type="text"
                  class="search-input"
                  placeholder="Digite para buscar competências..."
                  aria-label="Buscar competências"
                />
                <ul v-if="open.comp && filteredComps.length" class="search-list">
                  <li
                    v-for="comp in filteredComps"
                    :key="comp.codigo"
                    class="search-item"
                    @mousedown.prevent="addComp(comp)"
                  >
                    <span class="item-title">{{ comp.nome }}</span>
                  </li>
                </ul>
              </div>
              <p class="text-xs text-slate-500 mt-1">Digite e clique para adicionar</p>
            </div>

            <!-- Equipes (typeahead) -->
            <div>
              <label class="block mb-1 text-sm font-medium text-slate-700">Equipes</label>
              <div class="search-select" ref="wrapEquipe">
                <input
                  v-model="qEquipe"
                  @focus="open.equipe = true"
                  type="text"
                  class="search-input"
                  placeholder="Digite para buscar equipes..."
                  aria-label="Buscar equipes"
                />
                <ul v-if="open.equipe && filteredEquipes.length" class="search-list">
                  <li
                    v-for="e in filteredEquipes"
                    :key="e.id"
                    class="search-item"
                    @mousedown.prevent="addEquipe(e)"
                  >
                    <span class="item-title">{{ e.nome }}</span>
                  </li>
                </ul>
              </div>
              <p class="text-xs text-slate-500 mt-1">Digite e clique para adicionar</p>
            </div>
          </div>
        </div>

        <!-- Selecionados -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6">
          <p class="text-sm font-medium text-slate-700 mb-3">Colaboradores selecionados:</p>

          <div class="flex flex-wrap gap-3 mb-5">
            <div v-for="c in filtros.colaboradores" :key="c.id" class="chip">
              <span class="chip__avatar">{{ getInitials(c.nome) }}</span>
              <span class="chip__content">
                <span class="chip__title">{{ c.nome }}</span>
                <span class="chip__subtitle">{{ c.cargo }}</span>
              </span>
            </div>
            <p v-if="filtros.colaboradores.length === 0" class="text-sm text-slate-500">
              Nenhum colaborador selecionado
            </p>
          </div>

          <p class="text-sm font-medium text-slate-700 mb-1">Competências selecionadas (filtro):</p>
          <div class="flex flex-wrap gap-2">
            <span
              v-for="comp in filtros.competencias"
              :key="comp.codigo"
              class="inline-block px-3 py-1 rounded-full text-xs font-medium border border-blue-300 bg-blue-50 text-blue-700"
            >
              {{ comp.nome }}
            </span>

            <p v-if="filtros.competencias.length === 0" class="text-sm text-slate-500">
              Nenhuma competência adicionada ao filtro
            </p>
          </div>
        </div>

        <!-- Card Nova Pergunta -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6">
          <h3 class="text-base font-semibold text-slate-800 mb-4">Nova Pergunta</h3>

          <!-- Competência Selecionada (visível) -->
          <div class="mb-4">
            <p class="text-sm text-slate-600">
              <span class="font-medium">Competência vinculada:</span>
              <span v-if="competenciaSelecionadaNome" class="text-slate-800"> {{ competenciaSelecionadaNome }}</span>
              <span v-else class="text-slate-500"> nenhuma selecionada</span>
            </p>
          </div>

          <!-- Tipo de Pergunta -->
          <div class="mb-4">
            <label class="text-sm font-medium text-slate-700 mb-2 block">Tipo de Resposta</label>
            <select
              v-model="tipoPergunta"
              class="w-full px-3 py-2 rounded-lg border border-slate-300 bg-white text-slate-800 appearance-none cursor-pointer"
              style="background-image: url('data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 24 24%22 fill=%22none%22 stroke=%22%23334155%22 stroke-width=%222%22%3E%3Cpolyline points=%226 9 12 15 18 9%22%3E%3C/polyline%3E%3C/svg%3E'); background-repeat: no-repeat; background-position: right 0.75rem center; background-size: 1.25rem; padding-right: 2.5rem;"
              aria-label="Selecionar tipo de pergunta"
            >
              <option value="texto">
                <span>T</span> Texto
              </option>
              <option value="multipla escolha">
                <span>≡</span> Múltipla Escolha
              </option>
              <option value="escala/nota">
                <span>#</span> Escala/Nota
              </option>
            </select>
            <p class="text-xs text-slate-500 mt-1">
              Escolha como o colaborador irá responder.
            </p>
          </div>

          <!-- Campo Pergunta -->
          <div class="mb-4">
            <label class="text-sm font-medium text-slate-700 mb-2 block">Pergunta</label>
            <textarea
              v-model="pergunta"
              rows="3"
              placeholder="Digite a pergunta relacionada à competência selecionada..."
              style="width: 100%; padding: 0.625rem 1rem; font-size: 0.875rem; border: 1px solid #cbd5e1; border-radius: 0.5rem; background-color: #ffffff; resize: none; color: black;"
              @focus="$event.target.style.borderColor = '#3b82f6'; $event.target.style.boxShadow = '0 0 0 3px rgba(59, 130, 246, 0.3)';"
              @blur="$event.target.style.borderColor = '#cbd5e1'; $event.target.style.boxShadow = 'none';"
            />
          </div>

          <!-- Opções de Resposta (só quando múltipla escolha) -->
          <div v-if="tipoPergunta === 'multipla escolha'">
            <label class="text-sm font-medium text-slate-700 mb-2 block">
              Opções de Resposta <span class="text-slate-500 font-normal">(Obrigatórias para múltipla escolha)</span>
            </label>
            
            <div class="space-y-2">
              <div 
                v-for="(opcao, index) in opcoes" 
                :key="index" 
                class="flex items-center gap-2"
              >
                <input
                  type="text"
                  v-model="opcoes[index]"
                  :placeholder="`Opção ${index + 1}`"
                  style="flex: 1; padding: 0.625rem 1rem; font-size: 0.875rem; border: 1px solid #cbd5e1; border-radius: 0.5rem; background-color: #ffffff; color: black;"
                  @focus="$event.target.style.borderColor = '#3b82f6'; $event.target.style.boxShadow = '0 0 0 3px rgba(59,130,246,0.3)';"
                  @blur="$event.target.style.borderColor = '#cbd5e1'; $event.target.style.boxShadow = 'none';"
                />
                <button
                  v-if="opcoes.length > 1"
                  @click="handleRemoverOpcao(index)"
                  class="h-9 w-9 grid place-items-center rounded-lg border border-red-200 bg-red-50 text-red-600 hover:bg-red-100"
                  aria-label="Remover opção"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4">
                    <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                  </svg>
                </button>
              </div>
            </div>

            <button
              @click="handleAdicionarOpcao"
              class="mt-3 text-sm text-blue-600 hover:text-blue-700 font-medium flex items-center gap-1"
            >
              <span class="text-lg">+</span> Adicionar Opção
            </button>
          </div>

          <!-- Botão Adicionar Pergunta -->
          <div class="mt-6 pt-6 border-t border-slate-200">
            <button
              @click="handleSalvarPergunta"
              class="w-full rounded-lg bg-blue-600 px-4 py-3 text-sm font-semibold text-white hover:bg-blue-700"
            >
              + Adicionar Pergunta
            </button>
          </div>
        </div>
      </template>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import {useAuth} from '../auth';


const route = useRoute();
const router = useRouter();

const props = defineProps({ 
  id: String 
});



const { usuarioLogado, carregarUsuarioLogado } = useAuth();
const loading = ref(true);
const error = ref(null);
const pergunta = ref('');
const tipoPergunta = ref('texto');
const opcoes = ref(['']);
const competenciaSelecionada = ref('');
const perguntas = ref([]);
const allColaboradores = ref([]);
const allCompetencias = ref([]);
const allAreas = ref([]); // "Equipes" vêm da tabela "Area"
// ---- estados dos filtros (mock simples) ----
const filtros = ref({
  colaboradores: [],
  competencias: [],
  equipes: []
});


// ---------- BUSCA DO FUNCIONÁRIO ----------
/*const fetchFuncionarioData = async () => {
  loading.value = true;
  error.value = null;
  try {
    const funcionarioId = props.id || route.params.id;
    const response = await axios.get(`http://localhost:8080/api/funcionario`);
    funcionario.value = response.data;
  } catch (err) {
    console.error("Falha ao buscar dados do funcionário:", err);
    error.value = "Não foi possível carregar as informações. Tente novamente mais tarde.";
  } finally {
    loading.value = false;
  }
};

onMounted(fetchFuncionarioData);*/

onMounted(async () => {
  loading.value = true;
  error.value = null;
  try {
    // Pega o ID da prop (vindo da rota)
    const idUsuario = props.id; 
    if (!idUsuario) {
      throw new Error("ID do usuário logado não fornecido via props/rota.");
    }

    // 1. PRIMEIRO, carrega o usuário logado usando o ID da prop
    await carregarUsuarioLogado(Number(idUsuario)); 

    // VERIFICA se o usuário foi carregado com sucesso ANTES de prosseguir
    if (!usuarioLogado.value) {
        throw new Error("Falha ao carregar dados do usuário logado.");
    }

    // 2. DEPOIS, busca os dados dos filtros
    await fetchFilterData();
    
  } catch (err) {
    console.error("Falha ao carregar dados iniciais:", err);
    error.value = "Não foi possível carregar os dados. " + (err.message || '');
  } finally {
    loading.value = false;
  }

  // Adiciona o listener para fechar dropdowns
  document.addEventListener('click', handleClickOutside);
});

const fetchFilterData = async () => {
  try {
    // Busca simultânea dos 3 endpoints
    const [colabRes, compRes, areaRes] = await Promise.all([
      axios.get('http://localhost:8080/api/funcionario'), 
      axios.get('http://localhost:8080/api/competencia'), // Ajuste se a rota for plural
      axios.get('http://localhost:8080/api/area')      // Ajuste se a rota for diferente
    ]);

    // Mapeia a resposta de /api/funcionario para o formato do filtro
    allColaboradores.value = colabRes.data.map(c => ({
      codigo: c.codigo,
      nome: c.nomeCompleto,
      cargo: c.tituloProfissional || 'Cargo não informado',
      areaId: c.areaId,     
      perfilId: c.perfilId   
    }));
    
    // Assume que /api/competencia retorna { codigo, nome }
    allCompetencias.value = compRes.data; 
    
    // Assume que /api/areas retorna { codigo, nome }
    allAreas.value = areaRes.data; 

  } catch (err) {
    console.error("Falha ao buscar dados dos filtros:", err);
    throw new Error("Falha ao carregar listas de filtros. Verifique os endpoints da API.");
  }
};

// ---------- UTIL ---------- 
const getInitials = (fullName) => {
  if (!fullName) return '';
  const names = fullName.trim().split(/\s+/);
  const initials = names.length > 1
    ? `${names[0][0]}${names[names.length - 1][0]}`
    : names[0].substring(0, 2);
  return initials.toUpperCase();
};

const obterNomeCompetencia = (codigo) => {
  //const lista = listaCompetenciasBase.value;
  const found = lista.find(c => c.codigo === codigo);
  return found?.nome || codigo;
};

const handleVoltar = () => {
  router.back();
};

const handleAdicionarOpcao = () => {
  opcoes.value.push('');
};

const handleRemoverOpcao = (index) => {
  if (opcoes.value.length > 1) {
    opcoes.value.splice(index, 1);
  }
};

/*const competenciaSelecionadaNome = computed(() => {
  const lista = listaCompetenciasParaSelecao.value;
  const found = lista.find(c => c.codigo === competenciaSelecionada.value);
  return found?.nome || '';
});*/

const handleSalvarPergunta = async () => {
  if (!pergunta.value.trim()) {
    alert('Por favor, digite a pergunta.');
    return;
  }

  if (!competenciaSelecionada.value) {
    alert('Por favor, selecione uma competência.');
    return;
  }

  const opcoesValidas = opcoes.value
    .map(op => op.trim())
    .filter(op => op !== '');

  if (tipoPergunta.value === 'multipla escolha' && opcoesValidas.length < 2) {
    alert('Para múltipla escolha, informe ao menos 2 opções.');
    return;
  }

  const novaPergunta = {
    pergunta: pergunta.value.trim(),
    competenciaId: competenciaSelecionada.value,
    tipoPergunta: tipoPergunta.value,
    opcoes: (tipoPergunta.value === 'multipla escolha') ? opcoesValidas : null
  };

  try {
    const funcionarioId = props.id || route.params.id;
    
    console.log("Enviando pergunta:", novaPergunta);
    console.log("URL:", `http://localhost:8080/api/perguntas`);
    
    // Tenta primeiro com o novo endpoint
    try {
      const response = await axios.post(`http://localhost:8080/api/perguntas`, {
        ...novaPergunta,
        funcionarioId: funcionarioId
      });
      console.log("Resposta do servidor:", response.data);
    } catch (err) {
      // Se falhar, tenta o endpoint antigo
      if (err.response?.status === 404) {
        console.log("Tentando endpoint alternativo...");
        const response = await axios.post(`http://localhost:8080/api/funcionario/${funcionarioId}/perguntas`, novaPergunta);
        console.log("Resposta do servidor:", response.data);
      } else {
        throw err;
      }
    }
    
    // Adiciona à lista local
    perguntas.value.push(novaPergunta);
    
    alert('Pergunta cadastrada com sucesso!');
    
    // Limpa o formulário
    pergunta.value = '';
    tipoPergunta.value = 'texto';
    opcoes.value = [''];
  } catch (err) {
    console.error("Erro completo:", err);
    console.error("Mensagem:", err.message);
    console.error("Response:", err.response);
    
    let mensagem = "Ocorreu um erro ao salvar a pergunta.";
    if (err.response?.status === 404) {
      mensagem = "Endpoint não encontrado. Verifique qual é o endpoint correto no backend para salvar perguntas.";
    } else if (err.response?.status === 400) {
      mensagem = `Erro na requisição: ${err.response.data?.message || 'Dados inválidos'}`;
    } else if (err.response?.status === 500) {
      mensagem = `Erro no servidor: ${err.response.data?.message || 'Erro interno'}`;
    } else if (err.code === 'ERR_NETWORK') {
      mensagem = "Não foi possível conectar ao servidor. Verifique se localhost:8080 está ativo.";
    }
    
    alert(mensagem);
  }
};

const handleDeletarPergunta = (index) => {
  if (confirm('Tem certeza que deseja deletar esta pergunta?')) {
    perguntas.value.splice(index, 1);
  }
};

// ================== TYPEAHEAD ==================
const qColab  = ref('');
const qComp   = ref('');
const qEquipe = ref('');
const open    = ref({ colab: false, comp: false, equipe: false });

const wrapColab  = ref(null);
const wrapComp   = ref(null);
const wrapEquipe = ref(null);

const normaliza = (s) =>
  (s || '')
    .toString()
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .toLowerCase();

const jaTemColab = (codigo) => filtros.value.colaboradores.some((x) => x.codigo === codigo);
const jaTemComp = (codigo) => filtros.value.competencias.some((x) => x.codigo === codigo);
const jaTemEquipe = (codigo) => filtros.value.equipes.some((x) => x.codigo === codigo);

/*const listaCompetenciasBase = computed(() => {
  const lista = funcionario.value?.competencias;
  return Array.isArray(lista) && lista.length ? lista : competenciasMock.value;
});

const listaCompetenciasParaSelecao = computed(() => {
  return filtros.value.competencias.length
    ? filtros.value.competencias
    : listaCompetenciasBase.value;
});

const filteredColabs = computed(() => {
  const q = normaliza(qColab.value);
  return colaboradoresOptions.value
    .filter((c) => !jaTemColab(c.id))
    .filter((c) => !q || normaliza(c.nome).includes(q) || normaliza(c.cargo).includes(q))
    .slice(0, 8);
});

const filteredComps = computed(() => {
  const lista = listaCompetenciasBase.value;
  const q = normaliza(qComp.value);
  return lista
    .filter((c) => !jaTemComp(c.codigo))
    .filter((c) => !q || normaliza(c.nome).includes(q))
    .slice(0, 8);
});

const filteredEquipes = computed(() => {
  const q = normaliza(qEquipe.value);
  return equipesOptions.value
    .filter((e) => !jaTemEquipe(e.id))
    .filter((e) => !q || normaliza(e.nome).includes(q))
    .slice(0, 8);
});
*/

const listaCompetenciasParaSelecao = computed(() => {
  if (filtros.value.competencias.length) {
    return filtros.value.competencias;
  }
  if (usuarioLogado.value?.competencias?.length) { 
    return usuarioLogado.value.competencias;
  }
  return allCompetencias.value;
});

const filteredColabs = computed(() => {
  const userLogado = usuarioLogado.value;
  if (!userLogado || !userLogado.perfilId || !userLogado.areaId) return [];

  const userPerfilId = userLogado.perfilId;
  const userAreaId = userLogado.areaId;
  const q = normaliza(qColab.value);

  return allColaboradores.value
    .filter((colab) => !jaTemColab(colab.codigo))
    .filter((colab) => {
        if (colab.codigo === userLogado.codigo) return false; // Auto-avaliação
        const podeAvaliarNivel = userPerfilId < colab.perfilId; // Permissão
        const mesmaArea = colab.areaId === userAreaId;          // Área
        return podeAvaliarNivel && mesmaArea;
    })
    .filter((colab) => !q || normaliza(colab.nome).includes(q) || normaliza(colab.cargo).includes(q))
    .slice(0, 30);
});

const filteredComps = computed(() => {
  const q = normaliza(qComp.value);
  return allCompetencias.value 
    .filter((c) => !jaTemComp(c.codigo))
    .filter((c) => !q || normaliza(c.nome).includes(q))
    .slice(0, 30);
});

const filteredEquipes = computed(() => {
  const q = normaliza(qEquipe.value);
  return allAreas.value 
    .filter((e) => !jaTemEquipe(e.codigo))
    .filter((e) => !q || normaliza(e.nome).includes(q))
    .slice(0, 30);
});

const addColab = (c) => {
  if (!jaTemColab(c.codigo)) filtros.value.colaboradores.push(c);
  qColab.value = '';
};

const addComp = (comp) => {
  if (!jaTemComp(comp.codigo)) filtros.value.competencias.push(comp);
  qComp.value = '';
  if (!competenciaSelecionada.value) competenciaSelecionada.value = comp.codigo;
};

const addEquipe = (e) => {
  if (!jaTemEquipe(e.codigo)) filtros.value.equipes.push(e);
  qEquipe.value = '';
};

const handleClickOutside = (ev) => {
  const t = ev.target;
  if (wrapColab.value && !wrapColab.value.contains(t)) open.value.colab = false;
  if (wrapComp.value && !wrapComp.value.contains(t)) open.value.comp = false;
  if (wrapEquipe.value && !wrapEquipe.value.contains(t)) open.value.equipe = false;
};

/*onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});*/

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
/* Cores de fundo */
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
.text-blue-700 { color: #1d4ed8; }
.text-red-600 { color: #dc2626; }
.text-white { color: #ffffff; }

/* Bordas */
.border { border: 1px solid #e2e8f0; }
.border-slate-200 { border-color: #e2e8f0; }
.border-slate-300 { border-color: #cbd5e1; }
.border-blue-300 { border-color: #93c5fd; }
.border-red-200 { border-color: #fecaca; }
.border-t { border-top-width: 1px; }

/* Bordas arredondadas */
.rounded-lg { border-radius: 0.5rem; }
.rounded-xl { border-radius: 0.75rem; }
.rounded-md { border-radius: 0.375rem; }
.rounded-full { border-radius: 9999px; }

/* Sombras */
.shadow-sm { box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05); }

/* Layout */
.min-h-screen { min-height: 100vh; }
.mx-auto { margin-left: auto; margin-right: auto; }
.max-w-4xl { max-width: 56rem; }
.max-w-7xl { max-width: 80rem; }

/* Padding */
.p-2 { padding: 0.5rem; }
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

/* Margin */
.mt-1 { margin-top: 0.25rem; }
.mt-2 { margin-top: 0.5rem; }
.mt-3 { margin-top: 0.75rem; }
.mt-4 { margin-top: 1rem; }
.mt-6 { margin-top: 1.5rem; }
.mb-1 { margin-bottom: 0.25rem; }
.mb-2 { margin-bottom: 0.5rem; }
.mb-3 { margin-bottom: 0.75rem; }
.mb-4 { margin-bottom: 1rem; }
.mb-6 { margin-bottom: 1.5rem; }
.ml-auto { margin-left: auto; }

/* Gap */
.gap-1 { gap: 0.25rem; }
.gap-2 { gap: 0.5rem; }
.gap-4 { gap: 1rem; }

/* Flexbox */
.flex { display: flex; }
.flex-1 { flex: 1 1 0%; }
.flex-wrap { flex-wrap: wrap; }
.items-center { align-items: center; }
.items-start { align-items: flex-start; }

/* Grid */
.grid { display: grid; }
.place-items-center { place-items: center; }

/* Tamanhos */
.h-3 { height: 0.75rem; }
.h-4 { height: 1rem; }
.h-5 { height: 1.25rem; }
.h-8 { height: 2rem; }
.h-9 { height: 2.25rem; }
.w-3 { width: 0.75rem; }
.w-4 { width: 1rem; }
.w-5 { width: 1.25rem; }
.w-8 { width: 2rem; }
.w-9 { width: 2.25rem; }
.w-full { width: 100%; }

/* Tipografia */
.text-xs { font-size: 0.75rem; line-height: 1rem; }
.text-sm { font-size: 0.875rem; line-height: 1.25rem; }
.text-base { font-size: 1rem; line-height: 1.5rem; }
.text-lg { font-size: 1.125rem; line-height: 1.75rem; }
.font-medium { font-weight: 500; }
.font-semibold { font-weight: 600; }

/* Misc */
.block { display: block; }
.inline-block { display: inline-block; }
.inline-flex { display: inline-flex; }
.shrink-0 { flex-shrink: 0; }
.resize-none { resize: none; }
.text-center { text-align: center; }
.transition-colors { transition-property: color, background-color, border-color; transition-duration: 150ms; }
.space-y-1 > * + * { margin-top: 0.25rem; }
.space-y-2 > * + * { margin-top: 0.5rem; }
.space-y-4 > * + * { margin-top: 1rem; }
.list-disc { list-style-type: disc; }
.list-inside { list-style-position: inside; }

/* Hovers */
.hover\:bg-slate-100:hover { background: #f1f5f9; }
.hover\:bg-blue-50:hover { background: #eff6ff; }
.hover\:bg-blue-700:hover { background: #1d4ed8; }
.hover\:bg-red-100:hover { background: #fee2e2; }
.hover\:text-blue-700:hover { color: #1d4ed8; }
.hover\:border-blue-300:hover { border-color: #93c5fd; }

/* Responsive */
@media (min-width: 768px) {
  .md\:grid-cols-3 {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

/* ====== estilos extras para os cards ====== */

/* Chips de colaboradores */
.chip {
  display: inline-flex; align-items: center; gap: 10px;
  padding: 10px 12px; border: 1px solid #dbeafe; background: #f8fbff;
  border-radius: 12px; box-shadow: 0 1px 0 rgba(0,0,0,.02);
}
.chip__avatar {
  height: 36px; width: 36px; display: grid; place-items: center;
  border-radius: 9999px; background: #e5efff; color: #1d4ed8;
  font-weight: 700; font-size: 0.875rem;
  box-shadow: inset 0 0 0 4px #f2f6ff;
}
.chip__content { display: grid; }
.chip__title { font-size: 0.9rem; font-weight: 700; color: #1f2937; }
.chip__subtitle { font-size: 0.75rem; color: #6b7280; margin-top: -2px; }

/* ---- Typeahead simples ---- */
.search-select { position: relative; }
.search-input {
  width: 100%;
  padding: 0.625rem 0.75rem;
  font-size: 0.875rem;
  border: 1px solid #cbd5e1;
  border-radius: 0.5rem;
  background: #ffffff;
  color: #0f172a;
  min-height: 44px;
  outline: none;
}
.search-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59,130,246,.3);
}

.search-list {
  position: absolute;
  z-index: 30;
  top: calc(100% + 6px);
  left: 0;
  right: 0;
  max-height: 260px;
  overflow: auto;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);
  padding: 6px;
}

.search-item {
  display: grid;
  gap: 2px;
  padding: 8px 10px;
  border-radius: 8px;
  cursor: pointer;
}
.search-item:hover {
  background: #f1f5f9;
}
.item-title { font-size: 0.875rem; color: #0f172a; font-weight: 600; }
.item-sub   { font-size: 0.75rem;  color: #64748b; }

/* Lista de perguntas */
.space-y-4 > * + * {
  margin-top: 1rem;
}
</style>