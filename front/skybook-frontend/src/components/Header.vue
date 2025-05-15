<template>
    <header class="site-header">
      <h1 class="logo">✈️ SkyBook </h1>
      <nav class="nav-links">
        <router-link to="/flights"> {{ t('flights') }} </router-link>
        <router-link to="/bookings"> {{ t('bookings') }} </router-link>
       <!-- <router-link v-if="isAdmin()" to="/admin"> {{ t('admin') }} </router-link>-->
        <button @click="logout()"> {{ t('logout') }} </button>
      </nav>
    </header>
  </template>
  
<style scoped>
  .site-header {
    background-color: #0a3b6c;
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    min-height: 15dvh;
    position: sticky;
    top: 0;
    z-index: 1000;
    width: 100%;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  .logo {
    padding-left: 10dvw;
    font-size: 25px;
    font-weight: bold;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .nav-links {
    padding-right: 10dvw;
    display: flex;
    align-items: center;
    gap: 18px;
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
</style>

<script setup>
  import { useRouter } from 'vue-router';
  import { useI18n } from 'vue-i18n';
  const { t } = useI18n();
  const router = useRouter();

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
  