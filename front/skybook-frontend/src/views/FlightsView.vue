<template>
    <div class="flights-view">
      <h2>Available Flights</h2>
  
      <div v-if="loading">Loading flights...</div>
      <div v-else-if="flights.length === 0">No flights found.</div>
  
      <div class="flights-list">
        <div v-for="flight in flights" :key="flight.id" class="flight-card">
          <p><strong>Destination:</strong> {{ flight.destination }}</p>
          <p><strong>Model:</strong> {{ flight.model }}</p>
          <p><strong>Date:</strong> {{ formatDate(flight.departureDate) }}</p>
          <p><strong>Available Seats:</strong> {{ flight.seatsAvailable }}</p>
          <button @click="bookFlight(flight.id)" :disabled="flight.seatsAvailable <= 0">Book</button>
        </div>
      </div>
  
      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import api from '../services/api';
  
  const flights = ref([]);
  const loading = ref(true);
  const message = ref('');
  
  const fetchFlights = async () => {
    try {
      const res = await api.get('/flights');
      flights.value = res.data;
    } catch (err) {
      message.value = 'Failed to load flights.';
    } finally {
      loading.value = false;
    }
  };
  
  const bookFlight = async (flightId) => {
    try {
      const res = await api.post('/bookings', { flightId });
      message.value = 'Booking successful!';
      await fetchFlights(); // Refresh available seats
    } catch (err) {
      message.value = 'Booking failed. You may already be booked or unauthorized.';
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
    fetchFlights();
  });
  </script>
  
  <style scoped>
  .flights-view {
    text-align: center;
  }
  
  .flights-list {
    display: grid;
    gap: 20px;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
  
  .flight-card {
    border: 1px solid #ccc;
    padding: 15px;
    border-radius: 8px;
  }
  
  button {
    margin-top: 10px;
    padding: 6px 12px;
    background-color: #1e90ff;
    color: white;
    border: none;
    border-radius: 4px;
  }
  
  button:disabled {
    background-color: gray;
    cursor: not-allowed;
  }
  
  .message {
    margin-top: 20px;
    color: green;
  }
  </style>
  