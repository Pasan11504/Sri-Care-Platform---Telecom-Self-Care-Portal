import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Alert, Card, Form, Modal, Spinner } from 'react-bootstrap';
import { serviceService } from '../services/serviceService';
import { useNavigate } from 'react-router-dom';

export default function ServicesPage() {
  const [services, setServices] = useState([]);
  const [predefinedServices, setPredefinedServices] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    loadServices();
    loadPredefinedServices();
  }, [userId]);

  const loadServices = async () => {
    try {
      const res = await serviceService.getUserServices(userId);
      setServices(res.data);
    } catch (err) {
      setError('Failed to load services');
    }
  };

  const loadPredefinedServices = async () => {
    try {
      setLoading(true);
      const res = await serviceService.getPredefinedServices();
      setPredefinedServices(res.data);
      setLoading(false);
    } catch (err) {
      console.error('Failed to load predefined services:', err);
      setLoading(false);
    }
  };

  const handleActivate = async () => {
    if (!selectedService) {
      setError('Please select a service');
      return;
    }

    try {
      await serviceService.activateService({
        userId: parseInt(userId),
        serviceName: selectedService.serviceName,
        serviceType: selectedService.serviceType,
        price: selectedService.price
      });
      alert('Service activated successfully! Bill has been created.');
      setShowModal(false);
      setSelectedService(null);
      loadServices();
    } catch (err) {
      setError('Failed to activate service: ' + (err.response?.data?.message || err.message));
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

  const handleReactivate = async (serviceId) => {
    try {
      await serviceService.reactivateService(serviceId);
      alert('Service reactivated successfully! New bill has been created.');
      loadServices();
    } catch (err) {
      setError('Failed to reactivate service: ' + (err.response?.data?.message || err.message));
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

      {error && <Alert variant="danger" onClose={() => setError('')} dismissible>{error}</Alert>}

      <h3 className="mt-4 mb-3">Active Services</h3>
      <Table striped hover>
        <thead>
          <tr>
            <th>Service Name</th>
            <th>Type</th>
            <th>Monthly Charge (RS)</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {services.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center text-muted py-4">No active services</td>
            </tr>
          ) : (
            services.map(service => (
              <tr key={service.id}>
                <td>{service.serviceName}</td>
                <td>{service.serviceType}</td>
                <td>{service.monthlyCharge ? service.monthlyCharge.toFixed(2) : 'N/A'}</td>
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
                  {service.status === 'INACTIVE' && (
                    <Button size="sm" variant="primary" onClick={() => handleReactivate(service.id)}>
                      Reactivate
                    </Button>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Activate Service</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {loading ? (
            <div className="text-center py-4">
              <Spinner animation="border" className="me-2" />
              Loading available services...
            </div>
          ) : (
            <>
              <p className="text-muted mb-3">Select a service to activate:</p>
              {predefinedServices.length === 0 ? (
                <Alert variant="info">No services available</Alert>
              ) : (
                <div className="row">
                  {predefinedServices.map(service => (
                    <div key={service.id} className="col-md-6 mb-3">
                      <Card 
                        className={`cursor-pointer ${selectedService?.id === service.id ? 'border-primary' : ''}`}
                        style={{ cursor: 'pointer', border: selectedService?.id === service.id ? '2px solid #007bff' : '' }}
                        onClick={() => setSelectedService(service)}
                      >
                        <Card.Body>
                          <Card.Title>{service.serviceName}</Card.Title>
                          <Card.Text>
                            <small className="text-muted">Type: {service.serviceType}</small>
                          </Card.Text>
                          <Card.Text>
                            <strong>RS {service.price.toFixed(2)}/month</strong>
                          </Card.Text>
                          <Card.Text>
                            <small>{service.description}</small>
                          </Card.Text>
                        </Card.Body>
                      </Card>
                    </div>
                  ))}
                </div>
              )}
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Close</Button>
          <Button 
            variant="primary" 
            onClick={handleActivate}
            disabled={!selectedService}
          >
            Activate Selected Service
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}
