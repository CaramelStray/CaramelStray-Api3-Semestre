import { createRouter, createWebHistory } from 'vue-router';
import Funcionarios from '../components/Funcionarios.vue';
import Areas from '../components/Areas.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/funcionarios',
    },
    {
      path: '/funcionarios',
      name: 'Funcionarios',
      component: Funcionarios,
    },
    {
      path: '/areas',
      name: 'Areas',
      component: Areas,
    },
  ],
});

export default router;