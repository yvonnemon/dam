<template>
  <div class="layout">
    <AppHeader v-if="!['/register', '/login'].includes(route.path)"  />
    <AppHeader v-if="isAdmin() && ['/register', '/login'].includes(route.path)"  />
    <main class="page-content">
      <router-view />
    </main>

    <Footer />
  </div>
  <ChatWidget />
</template>

<style scoped>
/*
#ebfbff
#d3f5ff
#b1eeff
#7be8ff
#3dd5ff
#10b9ff
#0097ff
#007fff
#0066d0
#0559a3
#0a3b6c
#04386c
*/
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.site-header {
  position: sticky;
  top: 0;
  z-index: 1000;
}

.page-content {
  background-image: url('./assets/fondo2.webp');
  background-size: cover;
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: center;
  flex: 1;
  padding: 20px;
  background-size: cover;
  div {
    background-color: #e0edf9e0;
  }
}

@media (min-width: 1024px) {
  .page-content > div{
    width: 100%;
    max-width: 90rem;
    flex: 1;
    padding: 20px;    
  }
}
</style>

<script setup>
  import { useRoute } from 'vue-router';
  import AppHeader from './components/Header.vue'; 
  import Footer from './components/Footer.vue';
  import ChatWidget from './components/ChatWidget.vue'
  import { useI18n } from 'vue-i18n';
  import { jwtDecode } from 'jwt-decode';

  const { t } = useI18n();

  const route = useRoute();

  const token = sessionStorage.getItem('token');

  const isAdmin = () => {
    if(token) {
      const decodedToken = jwtDecode(token);
      const userRole = decodedToken.role;
      return userRole === 'ADMIN';
    }
  };
  
</script>


