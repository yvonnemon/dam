<template>
    <header class="site-header">
      <h1 class="logo">✈️ Flight Booking</h1>
      <nav class="nav-links">
        <router-link to="/flights">Flights</router-link>
        <router-link to="/bookings">Bookings</router-link>
        <router-link v-if="isAdmin" to="/admin">Admin</router-link>
        <button @click="logout">Logout</button>
      </nav>
    </header>
  </template>
  
  <style scoped>
  .site-header {
    background-color: #0a3b6c;
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    min-height: 15dvh;
    position: sticky;
    top: 0;
    z-index: 1000;
    width: 100%;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  .logo {
    padding-left: 10dvw;
    font-size: 25px;
    font-weight: bold;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .nav-links {
    padding-right: 10dvw;
    display: flex;
    align-items: center;
    gap: 18px;
  }
  
  .nav-links a {
    color: white;
    text-decoration: none;
    font-weight: 500;
    padding: 6px 10px;
    border-radius: 4px;
    transition: background 0.2s;
  }
  
  .nav-links a:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
  
  .nav-links button {
    background-color: white;
    color: #1e90ff;
    border: none;
    padding: 6px 12px;
    border-radius: 4px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .nav-links button:hover {
    background-color: #f1f1f1;
  }
  </style>
  