import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router';
import { createI18n } from 'vue-i18n';
import en from './i18n/en.json';
import es from './i18n/es.json';
import pt from './i18n/pt.json';

const i18n = createI18n({
    legacy: false, // allows Composition API use
    locale: 'en',  // default locale
    fallbackLocale: 'en',
    messages: { en, es, pt }
});

createApp(App).use(i18n).use(router).mount('#app');
