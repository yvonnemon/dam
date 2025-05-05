import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import FlightsView from '../views/FlightsView.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  { path: '/flights', component: FlightsView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
