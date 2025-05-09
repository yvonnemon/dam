import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import FlightsView from '../views/FlightsView.vue';
import AdminPanel from '../views/AdminPanel.vue';
import RegisterView from '../views/RegisterView.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  { path: '/flights', component: FlightsView },
  { path: '/admin', component: AdminPanel },
  { path: '/register', component: RegisterView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
