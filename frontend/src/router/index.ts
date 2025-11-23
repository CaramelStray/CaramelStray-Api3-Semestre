import { createRouter, createWebHistory, type RouteLocation, type RouteRecordRaw } from 'vue-router';

// Layouts (Mantemos a importação relativa)
import LiderLayout from '../layouts/LiderLayout.vue';
import ColaboradorLayout from '../layouts/ColaboradorLayout.vue';
import SupervisorLayout from '../layouts/SupervisorLayout.vue';

// Páginas (USANDO ALIAS @/ E OS NOMES DOS SEUS ARQUIVOS)
import InicioSupervisor from '@/components/InicioSupervisor.vue';
import Inicio from '@/components/Inicio.vue';
import Lista from '@/components/Lista.vue';
import Avaliacao from '@/components/Avaliacao.vue';
import Perguntas from '@/components/CadastroPerguntas.vue';
import Areas from '@/components/Areas.vue';
import Status from '@/components/Status.vue';
import Status2 from '@/components/Status2.vue';
import Respostas from '@/components/respostaAvaliacao.vue';
import Teste from '@/components/Teste.vue';
import Perfilfun from '@/components/perfilFun.vue';
import Competencia from '@/components/Competencia.vue';
import Perfilbase from '@/components/perfilBase.vue';
import CadastroColab from '@/components/CadastroColaborador.vue';

// === CORREÇÕES DE NOME AQUI ===
import Login from '@/components/login.vue';          // <--- Usa o seu arquivo "login.vue"
import Dashboard from '@/components/Dashboard.vue';  // <--- Usa o seu arquivo "Dashboard.vue"
// =============================


