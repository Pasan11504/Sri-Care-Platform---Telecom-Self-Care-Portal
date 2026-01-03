import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import DashboardPage from './pages/DashboardPage';
import BillsPage from './pages/BillsPage';
import ServicesPage from './pages/ServicesPage';
import ChatPage from './pages/ChatPage';
import ChangePasswordPage from './pages/ChangePasswordPage';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/bills" element={<BillsPage />} />
        <Route path="/services" element={<ServicesPage />} />
        <Route path="/chat" element={<ChatPage />} />
        <Route path="/change-password" element={<ChangePasswordPage />} />
        <Route path="/" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
