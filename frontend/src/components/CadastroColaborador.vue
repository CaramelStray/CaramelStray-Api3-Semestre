<template>
  <div class="cadastro-page">
    <!-- Header -->
    <div class="page-header">
      <div class="header-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
          <circle cx="9" cy="7" r="4"/>
          <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
          <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
        </svg>
      </div>
      <div class="header-text">
        <h1 class="header-title">Dados do Novo Colaborador</h1>
        <p class="header-subtitle">Preencha as informações do colaborador</p>
      </div>
    </div>

    <!-- Formulário -->
    <form @submit.prevent="handleSubmit" class="form-container">
      <!-- Informações Básicas -->
      <div class="form-section">
        <h2 class="section-title">Informações Básicas</h2>
        
        <div class="form-grid">
          <!-- Nome Completo -->
          <div class="form-group">
            <label for="nomeCompleto" class="label">
              Nome Completo <span class="required">*</span>
            </label>
            <input
              id="nomeCompleto"
              v-model="formData.nomeCompleto"
              type="text"
              placeholder="Nome completo do colaborador"
              class="input-field"
              required
            />
          </div>

          <!-- Email -->
          <div class="form-group">
            <label for="email" class="label">
              Email <span class="required">*</span>
            </label>
            <input
              id="email"
              v-model="formData.email"
              type="email"
              placeholder="email@allone.com"
              class="input-field"
              required
            />
          </div>

          <!-- ID do Crachá -->
          <div class="form-group">
            <label for="idCracha" class="label">
              ID do Crachá <span class="required">*</span>
            </label>
            <input
              id="idCracha"
              v-model="formData.idCracha"
              type="text"
              placeholder="Ex: CR001"
              class="input-field"
              required
            />
          </div>

          <!-- Senha Inicial -->
          <div class="form-group">
            <label for="senhaInicial" class="label">
              Senha Inicial <span class="required">*</span>
            </label>
            <input
              id="senhaInicial"
              v-model="formData.senhaInicial"
              type="password"
              placeholder="Senha temporária"
              class="input-field"
              required
            />
          </div>

          <!-- Telefone -->
          <div class="form-group">
            <label for="telefone" class="label">Telefone</label>
            <input
              id="telefone"
              v-model="formData.telefone"
              type="tel"
              placeholder="(00) 00000-0000"
              class="input-field"
              v-mask="'(##) #####-####'"
            />
          </div>

          <!-- CPF -->
          <div class="form-group">
            <label for="cpf" class="label">CPF <span class="required">*</span></label>
            <input
              id="cpf"
              v-model="formData.cpf"
              type="text"
              placeholder="000.000.000-00"
              class="input-field"
              required
            />
          </div>

          <!-- Localização -->
          <div class="form-group">
            <label for="localizacao" class="label">Localização</label>
            <input
              id="localizacao"
              v-model="formData.localizacao"
              type="text"
              placeholder="Ex: São Paulo / Remoto"
              class="input-field"
            />
          </div>

          <!-- Data de Admissão -->
          <div class="form-group">
            <label for="dataAdmissao" class="label">Data de Admissão</label>
            <input
              id="dataAdmissao"
              v-model="formData.dataAdmissao"
              type="date"
              placeholder="dd/mm/aaaa"
              class="input-field"
            />
          </div>
        </div>
      </div>

      <!-- Informações Profissionais -->
      <div class="form-section">
        <h2 class="section-title">Informações Profissionais</h2>
        
        <div class="form-grid">
          <!-- Cargo -->
          <div class="form-group">
            <label for="cargo" class="label">Cargo</label>
            <select
              id="cargo"
              v-model="formData.cargo"
              class="select-field"
            >
              <option value="">Selecione o cargo</option>
              <option value="Analista">Analista</option>
              <option value="Desenvolvedor">Desenvolvedor</option>
              <option value="Gerente">Gerente</option>
              <option value="Coordenador">Coordenador</option>
              <option value="Assistente">Assistente</option>
              <option value="Estagiario">Estagiário</option>
              <option value="Outro">Outro</option>
            </select>
          </div>

          <!-- Departamento -->
          <div class="form-group">
            <label for="departamento" class="label">Departamento</label>
            <select
              id="departamento"
              v-model="formData.departamento"
              class="select-field"
            >
              <option value="">Selecione o departamento</option>
              <option value="Analista">Recursos Humanos</option>
              <option value="Desenvolvedor">Tecnologia da Informação</option>
              <option value="Gerente">Financeiro</option>
              <option value="Coordenador">Comercial</option>
              <option value="Assistente">Marketing</option>
              <option value="Estagiario">Operações</option>
              <option value="Outro">Outro</option>
            </select>
          </div>

          <!-- Título Profissional -->
          <div class="form-group">
            <label for="tituloProfissional" class="label">Título Profissional</label>
            <input
              id="tituloProfissional"
              v-model="formData.tituloProfissional"
              type="text"
              placeholder="Ex: Desenvolvedor Pleno"
              class="input-field"
            />
          </div>

          <!-- Código do Gestor -->
          <div class="form-group">
            <label for="codigoGestor" class="label">Código do Gestor</label>
            <input
              id="codigoGestor"
              v-model="formData.codigoGestor"
              type="text"
              placeholder="ID do gestor responsável"
              class="input-field"
            />
          </div>
        </div>
      </div>

      <!-- Observações -->
      <div class="form-section">
        <h2 class="section-title">Observações</h2>
        
        <div class="form-group">
          <textarea
            id="observacoes"
            v-model="formData.observacoes"
            placeholder="Adicione observações sobre o colaborador (opcional)"
            class="textarea-field"
            rows="4"
          ></textarea>
        </div>
      </div>

      <!-- Mensagem de erro -->
      <div v-if="errorMessage" class="error-alert">
        {{ errorMessage }}
      </div>

      <!-- Mensagem de sucesso -->
      <div v-if="successMessage" class="success-alert">
        {{ successMessage }}
      </div>

      <!-- Botões -->
      <div class="form-actions">
        <button
          type="button"
          @click="handleCancel"
          class="btn-cancel"
          :disabled="loading"
        >
          Cancelar
        </button>
        <button
          type="submit"
          class="btn-submit"
          :disabled="loading"
        >
          <svg v-if="!loading" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
            <polyline points="17 21 17 13 7 13 7 21"/>
            <polyline points="7 3 7 8 15 8"/>
          </svg>
          <svg v-else class="spinner" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <circle cx="12" cy="12" r="10" stroke-width="4" stroke-opacity="0.25"/>
            <path d="M12 2a10 10 0 0 1 10 10" stroke-width="4" stroke-linecap="round"/>
          </svg>
          {{ loading ? 'Cadastrando...' : 'Cadastrar Colaborador' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const formData = reactive({
  nomeCompleto: '',
  email: '',
  idCracha: '',
  senhaInicial: '',
  telefone: '',
  dataAdmissao: '',
  cargo: '',
  departamento: '',
  observacoes: '',
  cpf: '',
  tituloProfissional: '',
  localizacao: '',
  codigoGestor: ''
});

const departamentos = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

// Buscar departamentos
const fetchDepartamentos = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/areas');
    departamentos.value = response.data;
  } catch (err) {
    console.error('Erro ao carregar departamentos:', err);
  }
};

