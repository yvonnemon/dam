<template>
  <div class="bookings-view">
    <h2> {{ t('your-bookings') }}</h2>
        <div class="filters booking">
          <input class="filter-input" v-model="filterDeparture" :placeholder="t('admin-page.search-departure')" />
          <input class="filter-input" v-model="filterDestination" :placeholder="t('admin-page.search-destination')" />
          <input class="filter-input" v-model="filterBookingNumber" :placeholder="t('admin-page.search-booking-number')" />
        </div>
    <div v-if="loading" class="loading-message"> {{ t('loading-your-bookings') }} </div>
    <div v-else-if="bookings.length === 0" class="no-bookings"> {{ t('you-have-no-bookings-yet') }} </div>

    <div class="booking-list">
      <div v-for="booking in filteredBookings" :key="booking.id" class="booking-card">
        <div class="card-header">
          <h3>{{ t('booking-number') }}: {{ booking.number }}</h3>
          
          <p :class="getStatusClass(booking.status)"><strong>{{ formatStatus(booking.status) }}</strong></p>
          <h5>
            <div class="h3-icons">            
              
              <p> {{ booking.flight.departureAirport.name }} - {{ booking.flight.departureAirport.iata }}</p>
                <span class="material-symbols-outlined iconos">travel</span>
                <p>   {{ booking.flight.destinationAirport.name }} - {{ booking.flight.destinationAirport.iata }}</p>
            </div>
          </h5>
          <div class="p-icons">
            <span class="material-symbols-outlined">schedule</span>
            <p class="date">{{ formatDate(booking.flight.departureDate) }}</p>
          </div>

        </div>
        <div class="card-body">
          <p><strong>{{ t('flight-number') }}:</strong> {{ booking.flight.flightNumber }}</p>
           <p><strong>{{ t('price') }}:</strong> {{ booking.price }} €</p>
        </div>
        <div class="action-buttons">
          <button class="button-with-icon" @click="openModal(booking.id)" :disabled="isDisabled(booking.status)"><span class="material-symbols-outlined">delete</span> {{ t('cancel-booking') }}</button>
          <button class="button-with-icon download" @click="downloadTicket(booking.id)" ><span class="material-symbols-outlined">download</span> {{ t('download') }}</button>
         </div>
      </div>

    </div>
      <ConfirmModal
        :visible="showModal"
        :title="modalTitle"
        :message="modalMessage"
        :show-confirm="showConfirm"
        :show-cancel="true"
        @confirm="confirmModal"
        @cancel="showModal = false"
      />

    <p v-if="message" class="message">{{ message }}</p>
  </div>
</template>

<style scoped>
  .bookings-view {
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
    display: flex;
    flex-direction: column;
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
    font-size: 16px;
    cursor: pointer;

  }
  
  button:disabled {
    background-color: #ccc; /* Gray background */
    color: #777; /* Lighter text */
    cursor: not-allowed; /* Change cursor to indicate it's disabled */
    border: 1px solid #ddd; /* Lighter border */
  }

  button:hover&:not(:disabled) {
    background-color: #c0392b;
  }

  .action-buttons{
    margin-top: auto;
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    align-content: center;
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

    /** filter */
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
    margin-bottom: 1rem;
    &.booking {
      max-width: 60dvw;
    }
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
  import { ref, onMounted, nextTick, computed } from 'vue';
  import api from '../services/api';
  import { useI18n } from 'vue-i18n';
  import { useToast } from 'vue-toastification';
  import ConfirmModal from '../components/CustomModal.vue';

  const { t } = useI18n();

  const bookings = ref([]);
  const loading = ref(true);
  const message = ref('');
  const toast = useToast();

  const filterDeparture = ref('');
  const filterDestination = ref('');
  const filterBookingNumber = ref('');

  const showModal = ref(false);
  const modalMessage = ref('');
  const modalTitle = ref('');
  const showConfirm = ref(true);
  const flightSelected = ref('');

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
      showSuccess('Successfully cancelled')
      message.value = 'Booking canceled.';
      await fetchBookings();
    } catch (err) {
      showError();
      message.value = 'Failed to cancel booking.';
    }
  };

  const downloadTicket = async (bookingId) => {
    try {
    // const response = await fetch(`/api/booking/${bookingId}/ticket`);
      const response = await api.get(`/bookings/${bookingId}/ticket`, {
        responseType: 'blob',
      });
        const blob = response.data;
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `ticket-${bookingId}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
        showSuccess('Downloaded successfully')
      } catch (error) {
        console.error('Download error:', error);

        showError();
        if (error.response && error.response.data) {
          const reader = new FileReader();
          reader.onload = () => {
            console.error("Server said:", reader.result);
          };
          reader.readAsText(error.response.data);
      }
    }
  };

  const openModal = async (flightId) => {
    flightSelected.value = flightId;
    modalTitle.value = "Cancel Booking";
    modalMessage.value = "Are you sure you want to cancel the booking?";
    await nextTick();
    showModal.value = true;
  };

  const confirmModal = async () => {
    await cancelBooking(flightSelected.value)
    fetchBookings();
    showModal.value = false;
  };

  const showSuccess = (message) => {
   toast.success(message);
  };

  const showError = () => {
    toast.error('Something went wrong. Please try again.');
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

  const filteredBookings = computed(() => {
    return bookings.value.filter(booking => {
     
      const dep = booking.flight?.departureAirport;
      const dest = booking.flight?.destinationAirport;
      const number = booking.number?.toLowerCase() || '';

      return (
        `${dep?.name} ${dep?.iata}`.toLowerCase().includes(filterDeparture.value.toLowerCase()) &&
        `${dest?.name} ${dest?.iata}`.toLowerCase().includes(filterDestination.value.toLowerCase()) &&
        number.includes(filterBookingNumber.value.toLowerCase())
      );
    });
  });


  onMounted(() => {
    const token = sessionStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    fetchBookings();
  });
</script>