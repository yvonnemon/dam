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

// Global route guard
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token'); // Assuming the JWT is stored in localStorage
  if (to.meta.requiresAuth) {
    // Check if token exists
    if (!token) {
      return next({ name: 'Login' });
    }

    try {
      // Decode the JWT token to extract the role
      const decodedToken = jwtDecode(token);
      const userRole = decodedToken.role; // Assuming your token has a `role` field

      // Check if the user's role is allowed for this route
      const allowedRoles = to.meta.roles;
      if (!allowedRoles.includes(userRole)) {
        return next({ name: 'Admin' });
      }
    } catch (error) {
      console.error('Error decoding JWT token:', error);
      return next({ name: 'NotFound' });
    }
  }

  // If everything is fine, allow navigation
  next();
});

export default router;
