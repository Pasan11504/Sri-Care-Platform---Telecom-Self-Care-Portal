import axios from 'axios';

const API_BASE_URL = 'http://localhost:8082';

export const billingService = {
  createBill: (data) => axios.post(`${API_BASE_URL}/api/bills`, data),
  getUserBills: (userId) => axios.get(`${API_BASE_URL}/api/bills/user/${userId}`),
  getUnpaidBills: (userId) => axios.get(`${API_BASE_URL}/api/bills/user/${userId}/unpaid`),
  getBill: (billId) => axios.get(`${API_BASE_URL}/api/bills/${billId}`),
  markAsPaid: (billId) => axios.put(`${API_BASE_URL}/api/bills/${billId}/pay`)
};
