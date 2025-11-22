<template>
  <main class="main-content-area"> <div v-if="loading" class="text-center py-10 text-slate-600">
    <p>Carregando informações do colaborador...</p>
  </div>

    <div v-else-if="error" class="text-center py-10 text-red-600 bg-red-50 rounded-lg p-4">
      <p class="font-semibold">Ocorreu um erro</p>
      <p class="text-sm">{{ error }}</p>
    </div>

    <template v-else-if="funcionario">
      <section class="profile-section">
        <div class="profile-header">
          <div class="avatar-status">
            <div class="avatar-large">{{ getInitials(funcionario.nomeCompleto) }}</div>
            <span class="status-badge"><span class="status-dot"></span>Ativo</span>
          </div>
          <div class="profile-info">
            <div class="name-title">
              <h2 class="name">{{ funcionario.nomeCompleto }}</h2>
              <p class="title">{{ funcionario.tituloProfissional }}</p>
            </div>
            <div class="summary-section">
              <div class="summary-header">
                <h3 class="section-title">Resumo Profissional</h3>
                <button v-if="podeEditarPerfil && !isEditingResumo" @click="iniciarEdicaoResumo" class="edit-button" aria-label="Editar Resumo">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/><path d="m15 5 4 4"/></svg>
                </button>
                <div v-if="podeEditarPerfil && isEditingResumo" class="edit-actions">
                  <button @click="cancelarEdicaoResumo" class="cancel-button" aria-label="Cancelar Edição"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg></button>
                  <button @click="salvarResumo" class="save-button" aria-label="Salvar Resumo"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg></button>
                </div>
              </div>
              <p v-if="!isEditingResumo" class="summary-text">
                {{ funcionario.resumo || 'Nenhum resumo profissional cadastrado.' }}
                <span v-if="!funcionario.resumo && podeEditarPerfil" class="edit-prompt">(Clique no lápis para adicionar)</span>
              </p>
              <textarea v-else v-model="resumoEmEdicao" rows="4" class="summary-textarea" placeholder="Escreva um breve resumo..."></textarea>
            </div>
            <dl class="details-grid">
              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon"><path d="M12 10h.01"/><path d="M12 14h.01"/><path d="M12 6h.01"/><path d="M16 10h.01"/><path d="M16 14h.01"/><path d="M16 6h.01"/><path d="M8 10h.01"/><path d="M8 14h.01"/><path d="M8 6h.01"/><path d="M9 22v-3a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v3"/><rect x="4" y="2" width="16" height="20" rx="2"/></svg>
                <div><dt class="detail-label">Departamento</dt><dd class="detail-value">{{ funcionario.nomeArea }}</dd></div>
              </div>
              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon lucide lucide-map-pin"><path d="M12 13.5a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/><path d="M12 2a9 9 0 0 0-9 9c0 6 9 13 9 13s9-7 9-13a9 9 0 0 0-9-9Z"/></svg>
                <div><dt class="detail-label">Localização</dt><dd class="detail-value">{{ funcionario.localizacao }}</dd></div>
              </div>
              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon lucide lucide-mail"><path d="m22 7-8.991 5.727a2 2 0 0 1-2.009 0L2 7"/><rect x="2" y="4" width="20" height="16" rx="2"/></svg>
                <div><dt class="detail-label">Email</dt><dd class="detail-value"><a class="email-link" :href="'mailto:' + funcionario.email">{{ funcionario.email }}</a></dd></div>
              </div>
              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon lucide lucide-calendar"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                <div><dt class="detail-label">Data de Admissão</dt><dd class="detail-value">{{ formatDate(funcionario.dataCadastro) }}</dd></div>
              </div>
              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon lucide lucide-phone"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.63A2 2 0 0 1 3.08 2h3a2 2 0 0 1 2 1.72a12.84 12.84 0 0 0 .7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45a12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92Z"/></svg>
                <div><dt class="detail-label">Telefone</dt><dd class="detail-value">{{ funcionario.telefone }}</dd></div>
              </div>

                          <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    class="detail-icon">
                  <rect x="3" y="7" width="18" height="13" rx="2" ry="2"/>
                  <path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                </svg>
                <div>
                  <dt class="detail-label">Título Profissional</dt>
                  <dd class="detail-value">
                    {{ funcionario.tituloProfissional || 'Não informado' }}
                  </dd>
                </div>
              </div>
            </dl>
          </div>
        </div>
      </section>

      <section class="section-card">
        <div class="section-header">
          <h3 class="section-title">Histórico Profissional</h3>
          <button v-if="podeEditarPerfil" @click="abrirModalParaNovaExperiencia" class="add-button">➕ Adicionar Novo</button>
        </div>
        <div v-if="funcionario.experiencias && funcionario.experiencias.length" class="experience-list">
          <div v-for="exp in funcionario.experiencias" :key="exp.codigo" class="experience-item">
            <div class="experience-header">
              <h4 class="experience-title">{{ exp.cargo }}</h4>
              <button v-if="podeEditarPerfil" @click="abrirModalParaEditarExperiencia(exp)" class="edit-experience-button" aria-label="Editar Experiência"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/></svg></button>
            </div>
            <p class="experience-company">{{ exp.empresa }}</p>
            <p class="experience-dates">{{ formatDate(exp.dataInicio) }} - {{ exp.dataFim ? formatDate(exp.dataFim) : 'Atual' }}</p>
            <p v-if="exp.descricao" class="experience-description">{{ exp.descricao }}</p>
          </div>
        </div>
        <p v-else class="no-data-message">Nenhuma experiência profissional cadastrada.</p>
      </section>

      <div class="bottom-grid">
        <section class="section-card">
          <div class="section-header">
            <h3 class="section-title">Competências</h3>
            <button v-if="podeEditarCompetencias" @click="navegarParaCompetencias" class="edit-link-button">Editar</button>
          </div>
          <div v-if="funcionario.competencias && funcionario.competencias.length" class="competencies-list">
            <div v-for="comp in funcionario.competencias" :key="comp.id" class="competency-item">• {{ comp.nome }}</div>
          </div>
          <p v-else class="no-data-message">Nenhuma competência cadastrada.</p>
        </section>

        <section class="section-card">
          <h3 class="section-title">Certificações</h3>
          <div class="certificates-list">
            <div v-for="cert in funcionario.certificados" :key="cert.codigo" class="certificate-item">
              <span class="certificate-name">{{ cert.nome }}</span>
              <button v-if="podeEditarPerfil" @click="removerCertificado(cert.codigo)" class="remove-button" aria-label="Remover certificação"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="remove-icon"><line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" /></svg></button>
            </div>
            <div v-if="isAddingCertificado" class="certificate-input-item">
              <input ref="inputNovoCertificado" v-model="novoCertificadoNome" @keydown.enter.prevent="salvarNovoCertificado" @keydown.esc.prevent="cancelarAdicao" type="text" placeholder="Digite o nome e tecle Enter" class="certificate-input"/>
              <button @click="cancelarAdicao" class="cancel-add-button" aria-label="Cancelar adição"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="remove-icon"><line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" /></svg></button>
            </div>
            <p v-if="(!funcionario.certificados || !funcionario.certificados.length) && !isAddingCertificado" class="no-data-message certificates">Nenhuma certificação cadastrada.</p>
          </div>
          <div class="add-certificate-area" v-if="podeEditarPerfil && !isAddingCertificado">
            <button @click="iniciarAdicao" class="add-certificate-button">Adicionar Certificação</button>
          </div>
        </section>

        <section class="section-card">
          <h3 class="section-title">Equipe</h3>
          <dl class="team-details">
            <div><dt class="detail-label">Gestor</dt><dd class="detail-value">{{ funcionario.nomeGestor || 'Não informado' }}</dd></div>
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
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router'; // useRoute adicionado
import { useAuth } from '../auth';
import ExperienciaModal from './ExperienciaModal.vue';

