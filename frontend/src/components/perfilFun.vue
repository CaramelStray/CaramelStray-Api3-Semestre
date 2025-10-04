<template>
  <div class="min-h-screen bg-slate-50 text-slate-800">
    <header class="bg-white border-b border-slate-200">
      <div class="mx-auto max-w-7xl px-4 py-4 flex items-center gap-3">
        <button class="h-9 w-9 grid place-items-center rounded-lg border border-slate-200 hover:bg-slate-100" aria-label="Voltar">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5"><path fill-rule="evenodd" d="M10.03 4.97a.75.75 0 0 1 0 1.06L5.06 11l4.97 4.97a.75.75 0 1 1-1.06 1.06l-5.5-5.5a.75.75 0 0 1 0-1.06l5.5-5.5a.75.75 0 0 1 1.06 0Zm4.94 0a.75.75 0 0 1 1.06 0l5.5 5.5a.75.75 0 0 1 0 1.06l-5.5 5.5a.75.75 0 1 1-1.06-1.06L18.94 11l-4.97-4.97a.75.75 0 0 1 1.06-1.06Z" clip-rule="evenodd"/></svg>
        </button>
        <div>
          <h1 class="text-lg font-semibold">Perfil do Colaborador</h1>
          <p class="text-sm text-slate-500 leading-tight">Informações detalhadas</p>
        </div>
        <div class="ml-auto flex items-center gap-2">
          </div>
      </div>
    </header>

    <main class="mx-auto max-w-7xl p-4 md:p-6 lg:p-8">

      <div v-if="loading" class="text-center py-10 text-slate-600">
        <p>Carregando informações do colaborador...</p>
      </div>

      <div v-else-if="error" class="text-center py-10 text-red-600 bg-red-50 rounded-lg p-4">
        <p class="font-semibold">Ocorreu um erro</p>
        <p class="text-sm">{{ error }}</p>
      </div>

      <template v-else-if="funcionario">
        <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
          <div class="flex flex-col md:flex-row md:items-start gap-6">
            <div class="relative shrink-0">
                <div class="h-28 w-28 rounded-full bg-teal-600 text-white grid place-items-center text-3xl font-semibold select-none">
                    {{ getInitials(funcionario.nomeCompleto) }}
                </div>
                <span class="absolute -bottom-1 left-1/2 -translate-x-1/2 inline-flex items-center gap-2 rounded-full bg-emerald-50 px-3 py-1 text-xs font-medium text-emerald-700 border border-emerald-200">
                    <span class="h-2.5 w-2.5 rounded-full bg-emerald-500"></span>
                    Ativo
                </span>
            </div>

            <div class="flex-1">
              <div class="gap-1">
                <h2 class="text-2xl font-semibold">{{ funcionario.nomeCompleto }}</h2>
                <p class="text-slate-500">{{ funcionario.tituloProfissional }}</p>
              </div>
              <div class="mt-6">
                <div class="flex items-center justify-between">
                  <div class="flex items-center gap-2">
                    <h3 class="text-base font-semibold">Resumo Profissional</h3>

                    <button v-if="podeEditarPerfil && !isEditingResumo" @click="iniciarEdicaoResumo" class="h-6 w-6 grid place-items-center rounded-full text-slate-400 hover:bg-slate-100 hover:text-teal-600" aria-label="Editar Resumo">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/><path d="m15 5 4 4"/></svg>
                    </button>
                  </div>
                  <div v-if="podeEditarPerfil && isEditingResumo" class="flex items-center gap-2">
                    <button @click="cancelarEdicaoResumo" class="h-8 w-8 grid place-items-center rounded-full text-slate-500 hover:bg-slate-100 hover:text-red-600" aria-label="Cancelar Edição">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                    </button>
                    <button @click="salvarResumo" class="h-8 w-8 grid place-items-center rounded-full text-slate-500 hover:bg-slate-100 hover:text-emerald-600" aria-label="Salvar Resumo">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    </button>
                  </div>
                </div>
 
                <p v-if="!isEditingResumo" class="mt-2 text-sm leading-relaxed text-slate-700 max-w-3xl">
                  {{ funcionario.resumo || 'Nenhum resumo profissional cadastrado. Clique no lápis para adicionar um.' }}
                </p>
                <textarea 
                v-else 
                v-model="resumoEmEdicao" 
                rows="4"
                  class="mt-2 w-full max-w-3xl rounded-md bg-transparent border-2 border-slate-800 focus:ring-0 focus:border-teal-600 sm:text-sm text-slate-800 resize-none" 
                    placeholder="Escreva um breve resumo sobre sua carreira..."
                ></textarea>
              </div>

              <dl class="mt-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                <div class="flex items-start gap-3">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-5 w-5 text-teal-600 shrink-0">
                        <path d="M12 10h.01"/><path d="M12 14h.01"/><path d="M12 6h.01"/><path d="M16 10h.01"/><path d="M16 14h.01"/><path d="M16 6h.01"/><path d="M8 10h.01"/><path d="M8 14h.01"/><path d="M8 6h.01"/><path d="M9 22v-3a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v3"/><rect x="4" y="2" width="16" height="20" rx="2"/>
                    </svg>
                    <div>
                        <dt class="text-xs uppercase tracking-wide text-slate-500">Departamento</dt>
                        <dd class="text-sm font-medium">{{ funcionario.nomeArea }}</dd>
                    </div>
                </div>

                <div class="flex items-start gap-3">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-5 w-5 text-teal-600 shrink-0 lucide lucide-map-pin">
                        <path d="M12 13.5a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/><path d="M12 2a9 9 0 0 0-9 9c0 6 9 13 9 13s9-7 9-13a9 9 0 0 0-9-9Z"/>
                    </svg>
                    <div>
                    <dt class="text-xs uppercase tracking-wide text-slate-500">Localização</dt>
                    <dd class="text-sm font-medium">{{ funcionario.localizacao }}</dd>
                    </div>
                </div>
                
                <div class="flex items-start gap-3">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-5 w-5 text-teal-600 shrink-0 lucide lucide-mail">
                        <path d="m22 7-8.991 5.727a2 2 0 0 1-2.009 0L2 7"/><rect x="2" y="4" width="20" height="16" rx="2"/>
                    </svg>
                    <div>
                        <dt class="text-xs uppercase tracking-wide text-slate-500">Email (não editável)</dt>
                        <dd class="text-sm font-medium">
                            <a class="text-teal-700 hover:underline" :href="'mailto:' + funcionario.email">{{ funcionario.email }}</a>
                        </dd>
                    </div>
                </div>
                
                <div class="flex items-start gap-3">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-5 w-5 text-teal-600 shrink-0 lucide lucide-calendar">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>
                    </svg>
                    <div>
                        <dt class="text-xs uppercase tracking-wide text-slate-500">Data de Admissão</dt>
                        <dd class="text-sm font-medium">{{ formatDate(funcionario.dataCadastro) }}</dd>
                    </div>
                </div>

                <div class="flex items-start gap-3">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-5 w-5 text-teal-600 shrink-0 lucide lucide-phone">
                        <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.63A2 2 0 0 1 3.08 2h3a2 2 0 0 1 2 1.72a12.84 12.84 0 0 0 .7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45a12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92Z"/>
                    </svg>
                    <div>
                        <dt class="text-xs uppercase tracking-wide text-slate-500">Telefone</dt>
                        <dd class="text-sm font-medium">{{ funcionario.telefone }}</dd>
                    </div>
                </div>

              </dl>
            </div>
          </div>
        </section>

        <section class="mt-6 rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold">Histórico Profissional</h3>
            
            <button v-if="podeEditarPerfil" @click="abrirModalParaNovaExperiencia" class="rounded-lg bg-teal-50 px-3 py-1.5 text-sm font-semibold text-teal-700 hover:bg-teal-100">
              ➕ Adicionar Novo
            </button>
          </div>

          <div v-if="funcionario.experiencias && funcionario.experiencias.length" class="space-y-6 mt-4">
            <div v-for="experiencia in funcionario.experiencias" :key="experiencia.codigo" class="relative border-l-4 border-teal-600 pl-4 py-2">
                <div class="flex items-center gap-2">
                    <h4 class="text-lg font-medium">{{ experiencia.cargo }}</h4>
              <button v-if="podeEditarPerfil" @click="abrirModalParaEditarExperiencia(experiencia)" class="absolute top-1 right-1 h-8 w-8 grid place-items-center rounded-full text-slate-400 hover:bg-slate-100 hover:text-teal-600" aria-label="Editar Experiência">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/></svg>
              </button>
                </div>
               
              
              <p class="text-sm text-slate-700">{{ experiencia.empresa }}</p>
              <p class="text-xs text-slate-500 mt-1">
                {{ formatDate(experiencia.dataInicio) }} - {{ experiencia.dataFim ? formatDate(experiencia.dataFim) : 'Atual' }}
              </p>
              <p v-if="experiencia.descricao" class="mt-2 pr-8 text-sm text-slate-600">
                {{ experiencia.descricao }}
              </p>
            </div>
          </div>
          <p v-else class="mt-4 text-sm text-slate-500">Nenhuma experiência profissional cadastrada.</p>
        </section>

        <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-6">
          <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
                  <div class="flex items-center justify-between">
                      
                      <h3 class="text-base font-semibold">Competências</h3>

                      <button v-if="podeEditarCompetencias" @click="navegarParaCompetencias" 
                          class="rounded-md px-2.5 py-1 text-md font-semibold text-teal-700 hover:bg-teal-50">
                          Editar
                      </button>

                  </div>
                  
                  <div v-if="funcionario.competencias && funcionario.competencias.length" class="mt-4 space-y-2">

                      <div v-for="competencia in funcionario.competencias" :key="competencia.codigo" class="flex text-sm">
                          
                          <span class="mr-2">•</span>

                          <span>{{ competencia.nome }}</span>

                      </div>
                  </div>
                  <p v-else class="mt-4 text-sm text-slate-500">Nenhuma competência cadastrada.</p>

              </section>
                    <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
              <h3 class="text-base font-semibold">Certificações</h3>

              <div class="mt-4 space-y-2">
                  <div v-for="certificado in funcionario.certificados" :key="certificado.codigo"
                      class="flex items-center gap-3 rounded-lg border border-slate-200 bg-slate-50 p-2 pr-1 pl-3">
                      
                      <span class="flex-grow text-sm font-medium text-slate-700">{{ certificado.nome }}</span>
                      
                      <button v-if="podeEditarPerfil" @click="removerCertificado(certificado.codigo)"
                          class="h-7 w-7 grid place-items-center rounded-md text-slate-500 hover:bg-slate-200 hover:text-slate-800 shrink-0"
                          aria-label="Remover certificação">
                          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24" fill="none"
                              stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"
                              class="h-4 w-4">
                              <line x1="18" y1="6" x2="6" y2="18" />
                              <line x1="6" y1="6" x2="18" y2="18" />
                          </svg>
                      </button>
                  </div>

                  <div v-if="isAddingCertificado"
                      class="flex items-center gap-3 rounded-lg border border-teal-500 bg-white p-2 pr-1 pl-3 ring-2 ring-teal-200">
                      
                      <input 
                          ref="inputNovoCertificado"
                          v-model="novoCertificadoNome"
                          @keydown.enter.prevent="salvarNovoCertificado"
                          @keydown.esc.prevent="cancelarAdicao"
                          type="text"
                          placeholder="Digite o nome e tecle Enter"
                          class="flex-grow text-sm font-medium text-slate-700 bg-transparent focus:outline-none"
                      />
                      <button @click="cancelarAdicao" class="h-7 w-7 grid place-items-center rounded-md text-slate-500 hover:bg-slate-200 hover:text-slate-800 shrink-0" aria-label="Cancelar adição">
                          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="h-4 w-4">
                              <line x1="18" y1="6" x2="6" y2="18" />
                              <line x1="6" y1="6" x2="18" y2="18" />
                          </svg>
                      </button>
                  </div>
                  
                  <p v-if="!funcionario.certificados || !funcionario.certificados.length" class="text-sm text-slate-500 py-2">
                      Nenhuma certificação cadastrada.
                  </p>
              </div>

              <div class="mt-4 pt-4 border-t border-slate-200" v-if="podeEditarPerfil && !isAddingCertificado">
                      <button @click="iniciarAdicao"
                      class="w-full text-center rounded-lg border-2 border-dashed border-slate-300 px-3 py-2 text-sm font-semibold text-slate-600 hover:border-teal-500 hover:bg-teal-50 hover:text-teal-700">
                      Adicionar Certificação
                  </button>
              </div>
          </section>
          <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
            <h3 class="text-base font-semibold">Equipe</h3>
            <dl class="mt-4 space-y-4">
              <div>
                <dt class="text-xs uppercase tracking-wide text-slate-500">Gestor</dt>
                <dd class="text-sm font-medium">{{ funcionario.nomeGestor || 'Não informado' }}</dd>
              </div>
            </dl>
          </section>
        </div>
      </template>
        <ExperienciaModal 
      :isOpen="isExperienciaModalOpen" 
      :experiencia-para-editar="experienciaSendoEditada"
      @close="fecharExperienciaModal"
      @save="salvarExperiencia"
        />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted,nextTick,computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import {useAuth} from '../auth';
