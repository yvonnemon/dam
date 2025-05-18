<template>
  <div class="not-found">
    <h1> {{ t('404page-not-found') }} </h1>
    <p> {{ t('404-message') }} </p>
  </div>
</template>

<script setup>
  import { useI18n } from 'vue-i18n';
  const { t } = useI18n();
</script>

<style scoped>
.not-found {
    max-width: 960px;
    margin: 0 auto;
    padding: 24px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
    background-color: #f9f9f9;
}
.not-found h1 {
  font-size: 3rem;
}
</style>
