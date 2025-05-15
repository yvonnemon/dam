<template>
  <div class="layout">
    <AppHeader v-if="!['/register', '/login'].includes(route.path)"  />

    <main class="page-content">
      <router-view />
    </main>

    <Footer />
  </div>
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
  flex: 1;
  padding: 20px;

  div {
    background-color: #e0edf9e0;
  }
}

@media (min-width: 1024px) {
  .page-content > div{
    max-width: 50dvw;
    flex: 1;
    padding: 20px;    
  }
}
</style>

<script setup>
  import { useRoute } from 'vue-router';
  import AppHeader from './components/Header.vue'; 
  import Footer from './components/Footer.vue';

  const route = useRoute();

  
</script>