import ExperienciaModal from './ExperienciaModal.vue';

const props = defineProps({ id: String }); // Recebe o ID da URL
const router = useRouter();                // Para podermos navegar para outras páginas
const { usuarioLogado } = useAuth(); 

const funcionario = ref(null); // Inicia como nulo. Só recebe valor após a chamada da API.
const loading = ref(true);     // Inicia como verdadeiro, pois começamos carregando.
const error = ref(null);
const isEditingResumo = ref(false);
const resumoEmEdicao = ref('');
const isExperienciaModalOpen = ref(false);
const experienciaSendoEditada = ref(null); 
const isAddingCertificado = ref(false); // Controla se o campo de input está visível
const novoCertificadoNome = ref('');    // Armazena o texto do novo certificado
const inputNovoCertificado = ref(null);

const fetchFuncionarioData = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await axios.get(`http://localhost:8080/api/funcionario/${props.id}`);
    funcionario.value = response.data;
  } catch (err) {
    console.error("Falha ao buscar dados do funcionário:", err);
    error.value = "Não foi possível carregar as informações. Tente novamente mais tarde.";
  } finally {
    loading.value = false;
  }
};

onMounted(fetchFuncionarioData);

const podeEditarPerfil = computed(() => {
    if (!usuarioLogado.value || !funcionario.value) return false;
    // Retorna true APENAS se o código do usuário logado for igual ao do perfil visto
    return usuarioLogado.value.codigo === funcionario.value.codigo;
});

