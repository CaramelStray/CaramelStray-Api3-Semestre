<template>
  <div class="min-h-screen bg-slate-50 text-slate-800">

    <main class="mx-auto max-w-4xl p-6">
      <div v-if="loading" class="text-center py-10 text-slate-600">
        <p>Carregando informações do colaborador...</p>
      </div>

      <div v-else-if="error" class="text-center py-10 text-red-600 bg-red-50 rounded-lg p-4">
        <p class="font-semibold">Ocorreu um erro</p>
        <p class="text-sm">{{ error }}</p>
      </div>

      <template v-else-if="usuarioLogado">
        <!-- TÍTULO DA AVALIAÇÃO -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6">
          <label for="tituloAvaliacao" class="block mb-2 text-base font-semibold text-slate-800">
            Título da Avaliação <span class="text-red-500">*</span>
          </label>
          <input
            type="text"
            id="tituloAvaliacao"
            v-model="tituloAvaliacao"
            placeholder="Ex: Avaliação de Desempenho - Q3 2025"
            class="w-full px-3 py-2 rounded-lg border border-slate-300 bg-white text-slate-800 focus:border-blue-500 focus:ring focus:ring-blue-200 focus:ring-opacity-50"
          />
        </div>

        <!-- ✅ DATA LIMITE (MOVIDO PARA ESTE CARD) -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6">
          <label class="text-sm font-medium text-slate-700 mb-2 block">
            Data limite para resposta <span class="text-red-500">*</span>
          </label>
          <input
            type="date"
            v-model="dataLimite"
            class="w-full px-3 py-2 rounded-lg border border-slate-300 bg-white text-slate-800 focus:border-blue-500 focus:ring focus:ring-blue-200 focus:ring-opacity-50"
          />
          <p class="text-xs text-slate-500 mt-1">
            Defina até quando os colaboradores poderão responder esta avaliação.
          </p>
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
                    :key="comp.id"
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
            <div
              v-for="c in filtros.colaboradores"
              :key="c.id"
              class="flex items-center gap-3 px-4 py-2 rounded-lg border border-slate-200 bg-slate-50 shadow-sm"
            >
              <span class="flex items-center justify-center w-8 h-8 rounded-full bg-blue-100 text-blue-600 font-bold text-sm">
                {{ getInitials(c.nome) }}
              </span>

              <div class="flex flex-col leading-tight">
                <span class="font-medium text-slate-800 text-sm">{{ c.nome }}</span>
                <span class="text-slate-500 text-xs">{{ c.cargo }}</span>
              </div>

              <button
                @click="removerColaborador(c)"
                class="text-slate-400 hover:text-red-500 ml-2 transition"
                title="Remover colaborador"
              >
                ✕
              </button>
            </div>

            <p v-if="filtros.colaboradores.length === 0" class="text-sm text-slate-500">
              Nenhum colaborador selecionado
            </p>
          </div>

          <p class="text-sm font-medium text-slate-700 mb-1">Competências selecionadas (filtro):</p>
          <div class="flex flex-wrap gap-2">
            <span
              v-for="comp in filtros.competencias"
              :key="comp.id"
              class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-blue-300 bg-blue-50 text-blue-700"
            >
              {{ comp.nome }}
              <button
                @click="removerCompetencia(comp)"
                class="text-blue-600 hover:text-blue-800 font-bold ml-1"
                title="Remover competência"
              >
                ✕
              </button>
            </span>

            <p v-if="filtros.competencias.length === 0" class="text-sm text-slate-500">
              Nenhuma competência adicionada ao filtro
            </p>
          </div>
        </div>

        <!-- Card Nova Pergunta -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6">
          <h3 class="text-base font-semibold text-slate-800 mb-4">Nova Pergunta</h3>

          <!-- Competência Vinculada -->
          <div class="mb-4">
            <label class="text-sm font-medium text-slate-700 mb-2 block">
              Competência Vinculada <span class="text-red-500">*</span>
            </label>
            <select
              v-model="competenciaSelecionada"
              class="w-full px-3 py-2 rounded-lg border border-slate-300 bg-white text-slate-800 appearance-none cursor-pointer"
              style="background-image: url('data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 24 24%22 fill=%22none%22 stroke=%22%23334155%22 stroke-width=%222%22%3E%3Cpolyline points=%226 9 12 15 18 9%22%3E%3C/polyline%3E%3C/svg%3E'); background-repeat: no-repeat; background-position: right 0.75rem center; background-size: 1.25rem; padding-right: 2.5rem;"
              aria-label="Selecionar competência"
            >
              <option disabled value="">-- Selecione a Competência --</option>
              <option v-for="comp in competenciasParaDropdown" :key="comp.id" :value="comp.id">
                {{ comp.nome }}
              </option>
            </select>
            <p class="text-xs text-slate-500 mt-1">
              {{ filtros.competencias.length > 0 ? 'Apenas competências selecionadas no filtro são mostradas.' : 'Esta pergunta será associada a esta competência.' }}
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
              <option value="texto">Texto</option>
              <option value="multipla escolha">Múltipla Escolha</option>
              <option value="escala/nota">Escala/Nota</option>
            </select>
            <p class="text-xs text-slate-500 mt-1">
              Escolha como o colaborador irá responder.
            </p>
          </div>

          <!-- Pergunta -->
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

          <!-- Opções de Resposta -->
          <div v-if="tipoPergunta === 'multipla escolha'">
            <label class="text-sm font-medium text-slate-700 mb-2 block">
              Opções de Resposta <span class="text-slate-500 font-normal">(Selecione a correta)</span>
            </label>

            <div class="space-y-2">
              <div
                v-for="(opcao, index) in opcoes"
                :key="index"
                class="flex items-center gap-2"
              >
                <input
                  type="radio"
                  name="opcaoCorretaRadio"
                  :value="index"
                  v-model.number="opcaoCorretaIndex"
                  class="h-5 w-5 text-blue-600 focus:ring-blue-500 border-slate-300"
                  :aria-label="`Marcar opção ${index + 1} como correta`"
                />
                <input
                  type="text"
                  v-model="opcoes[index].texto"
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

          <!-- Perguntas Cadastradas -->
          <div v-if="perguntas.length > 0" class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6 mt-6">
            <h3 class="text-base font-semibold text-slate-800 mb-4">
              Perguntas Cadastradas ({{ perguntas.length }})
            </h3>

            <div class="space-y-4">
              <div
                v-for="(p, index) in perguntas"
                :key="index"
                class="border border-slate-200 rounded-lg p-4 bg-slate-50"
              >
                <div class="flex items-center gap-2 mb-2 flex-wrap">
                  <span class="inline-block px-3 py-1 rounded-full text-xs font-medium border border-blue-300 bg-blue-50 text-blue-700">
                    {{ obterNomeCompetencia(p.competenciaId) }}
                  </span>
                  <span v-if="p.tipoPergunta === 'multipla escolha'" class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-slate-300 bg-white text-slate-700">
                    Múltipla Escolha
                  </span>
                  <span v-else-if="p.tipoPergunta === 'escala/nota'" class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-slate-300 bg-white text-slate-700">
                    Escala/Nota
                  </span>
                  <span v-else class="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border border-slate-300 bg-white text-slate-700">
                    Texto
                  </span>
                  <span class="ml-auto text-xs text-slate-500">Pergunta {{ index + 1 }}</span>
                </div>

                <p class="text-sm font-medium text-slate-800 mb-3">{{ p.pergunta }}</p>

                <div v-if="p.tipoPergunta === 'multipla escolha' && p.opcoes && p.opcoes.length">
                  <p class="text-xs font-medium text-slate-600 mb-2">Opções:</p>
                  <ul class="list-disc list-inside space-y-1">
                    <li v-for="(opcao, idx) in p.opcoes" :key="idx" class="text-sm text-slate-700">
                      {{ opcao.descricao }}
                      <span v-if="opcao.isCorreta" class="text-xs text-emerald-600 font-semibold"> (Correta)</span>
                    </li>
                  </ul>
                </div>

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

          <!-- Botão Criar Avaliação -->
          <div class="mt-8 pt-6 border-t border-slate-200">
            <button
              @click="handleCriarAvaliacao"
              :class="[
                'w-full rounded-lg px-4 py-3 text-base font-semibold text-white transition-colors disabled:opacity-50',
                perguntas.length > 0 &&
                filtros.colaboradores.length > 0 &&
                tituloAvaliacao.trim() &&
                dataLimite
                  ? 'bg-blue-600 hover:bg-blue-700'
                  : 'bg-gray-300 text-gray-600 cursor-not-allowed'
              ]"
              :disabled="
                perguntas.length === 0 ||
                filtros.colaboradores.length === 0 ||
                !tituloAvaliacao.trim() ||
                !dataLimite
              "
            >
              Criar Avaliação para {{ filtros.colaboradores.length }} Colaborador(es)
            </button>
            <p
              v-if="
                perguntas.length === 0 ||
                filtros.colaboradores.length === 0 ||
                !tituloAvaliacao.trim() ||
                !dataLimite
              "
              class="text-xs text-center text-red-600 mt-2"
            >
              Preencha o título, adicione perguntas, selecione colaboradores e defina a data limite.
            </p>
          </div>
        </div>
      </template>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { useAuth } from '../auth';

