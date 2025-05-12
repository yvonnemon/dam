<template>
  <div class="not-found">
    <h1>404 - Page Not Found</h1>
    <p>Sorry, the page you're looking for doesn't exist or you don't have permission to access it.</p>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
};
</script>

<style scoped>
.not-found {
    max-width: 960px;
    margin: 0 auto;
    padding: 24px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
    background-color: #f9f9f9;
}
.not-found h1 {
  font-size: 3rem;
}
</style>
