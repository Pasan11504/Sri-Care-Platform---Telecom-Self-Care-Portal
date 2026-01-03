import React, { useState } from 'react';
import { Container, Form, Button, Alert, Card } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import { userService } from '../services/userService';

export default function RegistrationPage() {
  const [formData, setFormData] = useState({
    phoneNumber: '',
    email: '',
    firstName: '',
    lastName: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const validateForm = () => {
    if (!formData.phoneNumber || !formData.email || !formData.firstName || 
        !formData.lastName || !formData.password || !formData.confirmPassword) {
      setError('All fields are required');
      return false;
    }

    if (formData.phoneNumber.length < 10) {
      setError('Phone number must be at least 10 digits');
      return false;
    }

    if (!formData.email.includes('@')) {
      setError('Please enter a valid email address');
      return false;
    }

    if (formData.password.length < 8) {
      setError('Password must be at least 8 characters');
      return false;
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return false;
    }

    return true;
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!validateForm()) {
      return;
    }

    setLoading(true);
    try {
      const registrationData = {
        phoneNumber: formData.phoneNumber,
        email: formData.email,
        firstName: formData.firstName,
        lastName: formData.lastName,
        password: formData.password
      };

      const response = await userService.register(registrationData);
      setSuccess('Registration successful! Logging you in...');
      
      // Store token and user ID if provided in response
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
      }
      if (response.data.userId) {
        localStorage.setItem('userId', response.data.userId);
      }

      // Redirect to login page after 2 seconds
      setTimeout(() => {
        navigate('/login', { state: { message: 'Registration successful! Please login.' } });
      }, 2000);
    } catch (err) {
      const errorMessage = err.response?.data?.message || 'Registration failed. Please try again.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: '100vh' }}>
      <Card style={{ width: '100%', maxWidth: '500px' }}>
        <Card.Body>
          <Card.Title className="text-center mb-4">Create Account - Sri-Care Portal</Card.Title>
          
          {error && <Alert variant="danger">{error}</Alert>}
          {success && <Alert variant="success">{success}</Alert>}
          
          <Form onSubmit={handleRegister}>
            <Form.Group className="mb-3">
              <Form.Label>First Name</Form.Label>
              <Form.Control
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                placeholder="Enter your first name"
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Last Name</Form.Label>
              <Form.Control
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Enter your last name"
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type="tel"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                placeholder="Enter 10-digit phone number"
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Email Address</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Enter your email"
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter password (min 8 characters)"
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Confirm Password</Form.Label>
              <Form.Control
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Confirm your password"
                required
              />
            </Form.Group>

            <Button 
              variant="primary" 
              type="submit" 
              className="w-100 mb-3" 
              disabled={loading}
            >
              {loading ? 'Creating Account...' : 'Create Account'}
            </Button>
          </Form>

          <div className="text-center">
            <p className="mb-0">
              Already have an account?{' '}
              <Link to="/login" style={{ textDecoration: 'none', color: '#0d6efd' }}>
                Login here
              </Link>
            </p>
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
}