const route = useRoute();
const router = useRouter();

const { usuarioLogado, carregarUsuarioLogado } = useAuth();
const loading = ref(true);
const error = ref(null);
const pergunta = ref('');
const tipoPergunta = ref('texto');
const opcoes = ref([{ texto: '', isCorreta: false }]);
const competenciaSelecionada = ref('');
const perguntas = ref([]);
const allColaboradores = ref([]);
const allCompetencias = ref([]);
const allAreas = ref([]);
const tituloAvaliacao = ref('');
const opcaoCorretaIndex = ref(0);
const dataLimite = ref(''); // campo de data limite

const filtros = ref({
  colaboradores: [],
  competencias: [],
  equipes: []
});

const removerColaborador = (colab) => {
  filtros.value.colaboradores = filtros.value.colaboradores.filter(c => c.codigo !== colab.codigo);
};

const removerCompetencia = (comp) => {
  filtros.value.competencias = filtros.value.competencias.filter(c => c.id !== comp.id);
};

onMounted(async () => {
  loading.value = true;
  error.value = null;
  try {
    if (!usuarioLogado.value) {
      console.warn("Usuário logado não encontrado no estado global, tentando carregar...");
      await carregarUsuarioLogado();
      if (!usuarioLogado.value) {
        throw new Error("Falha ao obter dados do usuário logado. O estado global está vazio.");
      }
    } else {
      console.log("Usuário logado encontrado no estado global:", usuarioLogado.value.nomeCompleto);
    }

    await fetchFilterData();
  } catch (err) {
    console.error("Falha ao carregar dados iniciais:", err);
    error.value = "Não foi possível carregar os dados. " + (err.message || '');
  } finally {
    loading.value = false;
  }

  document.addEventListener('click', handleClickOutside);
});

