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


const { carregarUsuarioLogado } = useAuth()
carregarUsuarioLogado(1) 


const app = createApp(App)

registerPlugins(app)


app.mount('#app')