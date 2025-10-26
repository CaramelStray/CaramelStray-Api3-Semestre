import { createRouter, createWebHistory } from 'vue-router';
import Funcionarios from '../components/Funcionarios.vue';
import Perfilbase from '../components/perfilBase.vue';
import Perfilfun from '../components/perfilFun.vue';
import Inicio from '../components/Inicio.vue';
import Competencia from '../components/Competencia.vue';
import Perguntas from '../components/CadastroPerguntas.vue';
import Lista from '../components/Lista.vue';
import Avaliacao from '../components/Avaliacao.vue';
import Teste from '../components/Teste.vue';
import Status from '../components/Status.vue';
import Respostas from '../components/respostaAvaliacao.vue';
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/competencia',
    },
    {
      path: '/respostas',
      name: 'Respostas',
      component: Respostas,
    },
    {
      path: '/funcionarios',
      name: 'Funcionarios',
      component: Funcionarios,
    },
    {
      path: '/status',
      name: 'Status',
      component: Status,
    },
    {
      path: '/teste',
      name: 'Teste',
      component: Teste,
      props:true
    },
    {
      path: '/perfilbase',
      name: 'perfilbase',
      component: Perfilbase,
    },
    {
      path: '/perfil/:id',
      name: 'perfilfun',
      component: Perfilfun,
      props:true
    },
    {
      path: '/inicio',
      name: 'Inicio',
      component: Inicio,
    },
    {
      path: '/perfil/:id/competencia',
      name: 'Competencias',
      component: Competencia,
      props:true
    },
    {
      path: '/avaliacao',
      name: 'Avaliacao',
      component: Avaliacao,
      props:true
    },
    {
      path: '/perguntas',
      name: 'CadastroPerguntas',
      component: Perguntas,
      props: true
    },
    {
      path: '/lista',
      name: 'Lista',
      component: Lista,
    },
  ],
});

export default router;