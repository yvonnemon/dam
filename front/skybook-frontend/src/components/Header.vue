<template>
  <header class="site-header">
    <div class="header-top">
      <router-link to="/flights" class="link-home"><h1 class="logo">✈️ SkyBook</h1></router-link>
      <button class="menu-toggle" @click="isMenuOpen = !isMenuOpen">
        <span class="material-symbols-outlined">menu</span>
      </button>
    </div>

    <nav class="nav-links" :class="{ open: isMenuOpen }">
      <router-link to="/flights">{{ t('flights') }}</router-link>
      <router-link to="/bookings">{{ t('bookings') }}</router-link>
      <router-link v-if="isAdmin()" to="/register">{{ t('register') }}</router-link>
      <router-link v-if="isAdmin()" to="/admin">{{ t('admin') }}</router-link>
      <button @click="logout()">{{ t('logout') }}</button>
    </nav>
  </header>
</template>

  
<style scoped>

.site-header {
  background-color: #0a3b6c;
  color: white;
  width: 100%;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.25rem 10dvw;
}


  a:link {
    text-decoration: none;
    color: white;
  }

  a:visited {
    text-decoration: none;
    color: white;
  }

  a:hover {
    text-decoration: none;
    color: white;
  }

  a:active {
    text-decoration: none;
    color: white;
  }


.logo {
  text-decoration: none;
  font-size: 25px;
  font-weight: bold;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;

}

.menu-toggle {
  display: none;
  background: none;
  border: none;
  color: white;
  font-size: 26px;
  cursor: pointer;
}

.nav-links {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 18px;
  padding: 0 10dvw 1rem;
}

.nav-links a {
  color: white;
  text-decoration: none;
  font-weight: 500;
  padding: 6px 10px;
  border-radius: 4px;
  transition: background 0.2s;
}

.nav-links a:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.nav-links button {
  background-color: white;
  color: #1e90ff;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.nav-links button:hover {
  background-color: #f1f1f1;
}


@media (max-width: 768px) {
  .menu-toggle {
    display: block;
  }

  .nav-links {
    display: none;
    flex-direction: column;
    align-items: flex-start;
    width: 100%;
    background-color: #0a3b6c;
    padding: 10px;
    gap: 10px;
  }

  .nav-links.open {
    display: flex;
  }

  .nav-links a,
  .nav-links button {
    width: 100%;
    text-align: left;
    padding: 10px;
  }
}
</style>

<script setup>
  import { ref, onMounted, nextTick } from 'vue';
  import { useRouter } from 'vue-router';
  import { useI18n } from 'vue-i18n';
  import { jwtDecode } from 'jwt-decode';

  const { t } = useI18n();
  const router = useRouter();
  const isMenuOpen = ref(false);

  const logout = () => {
    sessionStorage.removeItem('token');
    router.push({ name: 'Login' }); // Redirect to login
  };
  const token = sessionStorage.getItem('token');

  const isAdmin = () => {
    const decodedToken = jwtDecode(token);
    const userRole = decodedToken.role;
    return userRole === 'ADMIN';
  };

</script>
  