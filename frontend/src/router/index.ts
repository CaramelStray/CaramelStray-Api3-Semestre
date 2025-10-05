import { createRouter, createWebHistory } from 'vue-router';
import Funcionarios from '../components/Funcionarios.vue';
import Perfilbase from '../components/perfilBase.vue';
import Perfilfun from '../components/perfilFun.vue';
import Inicio from '../components/Inicio.vue';
import Competencia from '../components/Competencia.vue';
import Perguntas from '../components/CadastroPerguntas.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/competencia',
    },
    {
      path: '/funcionarios',
      name: 'Funcionarios',
      component: Funcionarios,
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
      path: '/perguntas/:id',
      name: 'CadastroPerguntas',
      component: Perguntas,
      props: true
    },
  ],
});

export default router;