const fetchFilterData = async () => {
  try {
    const [colabRes, compRes, areaRes] = await Promise.all([
      axios.get('http://localhost:8080/api/funcionario'),
      axios.get('http://localhost:8080/api/competencia'),
      axios.get('http://localhost:8080/api/area')
    ]);

    allColaboradores.value = colabRes.data.map(c => ({
      codigo: c.codigo,
      nome: c.nomeCompleto,
      cargo: c.tituloProfissional || 'Cargo não informado',
      areaId: c.areaId,
      perfilId: c.perfilId
    }));

    allCompetencias.value = compRes.data;
    allAreas.value = areaRes.data;
  } catch (err) {
    console.error("Falha ao buscar dados dos filtros:", err);
    throw new Error("Falha ao carregar listas de filtros. Verifique os endpoints da API.");
  }
};

// UTIL
const getInitials = (fullName) => {
  if (!fullName) return '';
  const names = fullName.trim().split(/\s+/);
  const initials = names.length > 1
    ? `${names[0][0]}${names[names.length - 1][0]}`
    : names[0].substring(0, 2);
  return initials.toUpperCase();
};

const obterNomeCompetencia = (id) => {
  const found = allCompetencias.value.find(c => c.id === id);
  return found?.nome || `Competência #${id}`;
};

const handleVoltar = () => {
  router.back();
};

const handleAdicionarOpcao = () => {
  opcoes.value.push({ texto: '', isCorreta: false });
};

