<template>
    <footer class="footer">
      <div class="footer-content">
        <p class="footer-message">{{ t('message-footer') }}</p>
        <p class="footer-links">
          <a href="#">{{ t('terms') }}</a> |
          <a href="#">{{ t('privacy') }}</a>
        </p>
  
        <div class="language-switcher">
          <label for="lang">{{ t('language') }}:</label>
          <select id="lang" v-model="locale">
            <option value="en">English</option>
            <option value="es">Español</option>
          </select>
        </div>
      </div>
    </footer>
  </template>
  
<style scoped>
  .footer {
    padding: 2rem 1rem;
    background-color: #E4EFFA;
    border-top: 1px solid #b1eeff;
    font-size: 0.95rem;
    color: #444;
  }
  
  .footer-content {
    max-width: 800px;
    margin: 0 auto;
    text-align: center;
  }
  
  .footer-message {
    margin-bottom: 0.5rem;
    font-weight: 500;
  }
  
  .footer-links a {
    color: #007BFF;
    text-decoration: none;
    margin: 0 0.3rem;
    transition: color 0.2s ease;
  }
  
  .footer-links a:hover {
    color: #0056b3;
    text-decoration: underline;
  }
  
  .language-switcher {
    margin-top: 1rem;
    font-size: 0.9rem;
  }
  
  .language-switcher label {
    margin-right: 0.5rem;
    font-weight: 500;
  }
  
  .language-switcher select {
    padding: 0.3rem 0.6rem;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #fff;
    cursor: pointer;
    font-size: 0.9rem;
    transition: border-color 0.2s ease;
  }
  
  .language-switcher select:focus {
    outline: none;
    border-color: #007BFF;
  }
</style>
  
  
  <script setup>
  import { useI18n } from 'vue-i18n';
  
  const { t, locale } = useI18n(); // `locale` is reactive
  </script>
  
  