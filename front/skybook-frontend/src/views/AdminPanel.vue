<template>
  <div class="admin-view">
    <h2>Admin Panel</h2>

    <section class="new-flight card">
      <h3>Create New Flight</h3>
      <form @submit.prevent="createFlight">
        <div class="form-grid">
          <!--TODO placeholder-->
          <select v-model="newFlight.departureId" placeholder="Departure">
            <option value="" disabled selected>Choose a departure airport</option>
            <option v-for="airport in airports" :value="airport.id">
              {{ airport.name }}
            </option>
          </select>
          <select v-model="newFlight.destinationId" placeholder="Destination">
             <option value="" disabled selected>Choose a destination airport</option>
            <option v-for="airport in airports" :value="airport.id">
              {{ airport.name }}
            </option>
          </select>
          <select v-model="newFlight.modelId" placeholder="Date">
            <option v-for="plane in planes" :value="plane.id">
              {{ plane.name }}
            </option>
          </select>
          <input v-model="newFlight.departureDate" type="datetime-local" required />
          
        </div>
        <button type="submit">Create Flight</button>
      </form>
    </section>

    <section>
      <h3>All Flights</h3>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>Origin</th>
              <th>Destination</th>
              <th>Date</th>
              <th>Model</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="flight in flights" :key="flight.id">
              <td>{{ flight.departureName }} - {{ flight.departureIata }}</td>

              <td>{{ flight.destinationName }} - {{ flight.destinationIata }}</td>

              <td>{{ formatDate(flight.departureDate) }}</td>
              <td>{{ flight.modelName }}</td>
              <td>
                
                <button @click="deleteFlight(flight.id)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section>
      <h3>All Bookings</h3>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>User</th>
              <th>Flight</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="booking in bookings" :key="booking.id">
              <td>{{ booking.user?.email }}</td>
              <td>{{ booking.flight?.destination }}</td>
              <td>{{ formatDate(booking.flight?.departureDate) }}</td>
            </tr>
          </tbody>
        </table>
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

  h2,
  h3 {
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

  form select {
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

  .table-container {
    overflow-x: auto;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    overflow: hidden;
  }

  th,
  td {
    padding: 12px 16px;
    text-align: left;
    border-bottom: 1px solid #eee;
  }

  th {
    background-color: #f0f0f0;
    color: #333;
    font-weight: bold;
  }

  tr:hover {
    background-color: #f9f9f9;
  }
</style>


<script setup>
  import { ref, onMounted } from 'vue';
  import api from '../services/api';

  const flights = ref([]);
  const bookings = ref([]);
  const airports = ref([]);
  const planes = ref([]);

  const message = ref('');

  const newFlight = ref({
    departureId: '',
    destinationId: '',
    departureDate: '',
    modelId: '',
  });
  
  const createFlight = async () => {
    try {
      await api.post('/flights', {
        ...newFlight.value,
      }); 

      message.value = 'Flight created successfully!';
      newFlight.value = { destination: '', model: '', departureDate: '', seatsTotal: 4 };
      fetchFlights();
    } catch (err) {
      message.value = 'Error creating flight.';
    }
  };

  const deleteFlight = async (flightId) => {
    try {
      await api.delete(`/flights/${flightId}`);

      message.value = 'Flight deleted successfully!';
      fetchFlights(); // Refresh the list after deletion
    } catch (err) {
      message.value = 'Error deleting flight.';
    }
  };


  onMounted(() => {
    const token = localStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    fetchFlights();
    fetchBookings();
    fetchAirports();
    fetchPlanes();
  });

  const fetchFlights = async () => {
    const res = await api.get('/flights/');
    flights.value = res.data;
  };

  const fetchBookings = async () => {
    const res = await api.get('/bookings/'); // Make sure this endpoint exists and is admin-only
    bookings.value = res.data;
  };

  const fetchAirports = async () => {
    const res = await api.get('/airports/');
    airports.value = res.data;
  };

  const fetchPlanes = async () => {
    const res = await api.get('/planes/');
    planes.value = res.data;
  };

    const formatDate = (d) => {
    const date = new Date(d);
    const day = String(date.getDate()).padStart(2, '0'); // Add leading zero
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Add leading zero, month starts at 0
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0'); // Add leading zero for hours
    const minutes = String(date.getMinutes()).padStart(2, '0'); // Add leading zero for minutes

    return `${day}-${month}-${year} ${hours}:${minutes}`;
  };

</script>