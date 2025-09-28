import { createRouter, createWebHistory } from 'vue-router';
import Funcionarios from '../components/Funcionarios.vue';
import Perfilbase from '../components/perfilBase.vue';
import Perfilfun from '../components/perfilFun.vue';
import Inicio from '../components/Inicio.vue';
import Competencia from '../components/Competencia.vue';

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
      path: '/perfil',
      name: 'perfilfun',
      component: Perfilfun,
    },
    {
      path: '/inicio',
      name: 'Inicio',
      component: Inicio,
    },
    {
      path: '/competencia',
      name: 'Competencia',
      component: Competencia,
    },
  ],
});

export default router;