const props = defineProps({ id: String });
const router = useRouter();
const route = useRoute(); // Instancia useRoute
const { usuarioLogado } = useAuth();

const funcionario = ref(null);
const loading = ref(true);
const error = ref(null);
const isEditingResumo = ref(false);
const resumoEmEdicao = ref('');
const isExperienciaModalOpen = ref(false);
const experienciaSendoEditada = ref(null);
const isAddingCertificado = ref(false);
const novoCertificadoNome = ref('');
const inputNovoCertificado = ref(null);

const fetchFuncionarioData = async () => {
  loading.value = true; error.value = null;
  const funcionarioId = props.id || route.params.id;
  if (!funcionarioId) { error.value = "ID do funcionário não encontrado."; loading.value = false; return; }
  try {
    const response = await axios.get(`http://localhost:8080/api/funcionario/${funcionarioId}`);
    funcionario.value = response.data;
  } catch (err) {
    console.error("Falha:", err); error.value = "Não foi possível carregar.";
    if (err.response?.status === 404) { error.value = `Funcionário ${funcionarioId} não encontrado.`; }
  } finally { loading.value = false; }
};

onMounted(fetchFuncionarioData);

const podeEditarPerfil = computed(() => !!(usuarioLogado.value && funcionario.value && String(usuarioLogado.value.codigo) === String(funcionario.value.codigo)));
const podeEditarCompetencias = computed(() => !!(usuarioLogado.value && funcionario.value && String(usuarioLogado.value.codigo) === String(funcionario.value.codigo))); // Simplificado, só o user edita por enquanto
const navegarParaCompetencias = () => router.push({ name: 'ColaboradorCompetencias', params: { id: funcionario.value.codigo } });
const getInitials = (fullName) => { if (!fullName) return ''; const names = fullName.split(' '); const initials = names.length > 1 ? `${names[0][0]}${names[names.length - 1][0]}` : names[0].substring(0, 2); return initials.toUpperCase(); };
const formatDate = (dateString) => { if (!dateString) return ''; return new Date(dateString).toLocaleDateString('pt-BR', { timeZone: 'UTC' }); };
const iniciarEdicaoResumo = () => { resumoEmEdicao.value = funcionario.value.resumo || ''; isEditingResumo.value = true; };
const cancelarEdicaoResumo = () => { isEditingResumo.value = false; };
const salvarResumo = async () => { if (resumoEmEdicao.value === funcionario.value.resumo) { cancelarEdicaoResumo(); return; } const payload = { ...funcionario.value, resumo: resumoEmEdicao.value, }; try { const response = await axios.put(`http://localhost:8080/api/funcionario/${funcionario.value.codigo}`, payload); funcionario.value = response.data; isEditingResumo.value = false; alert('Resumo atualizado!'); } catch (err) { console.error("Falha:", err); alert("Erro ao salvar."); } };
const abrirModalParaNovaExperiencia = () => { experienciaSendoEditada.value = null; isExperienciaModalOpen.value = true; };
const abrirModalParaEditarExperiencia = (exp) => { experienciaSendoEditada.value = { ...exp }; isExperienciaModalOpen.value = true; };
const fecharExperienciaModal = () => { isExperienciaModalOpen.value = false; };
const salvarExperiencia = async (expData) => { try { let response; if (expData.codigo) { response = await axios.put(`http://localhost:8080/api/funcionario/experiencias/${expData.codigo}`, expData); } else { const funcionarioId = funcionario.value.codigo; response = await axios.post(`http://localhost:8080/api/funcionario/${funcionarioId}/experiencias`, expData); } alert('Experiência salva!'); fecharExperienciaModal(); await fetchFuncionarioData(); } catch (err) { console.error("Falha:", err); alert("Erro ao salvar."); } };
const removerCertificado = async (certId) => { if (!confirm("Remover certificação?")) return; try { await axios.delete(`http://localhost:8080/api/funcionario/certificados/${certId}`); alert('Removido!'); await fetchFuncionarioData(); } catch (err) { console.error("Falha:", err); alert("Erro ao remover."); } };
const iniciarAdicao = async () => { isAddingCertificado.value = true; await nextTick(); inputNovoCertificado.value?.focus(); };
const cancelarAdicao = () => { isAddingCertificado.value = false; novoCertificadoNome.value = ''; };
const salvarNovoCertificado = async () => { const nomeLimpo = novoCertificadoNome.value.trim(); if (nomeLimpo === '') { cancelarAdicao(); return; } const payload = { nome: nomeLimpo }; try { const funcionarioId = funcionario.value.codigo; await axios.post(`http://localhost:8080/api/funcionario/${funcionarioId}/certificados`, payload); await fetchFuncionarioData(); } catch (err) { console.error("Falha:", err); alert("Erro ao salvar."); } finally { cancelarAdicao(); } };
</script>

