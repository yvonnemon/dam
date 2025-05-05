<template>
  <div id="app">
    <header v-if="showHeader">
      <h1>Flight Booking</h1>
      <nav>
        <router-link to="/flights">Flights</router-link>
        <router-link to="/bookings">My Bookings</router-link>
        <router-link v-if="isAdmin" to="/admin">Admin</router-link>
        <button @click="logout">Logout</button>
      </nav>
    </header>

    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
// No logic needed here yet
</script>

<style scoped>
#app {
  font-family: 'Segoe UI', sans-serif;
  text-align: center;
  padding: 0px 20px;
  min-width: 70vw;
}

header {
  background-color: #1e90ff;
  color: white;
  padding: 15px;
  margin-bottom: 30px;
}

main {
  max-width: 800px;
  margin: 10px auto;
}
</style>

