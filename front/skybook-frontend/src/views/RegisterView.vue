<template>
  <div class="register-container">
    <h2>Create Your Account</h2>
    <form @submit.prevent="register">
      
        <input v-model="firstName" placeholder="First Name" required />
        <input v-model="lastName" placeholder="Last Name" required />
      
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <input v-model="phone" placeholder="Phone (optional)" />
      <input v-model="dni" placeholder="DNI" required/>
      <button type="submit">Register</button>
    </form>
    <p v-if="error" class="message error">{{ error }}</p>
    <p v-if="success" class="message success">{{ success }}</p>
  </div>
</template>

<style scoped>
  .register-container {
    position: relative;
    width: 75dvw;
    margin: 80px auto;
    /*padding: 32px;*/
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 14px rgba(0, 0, 0, 0.08);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
  }

  h2 {
    text-align: center;
    margin-bottom: 24px;
    font-size: 24px;
    color: #2c3e50;
  }

  form {
    display: flex;
    flex-direction: column;
    gap: 14px;
  }

  .input-group {
    display: flex;
    gap: 12px;
  }

  .input-group input {
    flex: 1;
  }

  input {
    padding: 12px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 8px;
    transition: border-color 0.3s;
  }

  input:focus {
    border-color: #3498db;
    outline: none;
  }

  button {
    padding: 12px;
    font-size: 16px;
    background-color: #3498db;
    color: white;
    border: none;
    border-radius: 8px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  button:hover {
    background-color: #2980b9;
  }

  .message {
    text-align: center;
    margin-top: 16px;
    font-weight: 500;
    font-size: 14px;
  }

  .error {
    color: #e74c3c;
  }

  .success {
    color: #2ecc71;
  }
</style>

  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import api from '../services/api';
  
  const router = useRouter();
  
  const firstName = ref('');
  const lastName = ref('');
  const email = ref('');
  const password = ref('');
  const phone = ref('');
  const dni = ref('');
  const error = ref('');
  const success = ref('');
  
  const register = async () => {
    error.value = '';
    success.value = '';
  
    try {
      const response = await api.post('/auth/register', {
        firstName: firstName.value,
        lastName: lastName.value,
        email: email.value,
        password: password.value,
        phone: phone.value,
        dni: dni.value,
        
      });
  
      success.value = 'Registration successful! Redirecting to login...';
      setTimeout(() => router.push('/login'), 2000);
    } catch (err) {
      error.value = 'Registration failed. This email might already exist.';
    }
  };
  </script>

  