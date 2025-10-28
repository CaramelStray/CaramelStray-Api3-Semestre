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

// --- LÓGICA DE INICIALIZAÇÃO ---

// 1. Simulação de Login (Ainda útil para popular dados nos menus):
const { carregarUsuarioLogado } = useAuth()
// ==========================================================
// ALTERE O NÚMERO PARA TESTAR DADOS DE USUÁRIO DIFERENTES:
// 1 = Usuário Líder (provavelmente)
// 2 = Usuário Colaborador (provavelmente - confirme no BD!)
carregarUsuarioLogado(82); // <<<< Define QUEM está logado (para dados no menu)
// ==========================================================
// A URL acessada (/lider/* ou /colaborador/*) definirá QUAL layout/menu aparece.

// 2. Cria a aplicação Vue
const app = createApp(App)

// 3. Registra TODOS os plugins (Vuetify, Router, etc.) ANTES de montar.
registerPlugins(app) // Isso já inclui o 'router'

// 4. Monta a aplicação.
app.mount('#app')