const routes: RouteRecordRaw[] = [
  // --- ROTA DE LOGIN (Ponto de entrada) ---
  {
    path: '/login',
    name: 'Login', component: Login, // Usa o componente importado com o nome Login
    meta: { title: 'Acesso', subtitle: 'Faça seu login' }
  },

  // --- ROTAS DO LÍDER ---
  {
    path: '/lider',
    component: LiderLayout,
    redirect: '/lider/inicio',
    children: [
      {
        path: 'inicio', name: 'LiderInicio', component: Inicio,
        meta: { title: 'Sistema Corporativo', subtitle: 'Painel de Liderança' }
      },
      {
        path: 'funcionarios', name: 'LiderFuncionarios', component: Lista,
        meta: { title: 'Lista de Colaboradores', subtitle: 'Visualizar e filtrar equipe' }
      },
      {
        path: 'cadastrar', name: 'LiderCadastrarColaborador', component: CadastroColab,
        meta: { title: 'Novo Colaborador', subtitle: 'Cadastro de Usuários' }
      },
      {
        path: 'avaliacoes', name: 'LiderAvaliacoes', component: Avaliacao,
        props: { createHref: { name: 'LiderPerguntas' }, statusHref: { name: 'LiderStatusAvaliacoes' } },
        meta: { title: 'Sistema de Avaliações', subtitle: 'Criar e Acompanhar Avaliações' }
      },
      {
        path: 'dashboard', name: 'LiderDashboard', component: Dashboard, // <--- Componente Dashboard
        meta: { title: 'Dashboard', subtitle: 'Visão Geral' }
      },
      {
        path: 'perguntas/', name: 'LiderPerguntas', component: Perguntas,
        meta: { title: 'Cadastro de Perguntas', subtitle: 'Gerenciar perguntas globais' }
      },
      {
        path: 'areas', name: 'LiderAreas', component: Areas,
        meta: { title: 'Gerenciamento de Áreas', subtitle: 'Visualizar e cadastrar áreas' }
      },
      {
        path: 'avaliacoes/status', name: 'LiderStatusAvaliacoes', component: Status,
        meta: { title: 'Status das Avaliações', subtitle: 'Acompanhamento de progresso (Líder)' }
      },
      {
        path: 'avaliacoes/respostas/:avaliacaoId?', name: 'LiderVerRespostas', component: Respostas, props: true,
        meta: { title: 'Respostas da Avaliação', subtitle: 'Visualizar respostas (Líder)' }
      },
      {
        path: 'teste', name: 'LiderTeste', component: Teste, props: true,
        meta: { title: 'Criar Avaliação (Teste)', subtitle: 'Componente antigo' }
      },
      {
        path: 'perfil/:id', name: 'LiderVerPerfil', component: Perfilfun, props: true,
        meta: { title: 'Perfil do Colaborador', subtitle: 'Informações detalhadas' }
      },
      {
        path: 'perfil/:id/competencia', name: 'LiderVerCompetencias', component: Competencia, props: true,
        meta: { title: 'Gerenciar Competências', subtitle: 'Atribuir ou remover habilidades' }
      },
      {
        path: 'status2', name: 'LiderStatus2', component: Status2,
        meta: { title: 'Status 2', subtitle: 'Página de Teste 2' }
      }
    ]
  },

  // --- ROTAS DO SUPERVISOR (BLOCO DEDICADO) ---
  {
    path: '/supervisor',
    component: SupervisorLayout,
    redirect: '/supervisor/inicio',
    children: [
      {
        path: 'inicio', name: 'SupervisorInicio', component: InicioSupervisor,
        meta: { title: 'Sistema Corporativo', subtitle: 'Painel de Supervisão' }
      },
      {
        path: 'funcionarios', name: 'SupervisorFuncionarios', component: Lista,
        meta: { title: 'Lista de Colaboradores', subtitle: 'Visualizar e filtrar equipe' }
      },
      {
        path: 'avaliacoes', name: 'SupervisorAvaliacoes', component: Avaliacao,
        props: { createHref: { name: 'SupervisorPerguntas' }, statusHref: { name: 'SupervisorStatusAvaliacoes' } },
        meta: { title: 'Sistema de Avaliações', subtitle: 'Criar e Acompanhar Avaliações' }
      },
      {
        path: 'dashboard', name: 'SupervisorDashboard', component: Dashboard, // <--- Componente Dashboard
        meta: { title: 'Dashboard', subtitle: 'Visão Geral (Supervisor)' }
      },
      {
        path: 'perguntas/', name: 'SupervisorPerguntas', component: Perguntas,
        meta: { title: 'Cadastro de Perguntas', subtitle: 'Gerenciar perguntas globais' }
      },
      {
        path: 'areas', name: 'SupervisorAreas', component: Areas,
        meta: { title: 'Gerenciamento de Áreas', subtitle: 'Visualizar e cadastrar áreas' }
      },
      {
        path: 'avaliacoes/status', name: 'SupervisorStatusAvaliacoes', component: Status,
        meta: { title: 'Status das Avaliações', subtitle: 'Acompanhamento de progresso (Supervisor)' }
      },
      {
        path: 'avaliacoes/respostas/:avaliacaoId?', name: 'SupervisorVerRespostas', component: Respostas, props: true,
        meta: { title: 'Respostas da Avaliação', subtitle: 'Visualizar respostas (Supervisor)' }
      },
      {
        path: 'perfil/:id', name: 'SupervisorVerPerfil', component: Perfilfun, props: true,
        meta: { title: 'Perfil do Colaborador', subtitle: 'Informações detalhadas' }
      },
      {
        path: 'perfil/:id/competencia', name: 'SupervisorVerCompetencias', component: Competencia, props: true,
        meta: { title: 'Gerenciar Competências', subtitle: 'Atribuir ou remover habilidades' }
      },
    ]
  },

  // --- ROTAS DO COLABORADOR ---
  {
    path: '/colaborador',
    component: ColaboradorLayout,
    redirect: '/colaborador/perfil/base',
    children: [
      {
        path: 'perfil/base', name: 'ColaboradorPerfilBase', component: Perfilbase,
        meta: { title: 'Meu Perfil', subtitle: 'Informações Pessoais' }
      },
      {
        path: 'perfil/:id', name: 'ColaboradorPerfil', component: Perfilfun, props: true,
        meta: { title: 'Meu Perfil', subtitle: 'Gerencie suas informações' }
      },
      {
        path: 'perfil/:id/competencia', name: 'ColaboradorCompetencias', component: Competencia, props: true,
        meta: { title: 'Minhas Competências', subtitle: 'Gerencie suas habilidades' }
      },
      {
        path: 'status', name: 'ColaboradorStatus', component: Status2,
        meta: { title: 'Minhas Avaliações', subtitle: 'Acompanhe suas avaliações pendentes' }
      },
      {
        path: 'responder/:avaliacaoId', name: 'ColaboradorResponderAvaliacao', component: Teste, props: true,
        meta: { title: 'Responder Avaliação', subtitle: 'Forneça suas respostas' }
      },
    ]
  },

  // --- REDIRECIONAMENTOS INICIAIS ---
  { path: '/', redirect: '/login' },
  { path: '/inicio', redirect: { name: 'LiderInicio' }},
  { path: '/perfilbase', redirect: { name: 'ColaboradorPerfilBase' }},
  { path: '/perfil/:id', redirect: (to: RouteLocation) => ({ name: 'ColaboradorPerfil', params: { id: to.params.id }}) },
  { path: '/funcionarios', redirect: { name: 'LiderFuncionarios' } },
  { path: '/lista', redirect: { name: 'LiderFuncionarios' } },
  { path: '/perguntas', redirect: { name: 'LiderPerguntas' } },
  { path: '/areas', redirect: { name: 'LiderAreas' } },
  { path: '/avaliacao', redirect: { name: 'LiderAvaliacoes' } },
  { path: '/status', redirect: { name: 'ColaboradorStatus' } },
  { path: '/status2', redirect: { name: 'ColaboradorStatus' } },
  { path: '/perguntas/:id', redirect: { name: 'LiderPerguntas' } },
  {
    path: '/teste/:instanciaid',
    redirect: (to) => {
      return { name: 'LiderTeste', params: { instanciaid: to.params.instanciaid } };
    }
  },
  { path: '/respostas', redirect: { name: 'LiderVerRespostas' } },

  // --- ROTA CATCH-ALL ---
  { path: '/:pathMatch(.*)*', redirect: '/' }

];

const router = createRouter({
  history: createWebHistory(),
  routes,
  linkActiveClass: 'active',
  linkExactActiveClass: 'active',
});

export default router;
