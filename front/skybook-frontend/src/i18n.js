import { createI18n } from 'vue-i18n'
import en from './locales/en.json'
import es from './locales/es.json'
import pt from './locales/pt.json'

const i18n = createI18n({
  legacy: false,
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en,
    es,
    pt // ← this must be here, not in a separate object
  }
});


export default i18n
