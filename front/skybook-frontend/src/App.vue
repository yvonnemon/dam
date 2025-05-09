<template>
  <div class="layout">
    <AppHeader v-if="route.path !== '/login'" />

    <main class="page-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
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
  

  const route = useRoute();
</script>