// Lógica 2: Dono do perfil OU superior da mesma área (Apenas para Competências)
const podeEditarCompetencias = computed(() => {
    if (!usuarioLogado.value || !funcionario.value) return false;
    if (usuarioLogado.value.codigo === funcionario.value.codigo) return true;

    const isSuperior = usuarioLogado.value.perfilId < funcionario.value.perfilId;
    const isMesmaArea = usuarioLogado.value.areaId === funcionario.value.areaId;

    return isSuperior && isMesmaArea;
});

const navegarParaCompetencias = () => {
    router.push({ name: 'Competencias', params: { id: funcionario.value.codigo } });
};

const getInitials = (fullName) => {
  if (!fullName) return '';
  const names = fullName.split(' ');
  const initials = names.length > 1
    ? `${names[0][0]}${names[names.length - 1][0]}`
    : names[0].substring(0, 2);
  return initials.toUpperCase();
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  // Formata a data para o padrão brasileiro (dd/mm/aaaa)
  return new Date(dateString).toLocaleDateString('pt-BR', { timeZone: 'UTC' });
};
const iniciarEdicaoResumo = () => {
  
  resumoEmEdicao.value = funcionario.value.resumo;
  
  isEditingResumo.value = true;
};
const cancelarEdicaoResumo = () => {
  isEditingResumo.value = false;
};
const salvarResumo = async () => {
  
  if (resumoEmEdicao.value === funcionario.value.resumo) {
    cancelarEdicaoResumo(); 
    return;
  }
  const payload = {
    ...funcionario.value, // Usamos os dados atuais como base
    resumo: resumoEmEdicao.value, // Sobrescrevemos com o resumo editado
  };

  try {
    const response = await axios.put(`http://localhost:8080/api/funcionario/${funcionario.value.codigo}`, payload);
    
    funcionario.value = response.data;
    isEditingResumo.value = false;
    alert('Resumo atualizado com sucesso!');

  } catch (err) {
    console.error("Falha ao atualizar resumo:", err);
    alert("Ocorreu um erro ao salvar.");
  }
};

