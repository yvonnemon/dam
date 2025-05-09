<template>
  <div class="admin-view">
    <h2>Admin Panel</h2>

    <section class="new-flight card">
      <h3>Create New Flight</h3>
      <form @submit.prevent="createFlight">
        <div class="form-grid">
          <input v-model="newFlight.destination" placeholder="Destination" required />
          <input v-model="newFlight.model" placeholder="Model" required />
          <input v-model="newFlight.departureDate" type="datetime-local" required />
          <input v-model.number="newFlight.seatsTotal" placeholder="Total Seats" required />
        </div>
        <button type="submit">Create Flight</button>
      </form>
    </section>

    <section>
      <h3>All Flights</h3>
      <div class="card-list">
        <div v-for="flight in flights" :key="flight.id" class="card">
          <p><strong>Destination:</strong> {{ flight.destination }}</p>
          <p><strong>Date:</strong> {{ formatDate(flight.departureDate) }}</p>
          <p><strong>Model:</strong> {{ flight.model }}</p>
          <p><strong>Available:</strong> {{ flight.seatsAvailable }} / {{ flight.seatsTotal }}</p>
        </div>
      </div>
    </section>

    <section>
      <h3>All Bookings</h3>
      <div class="card-list">
        <div v-for="booking in bookings" :key="booking.id" class="card">
          <p><strong>User:</strong> {{ booking.user?.email }}</p>
          <p><strong>Flight:</strong> {{ booking.flight?.destination }}</p>
          <p><strong>Date:</strong> {{ formatDate(booking.flight?.departureDate) }}</p>
        </div>
      </div>
    </section>

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>

<style scoped>
.admin-view {
  max-width: 960px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
  background-color: #f9f9f9;
}

h2, h3 {
  color: #2c3e50;
  margin-bottom: 16px;
}

form {
  margin-top: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 12px;
  margin-bottom: 12px;
}

form input {
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
}

button {
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  background-color: #3498db;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #2980b9;
}

.card {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message {
  margin-top: 20px;
  padding: 10px;
  background-color: #e0ffe0;
  border: 1px solid #8bc34a;
  border-radius: 6px;
  color: #2e7d32;
}
</style>

  
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
  

  