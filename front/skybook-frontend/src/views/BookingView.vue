<template>
    <div class="bookings-view">
      <h2>Your Bookings</h2>
  
      <div v-if="loading">Loading your bookings...</div>
      <div v-else-if="bookings.length === 0">You have no bookings yet.</div>
  
      <div class="booking-list">
        <div v-for="booking in bookings" :key="booking.id" class="booking-card">
          <p><strong>Destination:</strong> {{ booking.flight.destination }}</p>
          <p><strong>Date:</strong> {{ formatDate(booking.flight.departureDate) }}</p>
          <p><strong>Model:</strong> {{ booking.flight.model }}</p>
          <button @click="cancelBooking(booking.id)">Cancel</button>
        </div>
      </div>
  
      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import api from '../services/api';
  
  const bookings = ref([]);
  const loading = ref(true);
  const message = ref('');
  
  const fetchBookings = async () => {
    try {
      const res = await api.get('/users/bookings');
      bookings.value = res.data;
    } catch (err) {
      message.value = 'Failed to load bookings.';
    } finally {
      loading.value = false;
    }
  };
  
  const cancelBooking = async (id) => {
    try {
      await api.delete(`/bookings/${id}`);
      message.value = 'Booking canceled.';
      await fetchBookings();
    } catch (err) {
      message.value = 'Failed to cancel booking.';
    }
  };
  
  const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleString();
  };
  
  onMounted(() => {
    const token = localStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    fetchBookings();
  });
  </script>
  
  <style scoped>
  .bookings-view {
    text-align: center;
  }
  
  .booking-list {
    display: grid;
    gap: 20px;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
  
  .booking-card {
    border: 1px solid #ccc;
    padding: 15px;
    border-radius: 8px;
  }
  
  button {
    margin-top: 10px;
    padding: 6px 12px;
    background-color: #dc3545;
    color: white;
    border: none;
    border-radius: 4px;
  }
  
  .message {
    margin-top: 20px;
    color: green;
  }
  </style>
  