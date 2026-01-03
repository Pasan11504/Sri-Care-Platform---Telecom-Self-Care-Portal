import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Alert, Card, Row, Col } from 'react-bootstrap';
import { billingService } from '../services/billingService';
import { paymentService } from '../services/paymentService';
import { useNavigate } from 'react-router-dom';

export default function BillsPage() {
  const [bills, setBills] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');

  useEffect(() => {
    loadBills();
  }, [userId]);

  const loadBills = async () => {
    try {
      const res = await billingService.getUserBills(userId);
      setBills(res.data);
    } catch (err) {
      setError('Failed to load bills');
    }
  };

  const handlePayBill = async (billId, amount) => {
    try {
      const paymentData = {
        userId: parseInt(userId),
        billId: billId,
        amount: amount,
        paymentMethod: 'CREDIT_CARD',
        cardNumber: '4111111111111111',
        cardHolderName: 'Customer Name',
        expiryDate: '12/25',
        cvv: '123'
      };
      await paymentService.processPayment(paymentData);
      await billingService.markAsPaid(billId);
      alert('Payment successful!');
      loadBills();
    } catch (err) {
      setError('Payment failed');
    }
  };

  const totalAmount = bills.reduce((sum, bill) => sum + parseFloat(bill.billAmount), 0);

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Your Bills</h1>
        <Button variant="secondary" onClick={() => navigate('/dashboard')}>Back</Button>
      </div>

      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={4}>
          <Card>
            <Card.Body>
              <Card.Title>Total Bills</Card.Title>
              <h3>Rs. {totalAmount.toFixed(2)}</h3>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Table striped hover>
        <thead>
          <tr>
            <th>Bill Date</th>
            <th>Due Date</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {bills.map(bill => (
            <tr key={bill.id}>
              <td>{bill.billDate}</td>
              <td>{bill.dueDate}</td>
              <td>Rs. {bill.billAmount}</td>
              <td>
                <span className={`badge ${bill.status === 'PAID' ? 'bg-success' : 'bg-warning'}`}>
                  {bill.status}
                </span>
              </td>
              <td>
                {bill.status === 'UNPAID' && (
                  <Button size="sm" variant="success" onClick={() => handlePayBill(bill.id, bill.billAmount)}>
                    Pay Now
                  </Button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
}
