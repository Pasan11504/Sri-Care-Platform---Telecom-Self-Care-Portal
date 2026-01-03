import axios from 'axios';

const API_BASE_URL = 'http://localhost:8084';

export const paymentService = {
  processPayment: (data) => axios.post(`${API_BASE_URL}/api/payments/process`, data),
  getUserPayments: (userId) => axios.get(`${API_BASE_URL}/api/payments/user/${userId}`),
  getPayment: (paymentId) => axios.get(`${API_BASE_URL}/api/payments/${paymentId}`)
};
