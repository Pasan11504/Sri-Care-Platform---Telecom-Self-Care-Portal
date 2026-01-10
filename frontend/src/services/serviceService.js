import axios from 'axios';

const API_BASE_URL = 'http://localhost:8083';

export const serviceService = {
  activateService: (data) => axios.post(`${API_BASE_URL}/api/services/activate`, data),
  deactivateService: (serviceId) => axios.put(`${API_BASE_URL}/api/services/${serviceId}/deactivate`),
  reactivateService: (serviceId) => axios.put(`${API_BASE_URL}/api/services/${serviceId}/reactivate`),
  getUserServices: (userId) => axios.get(`${API_BASE_URL}/api/services/user/${userId}`),
  getActiveServices: (userId) => axios.get(`${API_BASE_URL}/api/services/user/${userId}/active`),
  getPredefinedServices: () => axios.get(`${API_BASE_URL}/api/services/predefined`)
};
