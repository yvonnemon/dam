<template>
  <div class="book-flight-view">
    <h2>{{ t('book-a-flight') }}</h2>

    <div class="filters">
      <input type="date" v-model="filterDate" class="filter-input" />
      <input type="text" v-model="filterAirport" :placeholder="t('search-by-airport')" class="filter-input" />
    </div>


    <div v-if="loading" class="loading-message">
      {{ t('loading-available-flights') }}
    </div>

    <div v-else-if="filteredFlights.length === 0" class="no-flights">
      {{ t('no-flights-available-at-the-moment') }}
    </div>


    <div class="flight-list">
      <div v-for="flight in filteredFlights" :key="flight.id" class="flight-card">
        <div class="card-content">
          <!-- Center time & route -->
          <div class="flight-info">
            <div class="flight-time-line">
              <span class="time">{{ formatDate(flight.departureDate) }}</span>
              <div class="line">
                <span class="dot"></span>
                <span class="dot"></span>
              </div>

            </div>
            <div class="flight-route">
              <span class="airport">{{ flight.departureIata }} - {{ flight.departureName }}</span>
              
              <span class="airport destination">{{ flight.destinationIata }} - {{ flight.destinationName }} </span>
            </div>
            <div class="airline-code">
              <p><strong>Flight number: </strong>{{ flight.flightNumber }}</p>
              <p><strong>{{ t('seats') }}:</strong> {{ flight.seatsAvailable }} / {{ flight.seatsTotal }}</p>
            </div>
          </div>

          <!-- Book button -->
          <div class="flight-action">
            <button class="button-with-icon" @click="openBookModal(flight.id)"
              :disabled="isAlreadyBooked(flight.id) || flight.seatsAvailable === 0">
              <span class="material-symbols-outlined">airplane_ticket</span>
              {{ isAlreadyBooked(flight.id) ? t('already-booked') : t('book') }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <ConfirmModal
      :visible="showModal"
      :title="modalTitle"
      :message="modalMessage"
      :show-confirm="showConfirm"
      :show-cancel="true"
      @confirm="confirmBooking"
      @cancel="showModal = false"
    />

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>


<style scoped>
.book-flight-view {
  border-radius: 10px;
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
.no-flights {
  font-size: 18px;
  color: #d3f5ff;
}

.flight-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  margin-top: 30px;
}

.flight-card {
  margin-bottom: 20px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  border: 1px solid #ecf0f1;
  border-radius: 10px;

  /* min-height: 10dvh;*/
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.flight-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.flight-info {
  flex: 1;
  min-width: 250px;
    max-width: 70rem;
  }
  
  .flight-time-line {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 20px;
  }
  
  
  .flight-time-line .line {
    flex: 1;
    height: 2px;
    background: #ccc;
    position: relative;
  }
  
  .flight-time-line .dot {
    width: 8px;
    height: 8px;
    background: #ccc;
    border-radius: 50%;
    position: absolute;
    top: -3px;
  }
  
  .flight-time-line .dot:first-child {
    left: 0;
  }
  
  .flight-time-line .dot:last-child {
    right: 0;
  }
  
  .time {
    font-size: 1.1em;
    font-weight: bold;
  }
  
  .flight-route {
    display: flex;
    justify-content: space-between;
    font-size: 0.9em;
    margin-top: 5px;
  }
  
  .airport {
    color: #333;
  }
  
  .airline-code {
    text-align: center;
    display: flex;
    justify-content: space-evenly;
    margin-top: 8px;
    font-size: 0.85em;
    color: #444;
  }
  
  .flight-action {
    min-width: 120px;
    text-align: right;
    width: 12.5rem;
  }
  
  .card-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    flex-direction: row;
  
    @media (max-width: 1024px) {
  flex-direction: column;
  
  }
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
  margin-top: auto;
  display: flex;
  flex-direction: row;
  
  justify-content: space-evenly;
  align-items: center;
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
  
  .filters {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  margin: auto;
  }
  
  .filter-input {
  padding: 0.6rem 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
  flex: 1;
  min-width: 200px;
  transition: border-color 0.3s;
  }
  
  .filter-input:focus {
  border-color: #007bff;
  outline: none;
  }
</style>


<script setup>
  import { ref, onMounted, computed, nextTick } from 'vue';
  import api from '../services/api';
  import ConfirmModal from '../components/CustomModal.vue';
  import { useI18n } from 'vue-i18n';
  import { useToast } from 'vue-toastification';
  const { t } = useI18n();

  const flights = ref([]);
  const loading = ref(true);
  const message = ref('');
  const userBookings = ref([]);
  const filterDate = ref('');
  const filterAirport = ref('');

  const showModal = ref(false);
  const modalMessage = ref('');
  const modalTitle = ref('');
  const showConfirm = ref(true);
  const toast = useToast();

  const flightSelected = ref('');

  const newBooking = ref({
    flightId: '',
    datePurchase: new Date(),
  })

  const fetchFlights = async () => {
    try {
      const res = await api.get('/flights/');
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
      showSuccess('Flight booked successfully')
      await fetchFlights(); // Refresh availability
    } catch (err) {
      showError();
      message.value = 'Could not book flight. Are you already booked?';
    }
  };

  const openBookModal = async (flightId) => {
    flightSelected.value = flightId;
    modalTitle.value = "Confirm Booking";
    modalMessage.value = "Do you want to proceed with the booking?";
    await nextTick();
    showModal.value = true;
  };

  const confirmBooking = async () => {
    await bookFlight(flightSelected.value)
    fetchBookings();
    showModal.value = false;
  };

  const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings/user-booked');
      userBookings.value = res.data.map(b => b.flight.id);
    } catch (err) {
      message.value = 'Failed to load bookings.';
    } finally {
      loading.value = false;
    }
  };

  const showSuccess = (message) => {
   toast.success(message);
  };

  const showError = () => {
    toast.error('Something went wrong. Please try again.');
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

  const isAlreadyBooked = (flightId) => {
    return userBookings.value.includes(flightId);
  };


const filteredFlights = computed(() => {
    return flights.value.filter(flight => {
      const matchesDate = !filterDate.value || flight.departureDate.startsWith(filterDate.value);
      const matchesAirport =
        !filterAirport.value ||
        flight.departureName.toLowerCase().includes(filterAirport.value.toLowerCase()) ||
        flight.destinationName.toLowerCase().includes(filterAirport.value.toLowerCase());

      return matchesDate && matchesAirport;
    });
  });

  onMounted(() => {
    const token = sessionStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    fetchFlights();
    fetchBookings();
  });

</script>

