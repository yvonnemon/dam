<template>
    <div class="login-container">
      <h2>Login</h2>
      <form @submit.prevent="login" class="form">
        <div class="input-group">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="Enter your email"
            required
          />
        </div>
  
        <div class="input-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter your password"
            required
          />
        </div>
  
        <button type="submit" class="submit-btn">Log In</button>
  
        <p v-if="error" class="error-message">{{ error }}</p>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import api from '../services/api'; // axios instance
  
  const router = useRouter();
  
  const email = ref('');
  const password = ref('');
  const error = ref('');
  
  const login = async () => {
    error.value = '';
    try {
      const response = await api.post('/auth/login', {
        email: email.value,
        password: password.value,
      });
  
      const token = response.data.token;
      localStorage.setItem('token', token);
  
      // Set token as default for future API calls
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  
      // Redirect to flights page
      router.push('/flights');
    } catch (err) {
      error.value = 'Login failed. Check your credentials.';
    }
  };
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
