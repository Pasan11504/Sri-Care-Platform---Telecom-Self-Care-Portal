import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081';

export const userService = {
  register: (data) => axios.post(`${API_BASE_URL}/api/users/register`, data),
  login: (data) => axios.post(`${API_BASE_URL}/api/users/login`, data),
  getProfile: (userId) => axios.get(`${API_BASE_URL}/api/users/${userId}`),
  changePassword: (data) => axios.post(`${API_BASE_URL}/api/users/change-password`, data),
  resetPassword: (phoneNumber, newPassword) => 
    axios.post(`${API_BASE_URL}/api/users/reset-password?phoneNumber=${phoneNumber}&newPassword=${newPassword}`)
};
