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
            <div class="flex flex-col items-center md:items-start">
              <div class="h-28 w-28 shrink-0 rounded-full bg-teal-600 text-white grid place-items-center text-3xl font-semibold select-none">
                {{ getInitials(funcionario.nomeCompleto) }}
              </div>
              <span class="mt-3 inline-flex items-center gap-2 rounded-full bg-emerald-50 px-3 py-1 text-xs font-medium text-emerald-700 border border-emerald-200">
                <span class="h-2.5 w-2.5 rounded-full bg-emerald-500"></span>
                Ativo
              </span>
            </div>

            <div class="flex-1">
              <div class="gap-1">
                <h2 class="text-2xl font-semibold">{{ funcionario.nomeCompleto }}</h2>
                <p class="text-slate-500">{{ funcionario.tituloProfissional }}</p>
              </div>
              <p class="mt-4 text-sm leading-relaxed text-slate-700 max-w-3xl">
                {{ funcionario.resumo }}
              </p>

              <dl class="mt-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Departamento</dt>
                  <dd class="text-sm font-medium">{{ funcionario.nomeArea }}</dd>
                </div>
                 <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Localização</dt>
                  <dd class="text-sm font-medium">{{ funcionario.localizacao }}</dd>
                </div>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Data de Admissão</dt>
                  <dd class="text-sm font-medium">{{ formatDate(funcionario.dataCadastro) }}</dd>
                </div>
                 <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Email</dt>
                  <dd class="text-sm font-medium">
                    <a class="text-teal-700 hover:underline" :href="'mailto:' + funcionario.email">{{ funcionario.email }}</a>
                  </dd>
                </div>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Telefone</dt>
                  <dd class="text-sm font-medium">{{ funcionario.telefone }}</dd>
                </div>
              </dl>
            </div>
          </div>
        </section>

        <section class="mt-6 rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
                  <h3 class="text-base font-semibold">Histórico Profissional</h3>

                  <div v-if="funcionario.experiencias && funcionario.experiencias.length" class="space-y-6 mt-4">
                    <div v-for="experiencia in funcionario.experiencias" :key="experiencia.codigo" class="border-l-4 border-teal-600 pl-4">

                      <h4 class="text-lg font-medium">{{ experiencia.cargo }}</h4>

                      <p class="text-sm text-slate-700">{{ experiencia.empresa }}</p>

                      <p class="text-xs text-slate-500 mt-1">
                        {{ formatDate(experiencia.dataInicio) }} - {{ experiencia.dataFim ? formatDate(experiencia.dataFim) : 'Atual' }}
                      </p>

                      <p v-if="experiencia.descricao" class="text-sm text-slate-600 mt-2">
                        {{ experiencia.descricao }}
                      </p>
                      </div>
                  </div>
                  <p v-else class="mt-4 text-sm text-slate-500">Nenhuma experiência profissional cadastrada.</p>
        </section>

        <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-6">
          <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
            <h3 class="text-base font-semibold">Competências</h3>
            <div v-if="funcionario.competencias && funcionario.competencias.length" class="mt-4 flex flex-wrap gap-2">
              <Tag v-for="competencia in funcionario.competencias" :key="competencia.codigo" :label="competencia.nome" />
            </div>
             <p v-else class="mt-4 text-sm text-slate-500">Nenhuma competência cadastrada.</p>
          </section>

          <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
            <h3 class="text-base font-semibold">Certificações</h3>
            <ul v-if="funcionario.certificados && funcionario.certificados.length" class="mt-4 space-y-3">
              <li v-for="certificado in funcionario.certificados" :key="certificado.codigo" class="flex items-center gap-3">
                 <span class="text-sm">{{ certificado.nome }}</span>
              </li>
            </ul>
            <p v-else class="mt-4 text-sm text-slate-500">Nenhuma certificação cadastrada.</p>
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

    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const funcionario = ref(null); // Inicia como nulo. Só recebe valor após a chamada da API.
const loading = ref(true);     // Inicia como verdadeiro, pois começamos carregando.
const error = ref(null);

const fetchFuncionarioData = async (id) => {
  loading.value = true;
  error.value = null;
  try {
    // IMPORTANTE: Verifique se a porta (:8080) é a mesma do seu backend Spring Boot.
    const response = await axios.get(`http://localhost:8080/api/funcionario/${id}`);
    funcionario.value = response.data; // Armazena os dados da API na nossa variável reativa.
  } catch (err) {
    console.error("Falha ao buscar dados do funcionário:", err);
    error.value = "Não foi possível carregar as informações. Tente novamente mais tarde.";
  } finally {
    loading.value = false; // Define como falso ao final, tanto em caso de sucesso quanto de erro.
  }
};

onMounted(() => {
  fetchFuncionarioData(5);
});

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