const handleRemoverOpcao = (index) => {
  if (opcoes.value.length > 1) {
    opcoes.value.splice(index, 1);
    if (opcaoCorretaIndex.value === index) {
      opcaoCorretaIndex.value = 0;
    } else if (opcaoCorretaIndex.value > index) {
      opcaoCorretaIndex.value--;
    }
  }
};

const handleSalvarPergunta = () => {
  if (!pergunta.value.trim()) {
    alert('Por favor, digite a pergunta.');
    return;
  }
  if (!competenciaSelecionada.value) {
    alert('Por favor, selecione uma competência.');
    return;
  }

  const opcoesValidas = opcoes.value
    .map((op, index) => {
      const descricaoTrimmed = (op && typeof op.texto === 'string') ? op.texto.trim() : '';
      return {
        descricao: descricaoTrimmed,
        isCorreta: index === opcaoCorretaIndex.value
      };
    })
    .filter(op => op.descricao !== '');

  if (tipoPergunta.value === 'multipla escolha' && opcoesValidas.length < 2) {
    alert('Para múltipla escolha, informe ao menos 2 opções válidas.');
    return;
  }

  const novaPerguntaLocal = {
    pergunta: pergunta.value.trim(),
    competenciaId: competenciaSelecionada.value,
    tipoPergunta: tipoPergunta.value,
    opcoes: (tipoPergunta.value === 'multipla escolha') ? opcoesValidas : null,
  };

  perguntas.value.push(novaPerguntaLocal);

  pergunta.value = '';
  tipoPergunta.value = 'texto';
  opcoes.value = [{ texto: '', isCorreta: false }];
  opcaoCorretaIndex.value = 0;
};

const handleDeletarPergunta = (index) => {
  if (confirm('Tem certeza que deseja deletar esta pergunta?')) {
    perguntas.value.splice(index, 1);
  }
};

// TYPEAHEAD
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
const jaTemComp = (id) => filtros.value.competencias.some((x) => x.id === id);
const jaTemEquipe = (codigo) => filtros.value.equipes.some((x) => x.codigo === codigo);

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
      if (colab.codigo === userLogado.codigo) return false;
      const podeAvaliarNivel = userPerfilId < colab.perfilId;
      const mesmaArea = colab.areaId === userAreaId;
      return podeAvaliarNivel && mesmaArea;
    })
    .filter((colab) => !q || normaliza(colab.nome).includes(q) || normaliza(colab.cargo).includes(q))
    .slice(0, 30);
});

