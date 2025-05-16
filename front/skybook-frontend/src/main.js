import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router';
import { createI18n } from 'vue-i18n';
import en from './i18n/en.json';
import es from './i18n/es.json';
import pt from './i18n/pt.json';
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";

const i18n = createI18n({
    legacy: false, // allows Composition API use
    locale: 'en',  // default locale
    fallbackLocale: 'en',
    messages: { en, es, pt }
});

const options = {
    // You can set your default options here
    timeout: 5000,
    closeOnClick: true,
    position: 'bottom-center',
    type: 'info'
};

createApp(App).use(i18n).use(Toast, options).use(router).mount('#app');
