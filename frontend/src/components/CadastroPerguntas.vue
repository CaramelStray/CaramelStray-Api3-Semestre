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
            <p class="text-xs text-slate-500">Para {{ funcionario?.nomeCompleto || 'Colaborador' }}</p>
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

      <template v-else-if="funcionario">
        <!-- Card do Colaborador -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6 mb-6">
          <div class="flex items-start gap-4">
            <div class="h-12 w-12 rounded-full bg-blue-100 text-blue-600 grid place-items-center text-lg font-semibold shrink-0">
              {{ getInitials(funcionario.nomeCompleto) }}
            </div>
            <div class="flex-1">
              <h2 class="text-lg font-semibold text-slate-800">{{ funcionario.nomeCompleto }}</h2>
              <p class="text-sm text-slate-500">{{ funcionario.tituloProfissional }}</p>
              
              <div class="mt-4">
                <p class="text-sm font-medium text-slate-700 mb-2">Competências do colaborador:</p>
                <div class="flex flex-wrap gap-2">
                  <button
                    v-for="comp in funcionario.competencias"
                    :key="comp.codigo"
                    @click="competenciaSelecionada = comp.codigo"
                    :class="[
                      'px-3 py-1 rounded-md text-xs font-medium border transition-colors',
                      competenciaSelecionada === comp.codigo
                        ? 'bg-blue-50 text-blue-700 border-blue-300'
                        : 'bg-white text-slate-600 border-slate-300 hover:border-blue-300 hover:bg-blue-50'
                    ]"
                  >
                    {{ comp.nome }}
                  </button>
                </div>
                <p class="text-xs text-slate-500 mt-2">Clique em uma competência para adicionar a pergunta</p>
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

            <!-- Competências (typeahead) -->
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

          <p class="text-sm font-medium text-slate-700 mb-1">Competências selecionadas:</p>
          <div class="flex flex-wrap gap-2">
            <span
              v-for="comp in filtros.competencias"
              :key="comp.codigo"
              class="inline-block px-3 py-1 rounded-full text-xs font-medium border border-blue-300 bg-blue-50 text-blue-700"
            >
              {{ comp.nome }}
            </span>

            <p v-if="filtros.competencias.length === 0" class="text-sm text-slate-500">
              Nenhuma competência selecionada
            </p>
          </div>
        </div>

        <!-- Card Nova Pergunta -->
        <div class="rounded-xl bg-white shadow-sm border border-slate-200 p-6">
          <h3 class="text-base font-semibold text-slate-800 mb-4">Nova Pergunta</h3>

          <!-- Competência Selecionada (sempre visível) -->
          <div class="mb-4">
            <label class="text-sm font-medium text-slate-700 mb-2 block">Competência Selecionada</label>
            <div v-if="competenciaSelecionada" style="padding: 0.625rem 1rem; border-radius: 0.5rem; border: 1px solid #93c5fd; background-color: #eff6ff;">
              <span class="text-sm font-medium text-blue-700">
                {{ funcionario.competencias.find(c => c.codigo === competenciaSelecionada)?.nome }}
              </span>
            </div>
            <div v-else style="padding: 0.625rem 1rem; border-radius: 0.5rem; border: 1px solid #cbd5e1; background-color: #f8fafc;">
              <span class="text-sm text-slate-500">Selecione uma competência acima</span>
            </div>
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

          <!-- Opções de Resposta -->
          <div>
            <label class="text-sm font-medium text-slate-700 mb-2 block">
              Opções de Resposta <span class="text-slate-500 font-normal">(Opcional)</span>
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

const route = useRoute();
const router = useRouter();

const props = defineProps({ 
  id: String 
});

const funcionario = ref(null);
const loading = ref(true);
const error = ref(null);
const pergunta = ref('');
const opcoes = ref(['']);
const competenciaSelecionada = ref('');

// ---- estados dos filtros (mock simples) ----
const filtros = ref({
  colaboradores: [],   // [{id, nome, cargo}]
  competencias: [],    // [{codigo, nome}]
  equipes: []          // [{id, nome}]
});

// listinhas exemplo; troque quando integrar sua API
const colaboradoresOptions = ref([
  { id: '1', nome: 'Ana Silva Santos', cargo: 'Analista de Recursos Humanos' },
  { id: '2', nome: 'Carlos Eduardo Lima', cargo: 'Desenvolvedor Frontend' },
  { id: '3', nome: 'Mariana Costa Ferreira', cargo: 'Diretora de Marketing' },
  { id: '4', nome: 'João Pedro Oliveira', cargo: 'Analista Financeiro' },
  { id: '5', nome: 'Fernanda Ribeiro Cruz', cargo: 'Designer UX/UI' },
  { id: '6', nome: 'Ricardo Almeida Souza', cargo: 'Supervisor de Vendas' },
  { id: '7', nome: 'Patrícia Martins Silva', cargo: 'Analista de Qualidade' },
  { id: '8', nome: 'Lucas Fernando Santos', cargo: 'Supervisor de Backend' }
]);

const equipesOptions = ref([
  { id: 'e1', nome: 'Marketing' },
  { id: 'e2', nome: 'Financeiro' },
  { id: 'e3', nome: 'Tecnologia' },
  { id: 'e4', nome: 'Qualidade' }
]);

