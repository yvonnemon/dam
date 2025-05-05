<template>
    <div class="admin-view">
      <h2>Admin Panel</h2>
  
      <section class="new-flight">
        <h3>Create New Flight</h3>
        <form @submit.prevent="createFlight">
          <input v-model="newFlight.destination" placeholder="Destination" required />
          <input v-model="newFlight.model" placeholder="Model" required />
          <input v-model="newFlight.departureDate" type="datetime-local" required />
          <input v-model.number="newFlight.seatsTotal" placeholder="Total Seats" required />
          <button type="submit">Create Flight</button>
        </form>
      </section>
  
      <section>
        <h3>All Flights</h3>
        <div v-for="flight in flights" :key="flight.id" class="card">
          <p><strong>Destination:</strong> {{ flight.destination }}</p>
          <p><strong>Date:</strong> {{ formatDate(flight.departureDate) }}</p>
          <p><strong>Model:</strong> {{ flight.model }}</p>
          <p><strong>Available:</strong> {{ flight.seatsAvailable }} / {{ flight.seatsTotal }}</p>
        </div>
      </section>
  
      <section>
        <h3>All Bookings</h3>
        <div v-for="booking in bookings" :key="booking.id" class="card">
          <p><strong>User:</strong> {{ booking.user?.email }}</p>
          <p><strong>Flight:</strong> {{ booking.flight?.destination }}</p>
          <p><strong>Date:</strong> {{ formatDate(booking.flight?.departureDate) }}</p>
        </div>
      </section>
  
      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import api from '../services/api';
  
  const flights = ref([]);
  const bookings = ref([]);
  const newFlight = ref({
    destination: '',
    model: '',
    departureDate: '',
    seatsTotal: 4
  });
  const message = ref('');
  
  const fetchFlights = async () => {
    const res = await api.get('/flights');
    flights.value = res.data;
  };
  
  const fetchBookings = async () => {
    const res = await api.get('/admin/bookings'); // Make sure this endpoint exists and is admin-only
    bookings.value = res.data;
  };
  
  const createFlight = async () => {
    try {
      const res = await api.post('/flights', {
        ...newFlight.value,
        seatsAvailable: newFlight.value.seatsTotal
      });
      message.value = 'Flight created successfully!';
      newFlight.value = { destination: '', model: '', departureDate: '', seatsTotal: 4 };
      fetchFlights();
    } catch (err) {
      message.value = 'Error creating flight.';
    }
  };
  
  const formatDate = (d) => new Date(d).toLocaleString();
  
  onMounted(() => {
    const token = localStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    fetchFlights();
    fetchBookings();
  });
  </script>
  
  <style scoped>
  .admin-view {
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
  }
  
  form input {
    margin: 5px;
    padding: 6px;
  }
  
  button {
    padding: 6px 12px;
    margin-top: 10px;
  }
  
  .card {
    border: 1px solid #ccc;
    padding: 10px;
    margin: 10px 0;
    border-radius: 8px;
  }
  
  .message {
    color: green;
    margin-top: 10px;
  }
  </style>
  