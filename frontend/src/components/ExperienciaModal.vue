<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="fechar">
    <div class="modal-content">
      <h3 class="text-lg font-semibold text-slate-800">{{ tituloModal }}</h3>
      
      <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div>
          <label for="cargo" class="form-label">Cargo</label>
          <input id="cargo" type="text" v-model="formData.cargo" class="form-input">
        </div>
        <div>
          <label for="empresa" class="form-label">Empresa</label>
          <input id="empresa" type="text" v-model="formData.empresa" class="form-input">
        </div>
        <div>
          <label for="dataInicio" class="form-label">Data de Início</label>
          <input id="dataInicio" type="date" v-model="formData.dataInicio" class="form-input">
        </div>
        <div>
          <label for="dataFim" class="form-label">Data de Fim (deixe em branco se for o atual)</label>
          <input id="dataFim" type="date" v-model="formData.dataFim" class="form-input">
        </div>
        <div class="sm:col-span-2">
          <label for="descricao" class="form-label">Descrição das Atividades</label>
          <textarea id="descricao" v-model="formData.descricao" rows="4" class="form-textarea"></textarea>
        </div>
      </div>
      
      <div class="mt-6 flex justify-end gap-3">
        <button @click="fechar" class="btn-secondary">Cancelar</button>
        <button @click="salvar" class="btn-primary">Salvar Experiência</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue';

// 1. PROPRIEDADES (Props): O que o componente recebe do "pai" (Perfil.vue)
const props = defineProps({
  isOpen: Boolean, // Controla se o modal está visível
  experienciaParaEditar: Object // Os dados da experiência a ser editada (ou null se for nova)
});

// 2. EVENTOS (Emits): O que o componente "grita" de volta para o pai
const emit = defineEmits(['close', 'save']);

// 3. ESTADO INTERNO: O rascunho do formulário
const formData = ref({});

// Título dinâmico do modal
const tituloModal = computed(() => {
  return props.experienciaParaEditar?.codigo ? 'Editar Experiência' : 'Adicionar Nova Experiência';
});

// 4. WATCHER: Observa a propriedade 'experienciaParaEditar'
// Quando o pai manda uma experiência para editar, este watcher preenche o formulário.
watch(() => props.experienciaParaEditar, (novaExperiencia) => {
  if (novaExperiencia) {
    // Se está editando, copia os dados para o formulário
    formData.value = { ...novaExperiencia };
  } else {
    // Se é uma nova experiência, reseta o formulário
    formData.value = { cargo: '', empresa: '', dataInicio: '', dataFim: null, descricao: '' };
  }
});

// 5. FUNÇÕES INTERNAS
const fechar = () => {
  emit('close'); // Avisa ao pai que o modal deve ser fechado
};

const salvar = () => {
  // Avisa ao pai que o botão salvar foi clicado, enviando os dados do formulário
  emit('save', formData.value);
};
</script>

<style scoped>
/* Estilos para o Modal (você pode copiar e colar do seu Perfil.vue se já os tiver) */
.modal-overlay {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background-color: rgba(15, 23, 42, 0.6);
  display: flex; justify-content: center; align-items: center; z-index: 999;
}
.modal-content {
  background-color: white; padding: 1.5rem; border-radius: 1rem;
  box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
  width: 100%; max-width: 42rem; /* Aumentei um pouco para caber o form */
  margin: 1rem;
}

/* Estilos para os campos do formulário */
.form-label {
  display: block; font-size: 0.875rem; font-weight: 500; color: #475569; margin-bottom: 0.25rem;
}
.form-input, .form-textarea {
  display: block; 
  width: 100%; 
  border-radius: 0.375rem; 
  border: 1px solid #cbd5e1;
  box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05); 
  padding: 0.5rem 0.75rem;
  color: #334155; /* <<< LINHA ADICIONADA AQUI: Define a cor do texto para cinza escuro */
}
.form-input:focus, .form-textarea:focus {
  outline: none; border-color: #0d9488; box-shadow: 0 0 0 2px #a7f3d0;
}
.btn-primary {
  border-radius: 0.5rem; background-color: #047857; padding: 0.5rem 1rem;
  font-size: 0.875rem; font-weight: 600; color: white;
}
.btn-secondary {
  border-radius: 0.5rem; background-color: #e2e8f0; padding: 0.5rem 1rem;
  font-size: 0.875rem; font-weight: 600; color: #334155;
}
</style>