<template>
  <div class="min-h-screen bg-slate-50 text-slate-800">
    <!-- Top bar -->
    <header class="bg-white border-b border-slate-200">
      <div class="mx-auto max-w-7xl px-4 py-4 flex items-center gap-3">
        <button class="h-9 w-9 grid place-items-center rounded-lg border border-slate-200 hover:bg-slate-100" aria-label="Voltar">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5">
            <path fill-rule="evenodd" d="M10.03 4.97a.75.75 0 0 1 0 1.06L5.06 11l4.97 4.97a.75.75 0 1 1-1.06 1.06l-5.5-5.5a.75.75 0 0 1 0-1.06l5.5-5.5a.75.75 0 0 1 1.06 0Zm4.94 0a.75.75 0 0 1 1.06 0l5.5 5.5a.75.75 0 0 1 0 1.06l-5.5 5.5a.75.75 0 1 1-1.06-1.06L18.94 11l-4.97-4.97a.75.75 0 0 1 1.06-1.06Z" clip-rule="evenodd"/>
          </svg>
        </button>
        <div>
          <h1 class="text-lg font-semibold">Perfil do Colaborador</h1>
          <p class="text-sm text-slate-500 leading-tight">Informações detalhadas</p>
        </div>
        <div class="ml-auto flex items-center gap-2">
          <button class="h-9 w-9 grid place-items-center rounded-lg border border-slate-200 hover:bg-slate-100" aria-label="Anterior">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-5 w-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
            </svg>
          </button>
          <button class="h-9 w-9 grid place-items-center rounded-lg border border-slate-200 hover:bg-slate-100" aria-label="Próximo">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-5 w-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
            </svg>
          </button>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="mx-auto max-w-7xl p-4 md:p-6 lg:p-8">
      <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
        <div class="flex flex-col md:flex-row md:items-start gap-6">
          <!-- Avatar & status -->
          <div class="gap-1 items-center md:items-start">
            <div class="h-28 w-28 shrink-0 rounded-full bg-teal-600 text-white grid place-items-center text-3xl font-semibold select-none">AS</div>
            <span class="mt-3 inline-flex items-center gap-2 rounded-full bg-emerald-50 px-3 py-1 text-xs font-medium text-emerald-700 border border-emerald-200">
              <span class="h-2.5 w-2.5 rounded-full bg-emerald-500"></span>
              Ativo
            </span>
          </div>

          <!-- Header info -->
          <div class="flex-1">
            <div class="gap-1">
              <h2 class="text-2xl font-semibold">Ana Silva Santos</h2>
              <p class="text-slate-500">Analista de Recursos Humanos</p>
            </div>
            <p class="mt-4 text-sm leading-relaxed text-slate-700 max-w-3xl">
              Profissional experiente em Recursos Humanos com foco em recrutamento e desenvolvimento de talentos. Possui mais de 3 anos de experiência na área e é responsável por processos seletivos e treinamentos corporativos.
            </p>

            <!-- Details grid -->
            <dl class="mt-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              <div class="flex items-start gap-3">
                <span class="mt-0.5 h-9 w-9 grid place-items-center rounded-lg bg-slate-100 text-slate-600">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5"><path d="M2.25 12a8.25 8.25 0 1 1 16.5 0 8.25 8.25 0 0 1-16.5 0Zm9.615-3.596a.75.75 0 0 0-1.28-.808l-2.86 4.53a.75.75 0 0 0 .631 1.154h2.232l-.903 1.456a.75.75 0 0 0 1.28.808l2.86-4.53a.75.75 0 0 0-.631-1.154h-2.232l.903-1.456Z"/></svg>
                </span>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Departamento</dt>
                  <dd class="text-sm font-medium">Recursos Humanos</dd>
                </div>
              </div>

              <div class="flex items-start gap-3">
                <span class="mt-0.5 h-9 w-9 grid place-items-center rounded-lg bg-slate-100 text-slate-600">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 1 1-6 0 3 3 0 0 1 6 0ZM19.5 10.5c0 7.142-7.5 10.5-7.5 10.5S4.5 17.642 4.5 10.5a7.5 7.5 0 1 1 15 0Z"/></svg>
                </span>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Localização</dt>
                  <dd class="text-sm font-medium">São Paulo - SP</dd>
                </div>
              </div>

              <div class="flex items-start gap-3">
                <span class="mt-0.5 h-9 w-9 grid place-items-center rounded-lg bg-slate-100 text-slate-600">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5"><path fill-rule="evenodd" d="M6.75 3a.75.75 0 0 1 .75.75V5.5h9V3.75a.75.75 0 0 1 1.5 0V5.5h.75A2.25 2.25 0 0 1 21.5 7.75v10.5A2.25 2.25 0 0 1 19.25 20.5H4.75A2.25 2.25 0 0 1 2.5 18.25V7.75A2.25 2.25 0 0 1 4.75 5.5h.75V3.75A.75.75 0 0 1 6.25 3h.5Zm-2 6.5a.75.75 0 0 0 0 1.5h14.5a.75.75 0 0 0 0-1.5H4.75Z" clip-rule="evenodd"/></svg>
                </span>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Data de Admissão</dt>
                  <dd class="text-sm font-medium">15/03/2022</dd>
                </div>
              </div>

              <div class="flex items-start gap-3">
                <span class="mt-0.5 h-9 w-9 grid place-items-center rounded-lg bg-slate-100 text-slate-600">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M2.25 6.75c0-1.243 1.007-2.25 2.25-2.25h15c1.243 0 2.25 1.007 2.25 2.25v10.5c0 1.243-1.007 2.25-2.25 2.25h-15A2.25 2.25 0 0 1 2.25 17.25V6.75Zm3 0h13.5v.243a2.25 2.25 0 0 1-2.25 2.25H7.5a2.25 2.25 0 0 1-2.25-2.25V6.75Z"/></svg>
                </span>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Horário de Trabalho</dt>
                  <dd class="text-sm font-medium">08:00 - 17:00</dd>
                </div>
              </div>

              <div class="flex items-start gap-3">
                <span class="mt-0.5 h-9 w-9 grid place-items-center rounded-lg bg-slate-100 text-slate-600">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5"><path d="M1.5 4.5A2.25 2.25 0 0 1 3.75 2.25h16.5A2.25 2.25 0 0 1 22.5 4.5v15a2.25 2.25 0 0 1-2.25 2.25H3.75A2.25 2.25 0 0 1 1.5 19.5v-15ZM6 7.5h12a.75.75 0 0 1 0 1.5H6a.75.75 0 0 1 0-1.5Zm0 4.5h12a.75.75 0 0 1 0 1.5H6a.75.75 0 0 1 0-1.5Zm0 4.5h7.5a.75.75 0 0 1 0 1.5H6a.75.75 0 0 1 0-1.5Z"/></svg>
                </span>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Email</dt>
                  <dd class="text-sm font-medium">
                    <a class="text-teal-700 hover:underline" href="mailto:ana.santos@empresa.com">ana.santos@empresa.com</a>
                  </dd>
                </div>
              </div>

              <div class="flex items-start gap-3">
                <span class="mt-0.5 h-9 w-9 grid place-items-center rounded-lg bg-slate-100 text-slate-600">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5"><path fill-rule="evenodd" d="M1.5 4.875C1.5 3.84 2.34 3 3.375 3h2.548c.87 0 1.635.594 1.848 1.44l.776 3.104c.19.758-.135 1.55-.79 1.96l-1.293.796c1.214 2.643 3.327 4.756 5.97 5.97l.795-1.293c.41-.655 1.202-.98 1.96-.79l3.104.777c.846.212 1.44.978 1.44 1.848v2.548A1.875 1.875 0 0 1 19.125 21h-1.5C8.56 21 1.5 13.94 1.5 5.25v-0.375Z" clip-rule="evenodd"/></svg>
                </span>
                <div>
                  <dt class="text-xs uppercase tracking-wide text-slate-500">Telefone</dt>
                  <dd class="text-sm font-medium">(11) 99999-0001</dd>
                </div>
              </div>
            </dl>
          </div>
        </div>
      </section>

      <!-- Cards grid -->
      <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- Competências -->
        <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
          <h3 class="text-base font-semibold">Competências</h3>
          <div class="mt-4 flex flex-wrap gap-2">
            <Tag v-for="skill in skills" :key="skill" :label="skill" />
          </div>
        </section>

        <!-- Certificações -->
        <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
          <h3 class="text-base font-semibold">Certificações</h3>
          <ul class="mt-4 space-y-3">
            <li v-for="cert in certifications" :key="cert" class="flex items-center gap-3">
              <span class="h-6 w-6 grid place-items-center rounded-full bg-teal-50 text-teal-700 ring-1 ring-teal-200">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-4 w-4"><path fill-rule="evenodd" d="M16.5 4.5a4.5 4.5 0 1 1-3.879 6.75l-7.62 7.62a1.5 1.5 0 0 1-2.121-2.121l7.62-7.62A4.5 4.5 0 0 1 16.5 4.5Z" clip-rule="evenodd"/></svg>
              </span>
              <span class="text-sm">{{ cert }}</span>
            </li>
          </ul>
        </section>

        <!-- Equipe -->
        <section class="rounded-2xl bg-white shadow-sm ring-1 ring-slate-200 p-6">
          <h3 class="text-base font-semibold">Equipe</h3>
          <dl class="mt-4 space-y-4">
            <div>
              <dt class="text-xs uppercase tracking-wide text-slate-500">Gestor</dt>
              <dd class="text-sm font-medium">Maria Fernanda Costa</dd>
            </div>
            <div>
              <dt class="text-xs uppercase tracking-wide text-slate-500">Time</dt>
              <dd class="text-sm font-medium">RH Operacional</dd>
            </div>
          </dl>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const skills = ref([
  'Recrutamento',
  'Treinamentos',
  'Gestão de Pessoas',
  'Excel Avançado'
])

const certifications = ref([
  'Gestão de RH',
  'Coaching',
  'Liderança'
])
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
