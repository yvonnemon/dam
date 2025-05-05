<template>
    <div class="register-container">
      <h2>Create an Account</h2>
      <form @submit.prevent="register">
        <input v-model="firstName" placeholder="First Name" required />
        <input v-model="lastName" placeholder="Last Name" required />
        <input v-model="email" type="email" placeholder="Email" required />
        <input v-model="password" type="password" placeholder="Password" required />
        <input v-model="phone" placeholder="Phone" />
        <input v-model="dni" placeholder="DNI" />
        <button type="submit">Register</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>
    </div>
  </template>
  
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
        role: 'USER'
      });
  
      success.value = 'Registration successful! Redirecting to login...';
      setTimeout(() => router.push('/login'), 2000);
    } catch (err) {
      error.value = 'Registration failed. This email might already exist.';
    }
  };
  </script>
  
  <style scoped>
  .register-container {
    max-width: 400px;
    margin: 80px auto;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
  }
  
  input {
    display: block;
    margin: 10px 0;
    width: 100%;
    padding: 8px;
  }
  
  button {
    width: 100%;
    padding: 10px;
    margin-top: 10px;
  }
  
  .error {
    color: red;
    margin-top: 10px;
  }
  
  .success {
    color: green;
    margin-top: 10px;
  }
  </style>
  