const abrirModalParaNovaExperiencia = () => {
  experienciaSendoEditada.value = null; // Garante que o formulário estará vazio
  isExperienciaModalOpen.value = true;
};

const abrirModalParaEditarExperiencia = (experiencia) => {
  experienciaSendoEditada.value = experiencia; // Passa a experiência clicada para o modal
  isExperienciaModalOpen.value = true;
};

const fecharExperienciaModal = () => {
  isExperienciaModalOpen.value = false;
};

const salvarExperiencia = async (experienciaData) => {
  try {
    let response;
    // Se a experiência tem um 'codigo', significa que estamos EDITANDO (PUT)
    if (experienciaData.codigo) {
      // Usamos o novo endpoint para ATUALIZAR
      response = await axios.put(`http://localhost:8080/api/funcionario/experiencias/${experienciaData.codigo}`, experienciaData);
    } else {
      // Se não tem 'codigo', estamos CRIANDO (POST)
      // Usamos o novo endpoint para CRIAR
      const funcionarioId = funcionario.value.codigo;
      response = await axios.post(`http://localhost:8080/api/funcionario/${funcionarioId}/experiencias`, experienciaData);
    }

    alert('Experiência salva com sucesso!');
    fecharExperienciaModal();

    // ESSENCIAL: Recarrega os dados do funcionário para mostrar a lista atualizada!
    await fetchFuncionarioData();

  } catch (err) {
    console.error("Falha ao salvar experiência:", err);
    alert("Ocorreu um erro ao salvar a experiência.");
  }
};
const removerCertificado = async (certificadoId) => {
  // 1. Pede confirmação antes de apagar
  if (!confirm("Tem certeza que deseja remover esta certificação?")) {
    return;
  }

  try {
    // 2. Faz a chamada DELETE para a API, passando o ID do certificado a ser removido
    await axios.delete(`http://localhost:8080/api/funcionario/certificados/${certificadoId}`);

    alert('Certificação removida com sucesso!');

    // 3. ESSENCIAL: Recarrega os dados do funcionário para atualizar a lista na tela
    await fetchFuncionarioData();

  } catch (err) {
    console.error("Falha ao remover certificação:", err);
    alert("Ocorreu um erro ao remover a certificação.");
  }
};

