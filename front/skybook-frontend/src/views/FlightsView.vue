<template>
  <div class="flights-view">
    <h2>Available Flights</h2>

    <div v-if="loading" class="status-text">Loading flights...</div>
    <div v-else-if="flights.length === 0" class="status-text">No flights found.</div>

    <div class="flights-list">
      <div v-for="flight in flights" :key="flight.id" class="flight-card">
        <h3>{{ flight.destination }}</h3>
        <p><strong>Model:</strong> {{ flight.model }}</p>
        <p><strong>Date:</strong> {{ formatDate(flight.departureDate) }}</p>
        <p><strong>Available Seats:</strong> {{ flight.seatsAvailable }}</p>
        <button
          @click="bookFlight(flight.id)"
          :disabled="flight.seatsAvailable <= 0"
        >
          {{ flight.seatsAvailable <= 0 ? 'Full' : 'Book' }}
        </button>
      </div>
    </div>

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>

<style scoped>
  .flights-view {
    max-width: 1000px;
    margin: 0 auto;
    padding: 30px 20px;
    text-align: center;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
  }

  h2 {
    margin-bottom: 24px;
    font-size: 28px;
    color: #2c3e50;
  }

  .status-text {
    font-size: 16px;
    color: #666;
    margin: 20px 0;
  }

  .flights-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 24px;
    margin-top: 20px;
  }

  .flight-card {
    background-color: #fff;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 14px rgba(0, 0, 0, 0.08);
    text-align: left;
    transition: transform 0.2s;
  }

  .flight-card:hover {
    transform: translateY(-4px);
  }

  .flight-card h3 {
    font-size: 20px;
    margin-bottom: 10px;
    color: #1e90ff;
  }

  .flight-card p {
    margin: 6px 0;
  }

  button {
    margin-top: 12px;
    padding: 10px 16px;
    font-size: 14px;
    font-weight: bold;
    border: none;
    border-radius: 6px;
    background-color: #1e90ff;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  button:hover {
    background-color: #187bcd;
  }

  button:disabled {
    background-color: #aaa;
    cursor: not-allowed;
  }

  .message {
    margin-top: 30px;
    font-size: 16px;
    color: #2ecc71;
    font-weight: 500;
  }
</style>

  
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
  
