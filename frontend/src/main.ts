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

// +++ NOSSAS IMPORTAÇÕES (Router e Auth) +++
import router from './router'
import { useAuth } from './auth'


// --- LÓGICA DE INICIALIZAÇÃO ---

// 1. Inicia a simulação de login PRIMEIRO.
const { carregarUsuarioLogado } = useAuth()
carregarUsuarioLogado(1) // <-- Lembre-se de mudar aqui para testar com outros usuários

// 2. Cria a aplicação Vue
const app = createApp(App)

// 3. Registra TODOS os plugins (Vuetify, Router, etc.) ANTES de montar.
//    A função registerPlugins já deve estar cuidando do router e do vuetify.
registerPlugins(app)

// 4. Monta a aplicação. Este deve ser o ÚLTIMO passo.
app.mount('#app')