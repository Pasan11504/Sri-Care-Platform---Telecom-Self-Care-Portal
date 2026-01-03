import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Alert, Card, Form, Modal } from 'react-bootstrap';
import { serviceService } from '../services/serviceService';
import { useNavigate } from 'react-router-dom';

export default function ServicesPage() {
  const [services, setServices] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [serviceName, setServiceName] = useState('');
  const [serviceType, setServiceType] = useState('DATA');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    loadServices();
  }, [userId]);

  const loadServices = async () => {
    try {
      const res = await serviceService.getUserServices(userId);
      setServices(res.data);
    } catch (err) {
      setError('Failed to load services');
    }
  };

  const handleActivate = async () => {
    try {
      await serviceService.activateService({
        userId: parseInt(userId),
        serviceName,
        serviceType
      });
      alert('Service activated successfully!');
      setShowModal(false);
      setServiceName('');
      loadServices();
    } catch (err) {
      setError('Failed to activate service');
    }
  };

  const handleDeactivate = async (serviceId) => {
    try {
      await serviceService.deactivateService(serviceId);
      alert('Service deactivated!');
      loadServices();
    } catch (err) {
      setError('Failed to deactivate service');
    }
  };

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>My Services</h1>
        <div>
          <Button variant="success" className="me-2" onClick={() => setShowModal(true)}>
            Activate Service
          </Button>
          <Button variant="secondary" onClick={() => navigate('/dashboard')}>Back</Button>
        </div>
      </div>

      {error && <Alert variant="danger">{error}</Alert>}

      <Table striped hover>
        <thead>
          <tr>
            <th>Service Name</th>
            <th>Type</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {services.map(service => (
            <tr key={service.id}>
              <td>{service.serviceName}</td>
              <td>{service.serviceType}</td>
              <td>
                <span className={`badge ${service.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}`}>
                  {service.status}
                </span>
              </td>
              <td>
                {service.status === 'ACTIVE' && (
                  <Button size="sm" variant="danger" onClick={() => handleDeactivate(service.id)}>
                    Deactivate
                  </Button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Activate Service</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Service Name</Form.Label>
              <Form.Control
                type="text"
                value={serviceName}
                onChange={(e) => setServiceName(e.target.value)}
                placeholder="e.g., International Roaming"
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Service Type</Form.Label>
              <Form.Select value={serviceType} onChange={(e) => setServiceType(e.target.value)}>
                <option value="DATA">Data</option>
                <option value="VOICE">Voice</option>
                <option value="VAS">VAS</option>
              </Form.Select>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Close</Button>
          <Button variant="primary" onClick={handleActivate}>Activate</Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}
