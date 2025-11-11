import { createRouter, createWebHistory, type RouteLocation, type RouteRecordRaw } from 'vue-router';
// import { useAuth } from '../auth'; // Não precisamos do useAuth aqui

// Layouts
import LiderLayout from '../layouts/LiderLayout.vue';
import ColaboradorLayout from '../layouts/ColaboradorLayout.vue';
import Login from '../components/Login.vue'; 

// Páginas
import Inicio from '../components/Inicio.vue';
import Lista from '../components/Lista.vue';
import Avaliacao from '../components/Avaliacao.vue';
import Perguntas from '../components/CadastroPerguntas.vue'; 
import Areas from '../components/Areas.vue';
import Status from '../components/Status.vue'; 
import Status2 from '../components/Status2.vue'; 
import Respostas from '../components/respostaAvaliacao.vue'; 
import Teste from '../components/Teste.vue'; 
import Perfilfun from '../components/perfilFun.vue';
import Competencia from '../components/Competencia.vue';
import Perfilbase from '../components/perfilBase.vue';

const DashboardPage = { template: '<div><h1>Página de Dashboard</h1></div>' };

const routes: RouteRecordRaw[] = [
  // --- ROTA DE LOGIN PÚBLICA ---
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { 
      title: 'Login', 
      public: true
    }
  },

  // --- ROTAS DO LÍDER (PROTEGIDAS) ---
  {
    path: '/lider',
    component: LiderLayout,
    redirect: '/lider/inicio',
    meta: { requiresAuth: true },
    children: [
      { path: 'inicio', name: 'LiderInicio', component: Inicio, meta: { title: 'Sistema Corporativo', subtitle: 'Painel de Liderança' } },
      { path: 'funcionarios', name: 'LiderFuncionarios', component: Lista, meta: { title: 'Lista de Colaboradores', subtitle: 'Visualizar e filtrar equipe' } },
      { path: 'avaliacoes', name: 'LiderAvaliacoes', component: Avaliacao, props: { createHref: { name: 'LiderPerguntas' }, statusHref: { name: 'LiderStatusAvaliacoes' } }, meta: { title: 'Sistema de Avaliações', subtitle: 'Criar e Acompanhar Avaliações' } },
      { path: 'dashboard', name: 'LiderDashboard', component: DashboardPage, meta: { title: 'Dashboard', subtitle: 'Visão Geral' } },
      { path: 'perguntas/', name: 'LiderPerguntas', component: Perguntas, meta: { title: 'Cadastro de Perguntas', subtitle: 'Gerenciar perguntas globais' } },
      { path: 'areas', name: 'LiderAreas', component: Areas, meta: { title: 'Gerenciamento de Áreas', subtitle: 'Visualizar e cadastrar áreas' } },
      { path: 'avaliacoes/status', name: 'LiderStatusAvaliacoes', component: Status, meta: { title: 'Status das Avaliações', subtitle: 'Acompanhamento de progresso (Líder)' } },
      { path: 'avaliacoes/respostas/:avaliacaoId?', name: 'LiderVerRespostas', component: Respostas, props: true, meta: { title: 'Respostas da Avaliação', subtitle: 'Visualizar respostas (Líder)' } },
      { path: 'teste', name: 'LiderTeste', component: Teste, props: true, meta: { title: 'Criar Avaliação (Teste)', subtitle: 'Componente antigo' } },
      { path: 'perfil/:id', name: 'LiderVerPerfil', component: Perfilfun, props: true, meta: { title: 'Perfil do Colaborador', subtitle: 'Informações detalhadas' } },
      { path: 'perfil/:id/competencia', name: 'LiderVerCompetencias', component: Competencia, props: true, meta: { title: 'Gerenciar Competências', subtitle: 'Atribuir ou remover habilidades' } },
      { path: 'status2', name: 'LiderStatus2', component: Status2, meta: { title: 'Status 2', subtitle: 'Página de Teste 2' } }
    ]
  },

  // --- ROTAS DO COLABORADOR (PROTEGIDAS) ---
  {
    path: '/colaborador',
    component: ColaboradorLayout,
    redirect: '/colaborador/perfil/base', 
    meta: { requiresAuth: true }, 
    children: [
      { path: 'perfil/base', name: 'ColaboradorPerfilBase', component: Perfilbase, meta: { title: 'Meu Perfil', subtitle: 'Informações Pessoais' } },
      { path: 'perfil/:id', name: 'ColaboradorPerfil', component: Perfilfun, props: true, meta: { title: 'Meu Perfil', subtitle: 'Gerencie suas informações' } },
      { path: 'perfil/:id/competencia', name: 'ColaboradorCompetencias', component: Competencia, props: true, meta: { title: 'Minhas Competências', subtitle: 'Gerencie suas habilidades' } },
      { path: 'status', name: 'ColaboradorStatus', component: Status2, meta: { title: 'Minhas Avaliações', subtitle: 'Acompanhe suas avaliações pendentes' } },
      { path: 'responder/:avaliacaoId', name: 'ColaboradorResponderAvaliacao', component: Teste, props: true, meta: { title: 'Responder Avaliação', subtitle: 'Forneça suas respostas' } },
    ]
  },

  // --- REDIRECIONAMENTOS ---
  { path: '/', redirect: '/login' },
  { path: '/:pathMatch(.*)*', redirect: '/login' }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  // !!! CORREÇÃO AQUI !!!
  // As linhas 'linkActiveClass' e 'linkExactActiveClass' foram REMOVIDAS.
});

// !!! GUARDA DE ROTA GLOBAL (Corrigida para TypeScript) !!!
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const rotaRequerAuth = to.matched.some(record => record.meta.requiresAuth);
  const rotaPublica = to.meta.public;

  if (rotaRequerAuth && !token) {
    console.log("Guarda de Rota: Acesso negado. Redirecionando para /login");
    next('/login'); // Usa o caminho (path)
  } else if (rotaPublica && token) {
    console.log("Guarda de Rota: Usuário já logado. Redirecionando para Início");
    next('/lider/inicio'); // Usa o caminho (path)
  } else {
    next();
  }
});

export default router;