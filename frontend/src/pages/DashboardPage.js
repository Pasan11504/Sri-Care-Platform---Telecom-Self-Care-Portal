import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert, Table } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { userService } from '../services/userService';
import { billingService } from '../services/billingService';
import { serviceService } from '../services/serviceService';

export default function DashboardPage() {
  const [user, setUser] = useState(null);
  const [bills, setBills] = useState([]);
  const [services, setServices] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    if (!userId) {
      navigate('/login');
      return;
    }
    loadData();
  }, [userId, navigate]);

  const loadData = async () => {
    try {
      const userRes = await userService.getProfile(userId);
      setUser(userRes.data);

      const billsRes = await billingService.getUserBills(userId);
      setBills(billsRes.data);

      const servicesRes = await serviceService.getUserServices(userId);
      setServices(servicesRes.data);
    } catch (err) {
      setError('Failed to load data');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    navigate('/login');
  };

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Welcome, {user?.firstName}!</h1>
        <Button variant="danger" onClick={handleLogout}>Logout</Button>
      </div>

      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Account Details</Card.Title>
              <p><strong>Phone:</strong> {user?.phoneNumber}</p>
              <p><strong>Email:</strong> {user?.email}</p>
              <p><strong>Status:</strong> {user?.accountStatus}</p>
              <Button variant="info" onClick={() => navigate('/change-password')}>Change Password</Button>
            </Card.Body>
          </Card>
        </Col>
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Quick Actions</Card.Title>
              <Button variant="primary" className="w-100 mb-2" onClick={() => navigate('/bills')}>
                View Bills
              </Button>
              <Button variant="primary" className="w-100 mb-2" onClick={() => navigate('/services')}>
                Manage Services
              </Button>
              <Button variant="primary" className="w-100" onClick={() => navigate('/chat')}>
                Chat Support
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Card className="mb-4">
        <Card.Body>
          <Card.Title>Recent Bills</Card.Title>
          {bills.length > 0 ? (
            <Table striped>
              <thead>
                <tr>
                  <th>Bill Date</th>
                  <th>Amount</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {bills.slice(0, 5).map(bill => (
                  <tr key={bill.id}>
                    <td>{bill.billDate}</td>
                    <td>Rs. {bill.billAmount}</td>
                    <td>{bill.status}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <p>No bills yet</p>
          )}
        </Card.Body>
      </Card>

      <Card>
        <Card.Body>
          <Card.Title>Active Services</Card.Title>
          {services.length > 0 ? (
            <Table striped>
              <thead>
                <tr>
                  <th>Service</th>
                  <th>Type</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {services.map(service => (
                  <tr key={service.id}>
                    <td>{service.serviceName}</td>
                    <td>{service.serviceType}</td>
                    <td>{service.status}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <p>No services activated</p>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
}
