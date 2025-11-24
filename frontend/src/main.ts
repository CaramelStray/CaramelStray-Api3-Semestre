// Conteúdo de frontend/src/main.ts

/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from './App.vue'
// Composables
import { createApp } from 'vue'
// Plugins
import { registerPlugins } from '@/plugins'
// Auth (para simular usuário nos menus/componentes)
import { useAuth } from './auth'
// Importa o router por causa do seu uso no auth.ts/outros
import router from './router';


// --- LÓGICA DE INICIALIZAÇÃO ---

// 1. Inicia o uso do sistema de autenticação
const { carregarSessao } = useAuth()

// 2. Cria a aplicação Vue
const app = createApp(App)

// 3. Registra TODOS os plugins
registerPlugins(app)

// 4. Monta a aplicação APÓS O CARREGAMENTO DE SESSÃO
carregarSessao().finally(() => {
  app.mount('#app');
});