// ---------- BUSCA DO FUNCIONÁRIO ----------
const fetchFuncionarioData = async () => {
  loading.value = true;
  error.value = null;
  try {
    const funcionarioId = props.id || route.params.id;
    const response = await axios.get(`http://localhost:8080/api/funcionario/${funcionarioId}`);
    funcionario.value = response.data;
  } catch (err) {
    console.error("Falha ao buscar dados do funcionário:", err);
    error.value = "Não foi possível carregar as informações. Tente novamente mais tarde.";
  } finally {
    loading.value = false;
  }
};

onMounted(fetchFuncionarioData);

// ---------- UTIL ---------- 
const getInitials = (fullName) => {
  if (!fullName) return '';
  const names = fullName.trim().split(/\s+/);
  const initials = names.length > 1
    ? `${names[0][0]}${names[names.length - 1][0]}`
    : names[0].substring(0, 2);
  return initials.toUpperCase();
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

const handleSalvarPergunta = async () => {
  if (!pergunta.value.trim()) {
    alert('Por favor, digite a pergunta.');
    return;
  }

  if (!competenciaSelecionada.value) {
    alert('Por favor, selecione uma competência.');
    return;
  }

  const opcoesValidas = opcoes.value.filter(op => op.trim() !== '');

  const payload = {
    pergunta: pergunta.value.trim(),
    competenciaId: competenciaSelecionada.value,
    opcoes: opcoesValidas.length > 0 ? opcoesValidas : null
  };

  try {
    const funcionarioId = props.id || route.params.id;
    await axios.post(`http://localhost:8080/api/funcionario/${funcionarioId}/perguntas`, payload);
    
    alert('Pergunta cadastrada com sucesso!');
    
    // Limpa o formulário
    pergunta.value = '';
    opcoes.value = [''];
    competenciaSelecionada.value = '';
  } catch (err) {
    console.error("Falha ao salvar pergunta:", err);
    alert("Ocorreu um erro ao salvar a pergunta.");
  }
};

// ================== TYPEAHEAD (NOVO) ==================
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

const jaTemColab = (id) => filtros.value.colaboradores.some((x) => x.id === id);
const jaTemComp = (codigo) => filtros.value.competencias.some((x) => x.codigo === codigo);
const jaTemEquipe = (id) => filtros.value.equipes.some((x) => x.id === id);

const filteredColabs = computed(() => {
  const q = normaliza(qColab.value);
  return colaboradoresOptions.value
    .filter((c) => !jaTemColab(c.id))
    .filter((c) => !q || normaliza(c.nome).includes(q) || normaliza(c.cargo).includes(q))
    .slice(0, 8);
});

const filteredComps = computed(() => {
  const lista = funcionario.value?.competencias || [];
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

const addColab = (c) => {
  if (!jaTemColab(c.id)) filtros.value.colaboradores.push(c);
  qColab.value = '';
};
const addComp = (comp) => {
  if (!jaTemComp(comp.codigo)) filtros.value.competencias.push(comp);
  qComp.value = '';
};
const addEquipe = (e) => {
  if (!jaTemEquipe(e.id)) filtros.value.equipes.push(e);
  qEquipe.value = '';
};

const handleClickOutside = (ev) => {
  const t = ev.target;
  if (wrapColab.value && !wrapColab.value.contains(t)) open.value.colab = false;
  if (wrapComp.value && !wrapComp.value.contains(t)) open.value.comp = false;
  if (wrapEquipe.value && !wrapEquipe.value.contains(t)) open.value.equipe = false;
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});
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
.mt-2 { margin-top: 0.5rem; }
.mt-3 { margin-top: 0.75rem; }
.mt-4 { margin-top: 1rem; }
.mt-6 { margin-top: 1.5rem; }
.mb-1 { margin-bottom: 0.25rem; }
.mb-2 { margin-bottom: 0.5rem; }
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
.h-4 { height: 1rem; }
.h-5 { height: 1.25rem; }
.h-8 { height: 2rem; }
.h-9 { height: 2.25rem; }
.h-12 { height: 3rem; }
.w-4 { width: 1rem; }
.w-5 { width: 1.25rem; }
.w-8 { width: 2rem; }
.w-9 { width: 2.25rem; }
.w-12 { width: 3rem; }
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
.shrink-0 { flex-shrink: 0; }
.resize-none { resize: none; }
.text-center { text-align: center; }
.transition-colors { transition-property: color, background-color, border-color; transition-duration: 150ms; }
.space-y-2 > * + * { margin-top: 0.5rem; }

/* Hover states */
.hover\:bg-slate-100:hover { background: #f1f5f9; }
.hover\:bg-blue-50:hover { background: #eff6ff; }
.hover\:bg-blue-700:hover { background: #1d4ed8; }
.hover\:bg-red-100:hover { background: #fee2e2; }
.hover\:text-blue-700:hover { color: #1d4ed8; }
.hover\:border-blue-300:hover { border-color: #93c5fd; }

/* Responsive */
@media (min-width: 768px) {
  .md\:p-6 { padding: 1.5rem; }
}
@media (min-width: 1024px) {
  .lg\:p-8 { padding: 2rem; }
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

/* ---- Typeahead simples (NOVO) ---- */
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
</style>
