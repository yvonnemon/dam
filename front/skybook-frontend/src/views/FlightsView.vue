<template>
  <div class="book-flight-view">
    <h2>Book a Flight</h2>

    <div v-if="loading" class="loading-message">Loading available flights...</div>
    <div v-else-if="flights.length === 0" class="no-flights">No flights available at the moment.</div>

    <div class="flight-list">
      <div v-for="flight in flights" :key="flight.id" class="flight-card">
        <div class="card-header">
          <h3>{{ flight.departureName }} &rarr; {{ flight.destinationName }}</h3>
          <p class="date">{{ formatDate(flight.departureDate) }}</p>
        </div>
        <div class="card-body">
          <p><strong>Seats:</strong> {{ flight.seatsAvailable }} / {{ flight.seatsTotal }}</p>
        </div>
        <button
          @click="bookFlight(flight.id)"
          :disabled="isAlreadyBooked(flight.id) || flight.seatsAvailable === 0"
        >
          {{ isAlreadyBooked(flight.id) ? 'Already Booked' : 'Book' }}
        </button>
      </div>
    </div>

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>

<style scoped>
.book-flight-view {
    max-width: 960px;
    margin: 0 auto;
    padding: 24px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
    background-color: #ebfbff;
}

h2 {
  font-size: 28px;
  color: #2c3e50;
  margin-bottom: 20px;
}

.loading-message, .no-flights {
  font-size: 18px;
  color: #d3f5ff;
}

.flight-list {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  margin-top: 30px;
}

.flight-card {
  background-color: #ffffff;
  border: 1px solid #ecf0f1;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.flight-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  margin-bottom: 15px;
}

.card-header h3 {
  font-size: 22px;
  color: #0559a3;
  margin: 0;
}

.date {
  font-size: 14px;
  color: #7f8c8d;
  margin: 5px 0;
}

.card-body {
  margin-bottom: 20px;
}

button {
  padding: 10px 20px;
  background-color: #0066d0;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0559a3;
}

button:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.message {
  margin-top: 30px;
  font-size: 18px;
  color: #3dd5ff;
  font-weight: bold;
}
</style>


<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';

const flights = ref([]);
const loading = ref(true);
const message = ref('');
const userBookings = ref([]);

const newBooking = ref({
  flightId: '',
  datePurchase: new Date(),
})

const fetchFlights = async () => {
  try {
    const res = await api.get('/flights/' );
    flights.value = res.data;
  } catch (err) {
    message.value = 'Failed to load flights.';
  } finally {
    loading.value = false;
  }
};

const bookFlight = async (flightId) => {
  newBooking.value.flightId = flightId;
  try {
    await api.post('/bookings', { ...newBooking.value });
    message.value = 'Booking successful!';
    newBooking.value = {
      flightId: '',
      datePurchase: new Date()
    };
    await fetchFlights(); // Refresh availability
  } catch (err) {
    message.value = 'Could not book flight. Are you already booked?';
  }
};

const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings/user');
      userBookings.value = res.data.map(b => b.flight.id);
    } catch (err) {
      message.value = 'Failed to load bookings.';
    } finally {
      loading.value = false;
    }
};

const formatDate = (d) => new Date(d).toLocaleString();

const isAlreadyBooked = (flightId) => {
  return userBookings.value.includes(flightId);
};

onMounted(() => {
  const token = localStorage.getItem('token');
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }
  fetchFlights();
  fetchBookings();
});
</script>

