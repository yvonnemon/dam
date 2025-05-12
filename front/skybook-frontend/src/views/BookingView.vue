<template>
  <div class="bookings-view">
    <h2>Your Bookings</h2>

    <div v-if="loading" class="loading-message">Loading your bookings...</div>
    <div v-else-if="bookings.length === 0" class="no-bookings">You have no bookings yet.</div>

    <div class="booking-list">
      <div v-for="booking in bookings" :key="booking.id" class="booking-card">
        <div class="card-header">
          <h3>Booking number: {{ booking.number }}</h3>
          
          <p :class="getStatusClass(booking.status)"><strong>{{ formatStatus(booking.status) }}</strong></p>
          <h5>
            <div class="h3-icons">            
              <span class="material-symbols-outlined iconos">
              travel
          </span><p> {{ booking.flight.departureAirport.name }} - {{ booking.flight.departureAirport.iata }}
            > {{ booking.flight.destinationAirport.name }} - {{ booking.flight.destinationAirport.iata }}</p></div>
          </h5>
          <div class="p-icons">
            <span class="material-symbols-outlined">schedule</span>
            <p class="date">{{ formatDate(booking.flight.departureDate) }}</p>
          </div>

        </div>
        <div class="card-body">
          <p><strong>Flight number:</strong> {{ booking.flight.flightNumber }}</p>
          
        </div>
        <button @click="cancelBooking(booking.id)" :disabled="isDisabled(booking.status)">Cancel Booking</button>
      </div>
    </div>

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>

<style scoped>
.bookings-view {
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

.loading-message,
.no-bookings {
  font-size: 18px;
  color: #d3f5ff;
}

.booking-list {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  margin-top: 20px;
}

.booking-card {
  background-color: #ffffff;
  border: 1px solid #ebfbff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.booking-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  margin-bottom: 15px;
}

.card-header h3 {
  font-size: 22px;
  color: #2980b9;
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
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #c0392b;
}

button:disabled {
  background-color: #ccc; /* Gray background */
  color: #777; /* Lighter text */
  cursor: not-allowed; /* Change cursor to indicate it's disabled */
  border: 1px solid #ddd; /* Lighter border */
}


.message {
  margin-top: 30px;
  font-size: 18px;
  color: #27ae60;
  font-weight: bold;
}

.status-booked {
  color: green;
}

.status-cancelled {
  color: red;
}
</style>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';

const bookings = ref([]);
const loading = ref(true);
const message = ref('');

const fetchBookings = async () => {
  try {
    const res = await api.get('/bookings/user');
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

const formatStatus = (status) => {
  if (status === 'BOOKED') {
    return 'Confirmed';
  } else {
    return 'Cancelled';
  }

};

const getStatusClass = (status) => {
  if (status === 'BOOKED') {
    return 'status-booked';
  } else {
    return 'status-cancelled';
  }
};

const isDisabled = (status) => {
  return status === 'CANCELLED' ? true : false;
};



onMounted(() => {
  const token = localStorage.getItem('token');
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }
  fetchBookings();
});
</script>