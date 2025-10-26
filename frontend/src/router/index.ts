import { createRouter, createWebHistory, type RouteLocation, type RouteRecordRaw } from 'vue-router';

// Layouts
import LiderLayout from '../layouts/LiderLayout.vue';
import ColaboradorLayout from '../layouts/ColaboradorLayout.vue';

// Páginas (importa TODOS os seus componentes)
import Inicio from '../components/Inicio.vue';
import Lista from '../components/Lista.vue';
import Avaliacao from '../components/Avaliacao.vue';
import Perguntas from '../components/CadastroPerguntas.vue'; // Tela de Cadastro
import Areas from '../components/Areas.vue';
import Status from '../components/Status.vue'; // Tela de Status do Líder
import Status2 from '../components/Status2.vue'; // Tela de Status do Colaborador
import Respostas from '../components/respostaAvaliacao.vue'; // Tela de Ver/Responder
import Teste from '../components/Teste.vue'; // Tela antiga de 'Criar', pode ser removida depois
import Perfilfun from '../components/perfilFun.vue';
import Competencia from '../components/Competencia.vue';
import Perfilbase from '../components/perfilBase.vue';

// Componente temporário para o dashboard
const DashboardPage = { template: '<div><h1>Página de Dashboard</h1></div>' };

const routes: RouteRecordRaw[] = [
  // --- ROTAS DO LÍDER ---
  {
    path: '/lider',
    component: LiderLayout,
    redirect: '/lider/inicio',
    children: [
      {
        path: 'inicio',
        name: 'LiderInicio', component: Inicio,
        meta: { title: 'Sistema Corporativo', subtitle: 'Painel de Liderança' }
      },
      {
        path: 'funcionarios',
        name: 'LiderFuncionarios', component: Lista, // Note: Usa 'Lista', não 'Funcionarios'
        meta: { title: 'Lista de Colaboradores', subtitle: 'Visualizar e filtrar equipe' }
      },
      {
        path: 'avaliacoes',
        name: 'LiderAvaliacoes', component: Avaliacao,
        props: {
          createHref: { name: 'LiderPerguntas' },
          statusHref: { name: 'LiderStatusAvaliacoes' }
        },
        meta: { title: 'Sistema de Avaliações', subtitle: 'Criar e Acompanhar Avaliações' }
      },
      {
        path: 'dashboard',
        name: 'LiderDashboard', component: DashboardPage,
        meta: { title: 'Dashboard', subtitle: 'Visão Geral' }
      },
      {
        path: 'perguntas/', // Tela de Cadastro/Gerenciamento de Perguntas
        name: 'LiderPerguntas', component: Perguntas,
        meta: { title: 'Cadastro de Perguntas', subtitle: 'Gerenciar perguntas globais' }
      },
      {
        path: 'areas',
        name: 'LiderAreas', component: Areas,
        meta: { title: 'Gerenciamento de Áreas', subtitle: 'Visualizar e cadastrar áreas' }
      },
      {
        path: 'avaliacoes/status',
        name: 'LiderStatusAvaliacoes', component: Status, // Líder usa Status.vue
        meta: { title: 'Status das Avaliações', subtitle: 'Acompanhamento de progresso (Líder)' }
      },
      {
        path: 'avaliacoes/respostas/:avaliacaoId?', // Tela para o Líder VER respostas
        name: 'LiderVerRespostas', component: Respostas, props: true,
        meta: { title: 'Respostas da Avaliação', subtitle: 'Visualizar respostas (Líder)' }
      },
      {
        path: 'teste', // Rota antiga
        name: 'LiderTeste', component: Teste, props: true,
        meta: { title: 'Criar Avaliação (Teste)', subtitle: 'Componente antigo' }
      },
      {
        path: 'perfil/:id',
        name: 'LiderVerPerfil', component: Perfilfun, props: true,
        meta: { title: 'Perfil do Colaborador', subtitle: 'Informações detalhadas' }
      },
      {
        path: 'perfil/:id/competencia',
        name: 'LiderVerCompetencias', component: Competencia, props: true,
        meta: { title: 'Gerenciar Competências', subtitle: 'Atribuir ou remover habilidades' }
      },
      {
        path: 'status2', // Mantida por segurança
        name: 'LiderStatus2', component: Status2,
        meta: { title: 'Status 2', subtitle: 'Página de Teste 2' }
      }
    ]
  },

  // --- ROTAS DO COLABORADOR ---
  {
    path: '/colaborador',
    component: ColaboradorLayout,
    redirect: '/colaborador/perfil/base',
    children: [
      {
        path: 'perfil/base',
        name: 'ColaboradorPerfilBase', component: Perfilbase,
        meta: { title: 'Meu Perfil', subtitle: 'Informações Pessoais' }
      },
      {
        path: 'perfil/:id',
        name: 'ColaboradorPerfil', component: Perfilfun, props: true,
        meta: { title: 'Meu Perfil', subtitle: 'Gerencie suas informações' }
      },
      {
        path: 'perfil/:id/competencia',
        name: 'ColaboradorCompetencias', component: Competencia, props: true,
        meta: { title: 'Minhas Competências', subtitle: 'Gerencie suas habilidades' }
      },
      {
        path: 'status',
        name: 'ColaboradorStatus', component: Status2, // Colaborador usa Status2.vue
        meta: { title: 'Minhas Avaliações', subtitle: 'Acompanhe suas avaliações pendentes' }
      },
      {
        path: 'responder/:avaliacaoId', // Tela para o Colaborador RESPONDER
        name: 'ColaboradorResponderAvaliacao', component: Respostas, props: true,
        meta: { title: 'Responder Avaliação', subtitle: 'Forneça suas respostas' }
      },
    ]
  },

  // --- REDIRECIONAMENTOS INICIAIS ---
  // Esta seção cuida das rotas do seu PRIMEIRO arquivo
  { path: '/', redirect: '/lider/inicio' },
  { path: '/inicio', redirect: { name: 'LiderInicio' }},
  { path: '/perfilbase', redirect: { name: 'ColaboradorPerfilBase' }},
  { path: '/perfil/:id', redirect: (to: RouteLocation) => ({ name: 'ColaboradorPerfil', params: { id: to.params.id }}) },
  { path: '/funcionarios', redirect: { name: 'LiderFuncionarios' } },
  { path: '/lista', redirect: { name: 'LiderFuncionarios' } }, // Corrigido: 'LiderLista' não existe
  { path: '/perguntas', redirect: { name: 'LiderPerguntas' } }, // Redireciona acesso direto
  { path: '/areas', redirect: { name: 'LiderAreas' } },
  { path: '/avaliacao', redirect: { name: 'LiderAvaliacoes' } },
  { path: '/status', redirect: { name: 'ColaboradorStatus' } },
  { path: '/status2', redirect: { name: 'ColaboradorStatus' } },
  
  // Rota antiga de perguntas com ID, redireciona para a nova sem ID
  { path: '/perguntas/:id', redirect: { name: 'LiderPerguntas' } },
  // Rota antiga de teste com ID, redireciona para a nova
  { path: '/teste/:instanciaid', redirect: { name: 'LiderTeste' } }, 
  // Rota antiga de respostas
  { path: '/respostas', redirect: { name: 'LiderVerRespostas' } },

  // --- ROTA CATCH-ALL ---
  // Pega qualquer outra URL e redireciona para a página inicial
  { path: '/:pathMatch(.*)*', redirect: '/' }

];

const router = createRouter({
  history: createWebHistory(),
  routes,
  linkActiveClass: 'active',
  linkExactActiveClass: 'active',
});

export default router;