onMounted(() => {
  fetchDepartamentos();
});

const handleSubmit = async () => {
  errorMessage.value = '';
  successMessage.value = '';
  loading.value = true;

  try {
    const payload = {
      nomeCompleto: formData.nomeCompleto,
      email: formData.email,
      idCracha: formData.idCracha,
      senha: formData.senhaInicial,
      telefone: formData.telefone || null,
      dataCadastro: formData.dataAdmissao || new Date().toISOString().split('T')[0],
      tituloProfissional: formData.cargo || null,
      codigoArea: formData.departamento || null,
      observacoes: formData.observacoes || null,

      cpf: formData.cpf,
      localizacao: formData.localizacao || null,
      codigoGestor: formData.codigoGestor || null,
      titulo_profissional: formData.tituloProfissional || null
    };

    const response = await axios.post('http://localhost:8080/api/funcionario', payload);
    
    successMessage.value = 'Colaborador cadastrado com sucesso!';
    
    // Redirecionar após 2 segundos
    setTimeout(() => {
      router.push({ name: 'LiderFuncionarios' });
    }, 2000);
    
  } catch (err) {
    console.error('Erro ao cadastrar colaborador:', err);
    
    if (err.response?.status === 409) {
      errorMessage.value = 'Email ou ID do crachá já cadastrado';
    } else if (err.response?.data?.message) {
      errorMessage.value = err.response.data.message;
    } else {
      errorMessage.value = 'Erro ao cadastrar colaborador. Tente novamente.';
    }
  } finally {
    loading.value = false;
  }
};

