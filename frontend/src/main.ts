import App from './App.vue'
import { createApp } from 'vue'
import { registerPlugins } from '@/plugins'
import { useAuth } from './auth'
import axios from 'axios'


const { logout, carregarSessao } = useAuth();


axios.interceptors.response.use(
  (response) => response, 
  async (error) => {
    const status = error.response ? error.response.status : null;
    if (status === 401) {
      console.warn("Axios Interceptor: Recebido 401. Deslogando.");
      await logout();
    }
    return Promise.reject(error);
  }
);


async function startApp() {
  
  
  try {
    await carregarSessao();
  } catch (e) {
    console.error("Falha ao restaurar sessão no main.ts", e);
  }

  
  const app = createApp(App);

  
  registerPlugins(app);

  
  app.mount('#app');
}


startApp();