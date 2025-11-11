<template>
  <div class="login-page">
    <div class="login-card">
      
      <div class="logo-container">
        <img src="../assets/logo.png" ></img>
      </div>

      
      <h1 class="title">Acesso ao Sistema</h1>
      <p class="subtitle">Entre com suas credenciais para acessar o sistema</p>

      
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>

      
      <form @submit.prevent="handleLogin" class="login-form">
        
        <div class="form-group">
          <label for="email" class="label">Email</label>
          <div class="input-container">
            <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="2" y="4" width="20" height="16" rx="2"/>
              <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/>
            </svg>
            <input
              id="email"
              v-model="formData.email"
              type="email"
              placeholder="Digite seu email"
              class="input-field"
            />
          </div>
        </div>

        
        <div class="form-group">
          <label for="senha" class="label">Senha</label>
          <div class="input-container">
            <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
            </svg>
            <input
              id="senha"
              v-model="formData.senha"
              :type="showPassword ? 'text' : 'password'"
              placeholder="••••••••"
              class="input-field password-field"
            />
            <button
              type="button"
              @click="togglePassword"
              class="toggle-password"
              tabindex="-1"
            >
              <svg v-if="!showPassword" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                <line x1="1" y1="1" x2="23" y2="23"/>
              </svg>
            </button>
          </div>
        </div>
        
        
        <button type="submit" class="btn-entrar" :disabled="loading">
          <span v-if="!loading">Entrar</span>
          <span v-else class="loading">
            <svg class="spinner" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <circle cx="12" cy="12" r="10" stroke-width="4" stroke-opacity="0.25"/>
              <path d="M12 2a10 10 0 0 1 10 10" stroke-width="4" stroke-linecap="round"/>
            </svg>
            Entrando...
          </span>
        </button>

        
        <div class="forgot-password">
          <a href="#" @click.prevent="handleForgotPassword" class="forgot-link">
            Esqueceu sua senha?
          </a>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useAuth } from '../auth'; 


const { login } = useAuth(); 

const formData = reactive({
  email: '',
  password: '' 
});

const showPassword = ref(false);
const loading = ref(false);
const errorMessage = ref('');

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};


const handleLogin = async () => {
  errorMessage.value = '';
  loading.value = true;

  try {
    
    await login(formData.email, formData.senha);
    
  } catch (err) {
    
    console.error('Erro no login:', err);
    errorMessage.value = err.message || 'Erro desconhecido.';
  } finally {
    loading.value = false;
  }
};

const handleForgotPassword = () => {
  console.log('Recuperar senha');
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.login-page {
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #E8EDF2;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: #FFFFFF;
  border-radius: 16px;
  padding: 48px 40px 40px 40px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 32px;
}

.logo-container img {
    width: 50px;
}

.title {
  font-size: 24px;
  font-weight: 600;
  color: #1E3A8A;
  text-align: center;
  margin: 0 0 8px 0;
  line-height: 1.2;
}

.subtitle {
  font-size: 14px;
  color: #64748B;
  text-align: center;
  margin: 0 0 32px 0;
  line-height: 1.5;
}

.error-message {
  margin-bottom: 20px;
  padding: 12px 16px;
  background: #FEE2E2;
  border: 1px solid #FECACA;
  border-radius: 8px;
  color: #DC2626;
  font-size: 14px;
  text-align: center;
  animation: slideDown 0.3s ease;
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

.login-form {
  display: flex;
  flex-direction: column;
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
  color: #1E3A8A;
  display: block;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.input-icon {
  position: absolute;
  left: 14px;
  color: #2563EB;
  pointer-events: none;
  z-index: 1;
}

.input-field {
  width: 100%;
  height: 48px;
  padding: 0 14px 0 44px;
  font-size: 14px;
  color: #1E293B;
  background: #FFFFFF;
  border: 1px solid #CBD5E1;
  border-radius: 8px;
  transition: all 0.2s ease;
  font-family: inherit;
}

.input-field.password-field {
  padding-right: 44px;
}

.input-field::placeholder {
  color: #94A3B8;
}

.input-field:focus {
  outline: none;
  border-color: #2563EB;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.toggle-password {
  position: absolute;
  right: 14px;
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #64748B;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s;
  z-index: 1;
}

.toggle-password:hover {
  color: #2563EB;
}

.toggle-password:focus {
  outline: none;
}

.info-text {
  font-size: 12px;
  color: #64748B;
  text-align: center;
  margin: 4px 0 0 0;
  line-height: 1.5;
}

.btn-entrar {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
  background: #2563EB;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 8px;
  font-family: inherit;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-entrar:hover:not(:disabled) {
  background: #1D4ED8;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
  transform: translateY(-1px);
}

.btn-entrar:active:not(:disabled) {
  transform: translateY(0);
}

.btn-entrar:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading {
  display: flex;
  align-items: center;
  gap: 8px;
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

.forgot-password {
  text-align: center;
  margin-top: 8px;
}

.forgot-link {
  font-size: 13px;
  color: #6366F1;
  text-decoration: none;
  transition: color 0.2s;
  display: inline-block;
}

.forgot-link:hover {
  color: #4F46E5;
  text-decoration: underline;
}

/* Responsividade */
@media (max-width: 480px) {
  .login-card {
    padding: 40px 28px 32px 28px;
  }

  .title {
    font-size: 22px;
  }

  .subtitle {
    font-size: 13px;
  }

  .input-field {
    font-size: 16px; 
  }
}
</style>