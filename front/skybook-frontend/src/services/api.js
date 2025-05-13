import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// ✅ If token exists, attach it to every request
const token = sessionStorage.getItem('token');
if (token) {
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

export default api;

