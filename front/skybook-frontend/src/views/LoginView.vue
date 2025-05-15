<template>
    <div class="login-container">
      <h2> {{ t('skybook-login') }} </h2>
      <form @submit.prevent="login" class="form">
        <div class="input-group">
          <label for="email">{{ t('email')}}</label>
          <input
            id="email"
            v-model="email"
            type="email"
            :placeholder="t('enter-your-email')"
            required
          />
        </div>
  
        <div class="input-group">
          <label for="password">{{ t('password') }}</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter your password"
            required
          />
        </div>
        <ConfirmModal
          :visible="showModal"
          title="Download Ticket"
          message="Are you sure you want to download your ticket?"
          @confirm="handleConfirmDownload"
          @cancel="showModal = false"
        />
        <button type="submit" class="submit-btn" @click="openDownloadModal('1')"> {{ t('log-in') }} </button>
        <button type="" class="submit-btn" @click="register()"> {{ t('register') }} </button>
  
        <p v-if="error" class="error-message">{{ error }}</p>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import api from '../services/api'; // axios instance
  import ConfirmModal from '../components/CustomModal.vue';
  import { useI18n } from 'vue-i18n';
  const { t } = useI18n();

  const router = useRouter();
  
  const email = ref('');
  const password = ref('');
  const error = ref('');
  const showModal = ref(false);

  const register = () => {
    router.push('/register');
  };

  const login = async () => {
    error.value = '';
    try {
      const response = await api.post('/auth/login', {
        email: email.value,
        password: password.value,
      });
  
      const token = response.data.token;
      sessionStorage.setItem('token', token);
  
      // Set token as default for future API calls
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  
      // Redirect to flights page
      router.push('/flights');
    } catch (err) {
      error.value = 'Login failed. Check your credentials.';
    }
  };
  const openDownloadModal = (id) => {
    
    showModal.value = true;
  };

  /*
  
  const handleConfirmDownload = () => {
    showModal.value = false;
    downloadTicket(bookingIdToDownload.value);
  };
  */
  </script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 80px auto;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.form {
  display: flex;
  flex-direction: column;
}

.input-group {
  margin-bottom: 20px;
}

label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 12px 15px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #4a90e2;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 10px;
}

.submit-btn:hover {
  background-color: #357ab7;
}

.error-message {
  margin-top: 15px;
  color: red;
  text-align: center;
}
</style>
