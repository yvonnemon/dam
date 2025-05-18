import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import FlightsView from '../views/FlightsView.vue';
import AdminPanel from '../views/AdminPanel.vue';
import RegisterView from '../views/RegisterView.vue';
import BookingView from '../views/BookingView.vue';
import { jwtDecode } from "jwt-decode";
import NotFound from '../components/NotFound.vue';


const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView, name: 'Login' },
  { path: '/bookings', component: BookingView, name: 'Bookings', meta: { requiresAuth: true, roles: ['ADMIN','USER'] }, },
  { path: '/flights', component: FlightsView, name: 'Flights', meta: { requiresAuth: true, roles: ['ADMIN','USER'] }, },
  { path: '/admin', component: AdminPanel, name: 'Admin', meta: { requiresAuth: true, roles: ['ADMIN'] }, },
  { path: '/register', component: RegisterView, name: 'Register' },
   {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('token');

  if (to.meta.requiresAuth) {
    if (!token) {
      return next({ name: 'Login' });
    }

    try {
      const decodedToken = jwtDecode(token);
      const userRole = decodedToken.role;
      const allowedRoles = to.meta.roles || [];

      if (!allowedRoles.includes(userRole)) {
        return next({ name: 'NotFound' }); // ✅ redirect to 404 if role not allowed
      }
    } catch (error) {
      console.error('Error decoding JWT token:', error);
      return next({ name: 'NotFound' });
    }
  }

  next(); // ✅ allow access if no auth required or role is valid
});

export default router;
