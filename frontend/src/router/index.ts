import { createRouter, createWebHistory } from 'vue-router';
import Funcionarios from '../components/Funcionarios.vue';
import perfil from '../components/Perfil.vue';
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
      path: '/perfil',
      name: 'perfil',
      component: perfil,
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