<style scoped>
/* Estilos adaptados sem Tailwind */
:root { /* Fallback */
  --bg: #f8fafc; --card: #ffffff; --border: #e2e8f0; --shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  --title: #1e293b; --subtitle: #64748b; --text-secondary: #334155; --text-tertiary: #475569;
  --teal-600: #0d9488; --teal-700: #0f766e; --teal-50: #f0fdfa; --teal-100: #ccfbf1; --teal-200: #99f6e4;
  --emerald-50: #ecfdf5; --emerald-200: #a7f3d0; --emerald-500: #10b981; --emerald-700: #047857;
  --red-50: #fef2f2; --red-600: #dc2626; --slate-100: #f1f5f9; --slate-400: #94a3b8;
}

.main-content-area { max-width: 80rem; margin-left: auto; margin-right: auto; padding: 1rem; }
@media (min-width: 768px) { .main-content-area { padding: 1.5rem; } }
@media (min-width: 1024px) { .main-content-area { padding: 2rem; } }

.profile-section { background-color: var(--card, #fff); border-radius: 1rem; box-shadow: var(--shadow); border: 1px solid var(--border); padding: 1.5rem; }
.profile-header { display: flex; flex-direction: column; gap: 1.5rem; }
@media (min-width: 768px) { .profile-header { flex-direction: row; align-items: flex-start; } }

.avatar-status { display: flex; flex-direction: column; align-items: center; position: relative; padding-bottom: 1rem; /* Espaço para o badge */ flex-shrink: 0;}
@media (min-width: 768px) { .avatar-status { align-items: flex-start; padding-bottom: 0; } }
.avatar-large { height: 7rem; width: 7rem; border-radius: 9999px; background-color: var(--teal-600, #0d9488); color: white; display: grid; place-items: center; font-size: 1.875rem; font-weight: 600; user-select: none; }
.status-badge {
  margin-top: 0.75rem;
  /* Posicionamento absoluto no mobile/desktop pequeno */
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  display: inline-flex; align-items: center; gap: 0.5rem; border-radius: 9999px; background-color: var(--emerald-50); padding: 0.25rem 0.75rem; font-size: 0.75rem; font-weight: 500; color: var(--emerald-700); border: 1px solid var(--emerald-200); white-space: nowrap;
}
@media (min-width: 768px) {
  .status-badge {
    position: static; /* Volta a ser inline no desktop */
    transform: none;
    margin-top: 0.75rem; /* Mantém margem */
  }
}
.status-dot { height: 0.625rem; width: 0.625rem; border-radius: 9999px; background-color: var(--emerald-500); }

.profile-info { flex: 1; min-width: 0; /* Para evitar overflow */}
.name-title { margin-bottom: 1rem; /* Adiciona espaço abaixo */ }
.name { font-size: 1.5rem; font-weight: 600; color: var(--title); }
.title { color: var(--subtitle); }

.summary-section { margin-top: 1.5rem; }
.summary-header { display: flex; align-items: center; justify-content: space-between; gap: 0.5rem; /* Adiciona gap */ margin-bottom: 0.5rem; /* Adiciona espaço abaixo */}
.section-title { font-size: 1rem; font-weight: 600; color: var(--title); }
.edit-button { height: 1.5rem; width: 1.5rem; display: grid; place-items: center; border-radius: 9999px; color: var(--slate-400); background: none; border: none; cursor: pointer; flex-shrink: 0; }
.edit-button:hover { background-color: var(--slate-100); color: var(--teal-600); }
.edit-actions { display: flex; align-items: center; gap: 0.5rem; flex-shrink: 0;}
.cancel-button, .save-button { height: 2rem; width: 2rem; display: grid; place-items: center; border-radius: 9999px; color: var(--subtitle); background: none; border: none; cursor: pointer; }
.cancel-button:hover { background-color: var(--slate-100); color: var(--red-600); }
.save-button:hover { background-color: var(--slate-100); color: var(--emerald-700); }
.summary-text { margin-top: 0.5rem; font-size: 0.875rem; line-height: 1.625; color: var(--text-secondary); max-width: 48rem; }
.edit-prompt { font-style: italic; color: var(--subtitle); margin-left: 0.25rem; }
.summary-textarea { margin-top: 0.5rem; width: 100%; max-width: 48rem; border-radius: 0.375rem; background-color: var(--card); border: 1px solid var(--border); font-size: 0.875rem; color: var(--title); resize: vertical; padding: 0.5rem 0.75rem; }
.summary-textarea:focus { outline: 2px solid transparent; outline-offset: 2px; border-color: var(--teal-600); box-shadow: 0 0 0 3px rgba(13, 148, 136, 0.3); }

.details-grid { margin-top: 1.5rem; display: grid; grid-template-columns: repeat(1, minmax(0, 1fr)); gap: 1.5rem; }
@media (min-width: 640px) { .details-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); } }
@media (min-width: 1024px) { .details-grid { grid-template-columns: repeat(3, minmax(0, 1fr)); } }
.detail-item { display: flex; align-items: flex-start; gap: 0.75rem; }
.detail-icon { margin-top: 1px; height: 1.25rem; width: 1.25rem; color: var(--teal-600); flex-shrink: 0; }
.detail-label { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.025em; color: var(--subtitle); }
.detail-value { font-size: 0.875rem; font-weight: 500; color: var(--title); word-break: break-word; /* Quebra emails longos */ }
.email-link { color: var(--teal-700); }
.email-link:hover { text-decoration: underline; }

.section-card { background-color: var(--card, #fff); border-radius: 1rem; box-shadow: var(--shadow); border: 1px solid var(--border); padding: 1.5rem; }
.bottom-grid { margin-top: 1.5rem; display: grid; grid-template-columns: repeat(1, minmax(0, 1fr)); gap: 1.5rem; }
@media (min-width: 768px) { .bottom-grid { grid-template-columns: repeat(3, minmax(0, 1fr)); } }

.section-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1rem; }
.add-button { border-radius: 0.5rem; background-color: var(--teal-50); padding: 0.375rem 0.75rem; font-size: 0.875rem; font-weight: 600; color: var(--teal-700); border: none; cursor: pointer; }
.add-button:hover { background-color: var(--teal-100); }
.edit-link-button { border-radius: 0.375rem; padding: 0.25rem 0.625rem; font-size: 0.875rem; font-weight: 600; color: var(--teal-700); background: none; border: none; cursor: pointer; }
.edit-link-button:hover { background-color: var(--teal-50); }

.experience-list { margin-top: 1rem; display: flex; flex-direction: column; gap: 1.5rem; }
.experience-item { position: relative; border-left: 2px solid var(--teal-600); padding: 0.5rem 0 0.5rem 1rem; }
.experience-header { display: flex; align-items: center; justify-content: space-between; gap: 0.5rem; }
.experience-title { font-size: 1rem; /* Ajustado */ font-weight: 500; color: var(--title); }
.edit-experience-button { position: absolute; top: 4px; right: 0px; height: 2rem; width: 2rem; display: grid; place-items: center; border-radius: 9999px; color: var(--slate-400); background: none; border: none; cursor: pointer; }
.edit-experience-button:hover { background-color: var(--slate-100); color: var(--teal-600); }
.experience-company { font-size: 0.875rem; color: var(--text-secondary); }
.experience-dates { font-size: 0.75rem; color: var(--subtitle); margin-top: 0.25rem; }
.experience-description { margin-top: 0.5rem; padding-right: 2rem; font-size: 0.875rem; color: var(--text-tertiary); }

.competencies-list { margin-top: 1rem; display: flex; flex-direction: column; gap: 0.5rem; }
.competency-item { display: flex; font-size: 0.875rem; color: var(--text-secondary); }

.certificates-list { margin-top: 1rem; display: flex; flex-direction: column; gap: 0.75rem; }
.certificate-item { display: flex; align-items: center; gap: 0.75rem; border-radius: 0.5rem; border: 1px solid var(--border); background-color: var(--bg); padding: 0.5rem 0.25rem 0.5rem 0.75rem; }
.certificate-name { flex-grow: 1; font-size: 0.875rem; font-weight: 500; color: var(--text-secondary); }
.remove-button { height: 1.75rem; width: 1.75rem; display: grid; place-items: center; border-radius: 0.375rem; color: var(--subtitle); background: none; border: none; cursor: pointer; flex-shrink: 0; }
.remove-button:hover { background-color: #e2e8f0; color: var(--title); }
.remove-icon { height: 1rem; width: 1rem; }
.certificate-input-item { display: flex; align-items: center; gap: 0.75rem; border-radius: 0.5rem; border: 1px solid var(--teal-500); background-color: white; padding: 0.5rem 0.25rem 0.5rem 0.75rem; box-shadow: 0 0 0 2px rgba(13, 148, 136, 0.2); }
.certificate-input { flex-grow: 1; font-size: 0.875rem; font-weight: 500; color: var(--text-secondary); background: transparent; border: none; outline: none; padding: 0; }
.cancel-add-button { height: 1.75rem; width: 1.75rem; display: grid; place-items: center; border-radius: 0.375rem; color: var(--subtitle); background: none; border: none; cursor: pointer; flex-shrink: 0; }
.cancel-add-button:hover { background-color: #e2e8f0; color: var(--title); }
.add-certificate-area { margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border); }
.add-certificate-button { width: 100%; text-align: center; border-radius: 0.5rem; border: 1px dashed var(--border); padding: 0.5rem 0.75rem; font-size: 0.875rem; font-weight: 600; color: var(--text-secondary); background: none; cursor: pointer; }
.add-certificate-button:hover { border-color: var(--teal-500); background-color: var(--teal-50); color: var(--teal-700); }

.team-details { margin-top: 1rem; display: flex; flex-direction: column; gap: 1rem; }

.no-data-message { margin-top: 1rem; font-size: 0.875rem; color: var(--subtitle); }
.no-data-message.certificates { padding-top: 0.5rem; padding-bottom: 0.5rem; }

.text-center { text-align: center; }
.py-10 { padding-top: 2.5rem; padding-bottom: 2.5rem; }
.text-slate-600 { color: #475569; }
.text-red-600 { color: #dc2626; }
.bg-red-50 { background-color: #fef2f2; }
</style>