const handleCancel = () => {
  router.push({ name: 'LiderFuncionarios' });
};

// Diretiva de máscara simples (opcional, pode usar biblioteca)
const vMask = {
  mounted(el, binding) {
    el.addEventListener('input', (e) => {
      let value = e.target.value.replace(/\D/g, '');
      const mask = binding.value;
      let result = '';
      let valueIndex = 0;

      for (let i = 0; i < mask.length && valueIndex < value.length; i++) {
        if (mask[i] === '#') {
          result += value[valueIndex];
          valueIndex++;
        } else {
          result += mask[i];
        }
      }

      e.target.value = result;
    });
  }
};
</script>

<style scoped>
/* SEU CSS ORIGINAL (não alterei nada) */
.cadastro-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 32px 24px;
}

.page-header {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 32px;
  padding: 20px;
  background: #FFFFFF;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.header-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #EFF6FF;
  border-radius: 12px;
  color: #2563EB;
  flex-shrink: 0;
}

.header-text {
  flex: 1;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 4px 0;
}

.header-subtitle {
  font-size: 14px;
  color: #64748B;
  margin: 0;
}

.form-container {
  background: #FFFFFF;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.form-section {
  margin-bottom: 32px;
}

.form-section:last-of-type {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #F1F5F9;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.label {
  font-size: 14px;
  font-weight: 500;
  color: #334155;
}

.required {
  color: #EF4444;
}

.input-field,
.select-field,
.textarea-field {
  width: 100%;
  padding: 10px 14px;
  font-size: 14px;
  color: #1E293B;
  background: #FFFFFF;
  border: 1px solid #CBD5E1;
  border-radius: 8px;
  transition: all 0.2s ease;
  font-family: inherit;
}

.input-field::placeholder,
.textarea-field::placeholder {
  color: #94A3B8;
}

.input-field:focus,
.select-field:focus,
.textarea-field:focus {
  outline: none;
  border-color: #2563EB;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.select-field {
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%2364748B' d='M6 9L1 4h10z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 14px center;
  padding-right: 40px;
}

.textarea-field {
  resize: vertical;
  min-height: 100px;
}

.error-alert,
.success-alert {
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  margin-bottom: 20px;
  animation: slideDown 0.3s ease;
}

.error-alert {
  background: #FEE2E2;
  border: 1px solid #FECACA;
  color: #DC2626;
}

.success-alert {
  background: #ECFDF5;
  border: 1px solid #A7F3D0;
  color: #059669;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #F1F5F9;
}

.btn-cancel,
.btn-submit {
  height: 44px;
  padding: 0 24px;
  font-size: 15px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-cancel {
  color: #64748B;
  background: #FFFFFF;
  border: 1px solid #CBD5E1;
}

.btn-cancel:hover:not(:disabled) {
  background: #F8FAFC;
  border-color: #94A3B8;
}

.btn-submit {
  color: #FFFFFF;
  background: #2563EB;
}

.btn-submit:hover:not(:disabled) {
  background: #1D4ED8;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
  transform: translateY(-1px);
}

.btn-submit:active:not(:disabled) {
  transform: translateY(0);
}

.btn-cancel:disabled,
.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Responsividade */
@media (max-width: 768px) {
  .cadastro-page {
    padding: 20px 16px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .btn-cancel,
  .btn-submit {
    width: 100%;
    justify-content: center;
  }
}
</style>