const iniciarAdicao = async () => {
  isAddingCertificado.value = true;
  // Aguarda o Vue atualizar o DOM para mostrar o input
  await nextTick();
  // Foca no campo de input para o usuário já poder digitar
  inputNovoCertificado.value?.focus();
};
const cancelarAdicao = () => {
  isAddingCertificado.value = false;
  novoCertificadoNome.value = '';
};
const salvarNovoCertificado = async () => {
  const nomeLimpo = novoCertificadoNome.value.trim();
  if (nomeLimpo === '') {
    // Se o campo estiver vazio, apenas cancela a adição
    cancelarAdicao();
    return;
  }

  const payload = { nome: nomeLimpo };

  try {
    const funcionarioId = funcionario.value.codigo;
    // A chamada para a API continua a mesma de antes
    await axios.post(`http://localhost:8080/api/funcionario/${funcionarioId}/certificados`, payload);
    
    // Recarrega os dados para mostrar o novo item na lista
    await fetchFuncionarioData();
    
  } catch (err) {
    console.error("Falha ao adicionar certificação:", err);
    alert("Ocorreu um erro ao salvar a certificação.");
  } finally {
    // Esconde o campo de input após salvar (ou falhar)
    cancelarAdicao();
  }
};


</script>

<!-- Reusable Tag component (local) -->
<script>
export default {
  components: {
    Tag: {
      props: { label: { type: String, required: true } },
      template: `
        <span class="inline-flex items-center rounded-full border border-slate-200 bg-slate-50 px-3 py-1 text-xs font-medium text-slate-700">
          {{ label }}
        </span>
      `
    }
  }
}
</script>

