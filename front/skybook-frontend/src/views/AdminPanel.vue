<template>
  <div class="admin-view">
    <h2>{{ t('admin-panel') }}</h2>

    <section class="new-flight card">
      <h3>{{ t('create-new-flight') }}</h3>
      <form @submit.prevent="createFlight">
        <div class="form-grid">
                   
            <div class="same-size">
              <multiselect
                
                v-model="newFlight.departureId"
                :options="airports"
                :searchable="true"
                :multiple="false"
                :placeholder="t('search-for-an-airport')"
                label="name"
                track-by="id"
              />
            </div>

          <div class="same-size">

            <div class="same-size">
              <multiselect
                
                v-model="newFlight.destinationId"
                :options="airports"
                :searchable="true"
                :multiple="false"
                :placeholder="t('search-for-an-airport')"
                label="name"
                track-by="id"
              />
            </div>
          </div>
          <div class="same-size">
          <select v-model="newFlight.modelId" :placeholder="t('date')">
            <option v-for="plane in planes" :value="plane.id">
              {{ plane.name }}
            </option>
          </select>
          </div>
          
          <div class="same-size">
            <input v-model="newFlight.departureDate" type="datetime-local" required />
          </div>
          
        </div>
        <button class="button-with-icon add-flight" type="submit"><span class="material-symbols-outlined">add</span> {{ t('create-flight') }}</button>
      </form>
    </section>

    <section>
      <h3> {{ t('all-flights') }}</h3>
        <div class="filters">
          <input type="date" v-model="filterDate"  @change="fetchFlights()" class="filter-input" />
          <input type="text" v-model="filterAirport" @input="fetchFlights()" :placeholder="t('search-by-airport')" class="filter-input" />
        </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>{{ t('origin') }}</th>
              <th>{{ t('destination') }}</th>
              <th>{{ t('date') }}</th>
              <th>{{ t('model') }}</th>
              <th>{{ t('actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="flight in filteredFlights" :key="flight.id">
              <td>{{ flight.departureName }} - {{ flight.departureIata }}</td>

              <td>{{ flight.destinationName }} - {{ flight.destinationIata }}</td>

              <td>{{ formatDate(flight.departureDate) }}</td>
              <td>{{ flight.modelName }}</td>
              <td>
                
                <button class="button-with-icon" @click="openModal(flight.id)"><span class="material-symbols-outlined">delete</span> {{ t('delete') }}</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
            <div class="pagination">
        <button v-for="page in totalPages" :key="page" @click="goToPage(page)" :class="['page-btn', { active: currentPage.value === page - 1 }]">
          {{ page }}
        </button>

      </div>
    </section>

    <section>
      <h3> {{ t('all-bookings') }}</h3>
        <div class="filters booking">
          <input class="filter-input" v-model="filterUser" :placeholder="t('admin-page.search-user')" />
          <input class="filter-input" v-model="filterDeparture" :placeholder="t('admin-page.search-departure')" />
          <input class="filter-input" v-model="filterDestination" :placeholder="t('admin-page.search-destination')" />
          <input class="filter-input" v-model="filterBookingNumber" :placeholder="t('admin-page.search-booking-number')" />
        </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>{{ t('user') }} </th>
              <th>{{ t('departure') }} </th>
              <th>{{ t('destination') }} </th>
              <th>{{ t('booking-number') }} </th>
              <th>{{ t('date') }} </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="booking in filteredBookings" :key="booking.id">
              <td>{{ booking.user?.email }}</td>
              <td>{{ booking.flight?.departureAirport.name }} - {{ booking.flight?.departureAirport.iata }}</td>
              <td>{{ booking.flight?.destinationAirport.name }} - {{ booking.flight?.destinationAirport.iata }}</td>
              <td>{{ booking.number }}</td>
              <td>{{ formatDate(booking.flight?.departureDate) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
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

  .admin-view {
    border-radius: 10px;
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
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: flex-start;
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 12px;
    margin-bottom: 12px;
  }
.same-size {
   flex: 1;
    display: flex;        /* make children also flex containers */
    flex-direction: column; /* if vertical layout is desired */
    justify-content: stretch;
    align-items: stretch;
  }

  form {
    display: flex;
    flex-direction: column;
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
    background-color: rgba(204, 0, 0, 0.964);
    color: white;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .add-flight {
    align-self: center;
    background-color: #0066d0;
    &:hover {
      background-color: #0559A3;
    }
  }

  button:hover {
    background-color: #dc3e3e;
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

  /* pagination */
  .pagination {
    display: flex;
    justify-content: center;
    gap: 8px;
    margin-top: 1rem;
    flex-wrap: wrap;
  }

  .page-btn {
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-radius: 6px;
    background-color: #2196f3;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
  }

  .page-btn:hover {
    background-color: #0559A3;
  }

  .page-btn.active {
    background-color: #0559A3;
    color: white;
    border-color: #2196f3;
  }

</style>


<script setup>
  import { ref, onMounted, nextTick, computed } from 'vue';
  import api from '../services/api';
  import Multiselect from 'vue-multiselect';
  import { useToast } from 'vue-toastification';
  import 'vue-multiselect/dist/vue-multiselect.css';
  import { useI18n } from 'vue-i18n';
  import ConfirmModal from '../components/CustomModal.vue';

  const { t } = useI18n();

  const flights = ref([]);
  const loading = ref(true);
  const bookings = ref([]);
  const airports = ref([]);
  const planes = ref([]);

  //modal y toast
  const showModal = ref(false);
  const modalMessage = ref('');
  const modalTitle = ref('');
  const showConfirm = ref(true);
  const flightSelected = ref('');

  const message = ref('');
  const toast = useToast();

  //filters
  const filterDate = ref('');
  const filterAirport = ref('');

  const filterUser = ref('');
  const filterDeparture = ref('');
  const filterDestination = ref('');
  const filterBookingNumber = ref('');

  const currentPage = ref(0);
  const pageSize = ref(10);
  const totalPages = ref(0);

  const newFlight = ref({
    departureId: '',
    destinationId: '',
    departureDate: '',
    modelId: '',
  });
  
  const createFlight = async () => {
    try {
      await api.post('/flights/', {
        ...newFlight.value,
        departureId: newFlight.value.departureId.id,     
        destinationId: newFlight.value.destinationId.id,
      }); 

      message.value = 'Flight created successfully!'; //TODO
      showSuccess("Successfully created flight");
      newFlight.value = { destination: '', model: '', departureDate: '', seatsTotal: 4 };
      fetchFlights();
    } catch (err) {
      showError();
      message.value = 'Error creating flight.';
    }
  };

  const deleteFlight = async (flightId) => {
    try {
      await api.delete(`/flights/${flightId}`);
      showSuccess("Successfully created flight");
      message.value = 'Flight deleted successfully!';
      fetchFlights(); // Refresh the list after deletion
    } catch (err) {
      showError();
      message.value = 'Error deleting flight.';
    }
  };


  onMounted(() => {
    const token = sessionStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    fetchFlights();
    fetchBookings();
    fetchAirports();
    fetchPlanes();
  });

  const openModal = async (flightId) => {
    flightSelected.value = flightId;
    modalTitle.value = "Delete Flight";
    modalMessage.value = "Are you sure you want to delete this flight?";
    await nextTick();
    showModal.value = true;
  };

  const confirmModal = async () => {
    await deleteFlight(flightSelected.value)
    fetchBookings();
    showModal.value = false;
  };

  const showSuccess = (message) => {
   toast.success(message);
  };

  const showError = () => {
    toast.error('Something went wrong. Please try again.');
  };


  const fetchFlights = async () => {
    try {
      const res = await api.get('/flights/paginated', {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          airport: filterAirport.value || null,
          date: filterDate.value || null
        }
      });
      flights.value = res.data.content;       // paginated results
      totalPages.value = res.data.totalPages; // for page navigation
    } catch (err) {
      message.value = 'Failed to load flights.';
    } finally {
      loading.value = false;
    }
  };

  const goToPage = (page) => {
    currentPage.value = page;
    fetchFlights();
  };

  const fetchBookings = async () => {
    const res = await api.get('/bookings/admin'); // Make sure this endpoint exists and is admin-only
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

  const filteredBookings = computed(() => {
    return bookings.value.filter(booking => {
      const user = booking.user?.email?.toLowerCase() || '';
      const dep = booking.flight?.departureAirport;
      const dest = booking.flight?.destinationAirport;
      const number = booking.number?.toLowerCase() || '';

      return (
        user.includes(filterUser.value.toLowerCase()) &&
        `${dep?.name} ${dep?.iata}`.toLowerCase().includes(filterDeparture.value.toLowerCase()) &&
        `${dest?.name} ${dest?.iata}`.toLowerCase().includes(filterDestination.value.toLowerCase()) &&
        number.includes(filterBookingNumber.value.toLowerCase())
      );
    });
  });
   
</script>