const filteredComps = computed(() => {
  const q = normaliza(qComp.value);
  const result = allCompetencias.value
    .filter((c) => !jaTemComp(c.id))
    .filter((c) => !q || normaliza(c.nome).includes(q))
    .slice(0, 30);
  return result;
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
  open.value.colab = false;
};

const addComp = (comp) => {
  if (!jaTemComp(comp.id)) {
    filtros.value.competencias.push(comp);
  } else {
    nextTick(() => {
      open.value.comp = false;
    });
  }
  qComp.value = '';
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

const competenciasParaDropdown = computed(() => {
  if (filtros.value.competencias.length > 0) {
    return filtros.value.competencias;
  }
  return allCompetencias.value;
});

watch(() => filtros.value.competencias, (novasCompetenciasSelecionadas) => {
  if (novasCompetenciasSelecionadas.length === 1) {
    const compCodigo = novasCompetenciasSelecionadas[0]?.id;
    if (compCodigo !== undefined) {
      competenciaSelecionada.value = compCodigo;
    } else {
      competenciaSelecionada.value = '';
    }
  } else {
    const selecionadaAindaValida = novasCompetenciasSelecionadas.length === 0 ||
      novasCompetenciasSelecionadas.some(c => c.id === competenciaSelecionada.value);
    if (!selecionadaAindaValida) {
      competenciaSelecionada.value = '';
    }
  }
}, { deep: true });

const handleCriarAvaliacao = async () => {
  if (!tituloAvaliacao.value.trim()) {
    alert('Por favor, informe o título da avaliação.');
    return;
  }
  if (perguntas.value.length === 0) {
    alert('Adicione pelo menos uma pergunta à avaliação antes de criar.');
    return;
  }
  if (filtros.value.colaboradores.length === 0) {
    alert('Selecione pelo menos um colaborador para receber a avaliação.');
    return;
  }
  if (!dataLimite.value) {
    alert('Defina uma data limite para a avaliação.');
    return;
  }

  let codigosPerguntasSalvas = [];

  try {
    const savePromises = perguntas.value.map(perguntaLocal => {
      const payloadPergunta = {
        pergunta: perguntaLocal.pergunta,
        competenciaCodigo: perguntaLocal.competenciaId,
        tipoPergunta: perguntaLocal.tipoPergunta,
        opcoes: perguntaLocal.opcoes
      };
      return axios.post('http://localhost:8080/api/perguntas', payloadPergunta);
    });

    const responses = await Promise.all(savePromises);

    codigosPerguntasSalvas = responses.map(response => response.data.codigo);

    if (codigosPerguntasSalvas.length !== perguntas.value.length || codigosPerguntasSalvas.some(code => code == null)) {
      throw new Error("Falha ao salvar uma ou mais perguntas no backend.");
    }

    const codigosFuncionarios = filtros.value.colaboradores.map(c => c.codigo);

    const payloadAvaliacao = {
      titulo: tituloAvaliacao.value.trim(),
      codigosFuncionarios,
      codigosPerguntas: codigosPerguntasSalvas,
      dataLimite: dataLimite.value
    };

    const responseAvaliacao = await axios.post('http://localhost:8080/api/avaliacoes', payloadAvaliacao);

    alert(`Avaliação "${responseAvaliacao.data.titulo}" criada com sucesso!`);

    tituloAvaliacao.value = '';
    perguntas.value = [];
    filtros.value.colaboradores = [];
    filtros.value.competencias = [];
    filtros.value.equipes = [];
    dataLimite.value = '';
  } catch (err) {
    console.error("Erro completo durante a criação da avaliação:", err);
    let mensagem = "Ocorreu um erro geral ao criar a avaliação.";

    if (err.message && err.message.includes("Falha ao salvar uma ou mais perguntas")) {
      mensagem = err.message + " Verifique o console do backend para detalhes.";
    } else if (axios.isAxiosError(err) && err.response) {
      if (err.config.url.includes('/api/perguntas')) {
        mensagem = `Erro ao salvar uma das perguntas (${err.response.status}): ${err.response.data?.message || err.response.data || 'Erro desconhecido'}`;
      } else if (err.config.url.includes('/api/avaliacoes')) {
        mensagem = `Erro ao criar a avaliação final (${err.response.status}): ${err.response.data?.message || err.response.data || 'Funcionário(s) ou Pergunta(s) inválido(s)'}`;
      } else {
        mensagem = `Erro de rede ou servidor (${err.response.status}).`;
      }
    } else if (err.code === 'ERR_NETWORK') {
      mensagem = "Não foi possível conectar ao servidor (localhost:8080).";
    } else if (err.message) {
      mensagem = err.message;
    }
    alert(mensagem);
  }
};

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
/* (mesmos estilos que você já tinha, mantive tudo) */

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
.mt-8 { margin-top: 2rem; }
.mb-1 { margin-bottom: 0.25rem; }
.mb-2 { margin-bottom: 0.5rem; }
.mb-3 { margin-bottom: 0.75rem; }
.mb-4 { margin-bottom: 1rem; }
.mb-6 { margin-bottom: 1.5rem; }
.ml-auto { margin-left: auto; }

/* Gap */
.gap-1 { gap: 0.25rem; }
.gap-2 { gap: 0.5rem; }
.gap-3 { gap: 0.75rem; }
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

/* Miscelânea */
.block { display: block; }
.inline-block { display: inline-block; }
.inline-flex { display: inline-flex; }
.shrink-0 { flex-shrink: 0; }
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

/* Responsivo */
@media (min-width: 768px) {
  .md\:grid-cols-3 {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

/* Typeahead */
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
.search-item:hover { background: #f1f5f9; }
.item-title { font-size: 0.875rem; color: #0f172a; font-weight: 600; }
.item-sub   { font-size: 0.75rem;  color: #64748b; }
</style>