<style scoped>
/* If you don't use Tailwind, these utility-like classes below keep a similar look */
:root {
  --ring: #e2e8f0;
}

.bg-slate-50 { background: #eaf7fb; }
.bg-white { background: #ffffffff; }
.text-slate-800 { color: #0f172a; }
.text-slate-700 { color: #334155; }
.text-slate-600 { color: #475569; }
.text-slate-500 { color: #64748b; }
.text-teal-600 { color: #0d9488; }
.text-teal-700 { color: #0f766e; }
.bg-teal-600 { background: #0d9488; }
.bg-teal-50 { background: #f0fdfa; }
.bg-emerald-50 { background: #ecfdf5; }
.text-emerald-700 { color: #047857; }
.border { border: 1px solid var(--ring); }
.border-slate-200 { border-color: #e2e8f0; }
.ring-1 { box-shadow: 0 0 0 1px var(--ring); }
.ring-slate-200 { box-shadow: 0 0 0 1px #e2e8f0; }
.rounded-lg { border-radius: .5rem; }
.rounded-2xl { border-radius: 1rem; }
.rounded-full { border-radius: 9999px; }
.shadow-sm { box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05); }

/***** tiny utility subset for spacing & layout *****/
.min-h-screen { min-height: 100vh; }
.mx-auto { margin-left: auto; margin-right: auto; }
.p-4 { padding: 1rem; } .p-6 { padding: 1.5rem; } .p-8 { padding: 2rem; }
.py-4 { padding-top: 1rem; padding-bottom: 1rem; }
.px-4 { padding-left: 1rem; padding-right: 1rem; }
.mt-3 { margin-top: .75rem; } .mt-4 { margin-top: 1rem; } .mt-6 { margin-top: 1.5rem; }
.gap-2 { gap: .5rem; } .gap-3 { gap: .75rem; } .gap-6 { gap: 1.5rem; }
.grid { display: grid; }
.flex { display: flex; }
.items-center { align-items: center; }
.items-start { align-items: flex-start; }
.justify-between { justify-content: space-between; }
.place-items-center { place-items: center; }
.h-5 { height: 1.25rem; } .w-5 { width: 1.25rem; }
.h-6 { height: 1.5rem; } .w-6 { width: 1.5rem; }
.h-9 { height: 2.25rem; } .w-9 { width: 2.25rem; }
.h-28 { height: 7rem; } .w-28 { width: 7rem; }
.text-xs { font-size: .75rem; } .text-sm { font-size: .875rem; } .text-base { font-size: 1rem; }
.text-lg { font-size: 1.125rem; } .text-2xl { font-size: 1.5rem; }
.font-medium { font-weight: 500; } .font-semibold { font-weight: 600; }
.leading-tight { line-height: 1.25; } .leading-relaxed { line-height: 1.625; }
.uppercase { text-transform: uppercase; }
.tracking-wide { letter-spacing: .025em; }
.rounded { border-radius: .25rem; }

/***** responsive helpers *****/
@media (min-width: 640px) { /* sm */
  .sm\:grid-cols-2 { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
@media (min-width: 768px) { /* md */
  .md\:grid-cols-3 { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .md\:flex-row { flex-direction: row; }
  .md\:items-start { align-items: flex-start; }
  .md\:p-6 { padding: 1.5rem; }
}
@media (min-width: 1024px) { /* lg */
  .lg\:p-8 { padding: 2rem; }
}

/***** misc *****/
.hover\:bg-slate-100:hover { background: #4a7eb3ff; }
.hover\:underline:hover { text-decoration: underline; }
.ml-auto { margin-left: auto; }
.shrink-0 { flex-shrink: 0; }
.select-none { user-select: none; }
.max-w-7xl { max-width: 80rem; }
.max-w-3xl { max-width: 48rem; }

/* simple utility for ring border on avatars */
.ring-teal-200 { box-shadow: 0 0 0 1px #a7f3d0; }
</style>
