import React, { useState, useEffect, useRef } from 'react';
import { Container, Card, Form, Button, Alert, ListGroup, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

export default function ChatPage() {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId');
  const token = localStorage.getItem('token');
  const messagesEndRef = useRef(null);

  // Fetch existing messages on load and periodically refresh
  useEffect(() => {
    if (!userId || !token) {
      setError('User not authenticated. Please login first.');
      navigate('/login');
      return;
    }

    // Fetch messages immediately
    fetchMessages();

    // Poll for new messages every 2 seconds
    const interval = setInterval(() => {
      fetchMessages();
    }, 2000);

    return () => clearInterval(interval);
  }, [userId, token]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const fetchMessages = async () => {
    try {
      const response = await fetch(`http://localhost:8086/api/chat/customer/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        const data = await response.json();
        setMessages(data);
        setLoading(false);
      } else if (response.status === 401) {
        navigate('/login');
      } else {
        console.warn('Failed to fetch messages');
      }
    } catch (err) {
      console.error('Error fetching messages:', err);
      setLoading(false);
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    try {
      const messagePayload = {
        customerId: parseInt(userId),
        message: newMessage,
        senderType: 'CUSTOMER',
        agentId: null
      };

      const response = await fetch('http://localhost:8086/api/chat/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(messagePayload)
      });

      if (response.ok) {
        setNewMessage('');
        setError('');
        // Refresh messages immediately
        fetchMessages();
      } else if (response.status === 401) {
        navigate('/login');
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Failed to send message');
      }
    } catch (err) {
      console.error('Error sending message:', err);
      setError('Error sending message. Please try again.');
    }
  };

  return (
    <Container className="mt-4" style={{ maxWidth: '600px' }}>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Customer Support Chat</h1>
        <Button variant="secondary" onClick={() => navigate('/dashboard')}>Back</Button>
      </div>

      {error && <Alert variant="danger">{error}</Alert>}
      {loading && (
        <Alert variant="info" className="text-center">
          <Spinner animation="border" size="sm" className="me-2" />
          Loading chat...
        </Alert>
      )}

      <Card>
        <Card.Body style={{ height: '400px', overflowY: 'auto', backgroundColor: '#f5f5f5' }}>
          {messages.length === 0 && !loading && (
            <div className="text-center text-muted py-5">
              No messages yet. Start a conversation!
            </div>
          )}
          <ListGroup variant="flush">
            {messages.map((msg, idx) => (
              <ListGroup.Item key={idx} className={`mb-2 ${msg.senderType === 'CUSTOMER' ? 'text-end' : ''}`}>
                <div className={`badge ${msg.senderType === 'CUSTOMER' ? 'bg-primary' : 'bg-success'} mb-1`}>
                  {msg.senderType === 'CUSTOMER' ? 'You' : 'Support Agent'}
                </div>
                <div className={`p-2 rounded ${msg.senderType === 'CUSTOMER' ? 'bg-primary text-white' : 'bg-light'}`}>
                  {msg.message}
                </div>
                <small className="text-muted">
                  {msg.createdAt ? new Date(msg.createdAt).toLocaleString() : 'Just now'}
                </small>
              </ListGroup.Item>
            ))}
            <div ref={messagesEndRef} />
          </ListGroup>
        </Card.Body>
        <Card.Footer>
          <Form onSubmit={handleSendMessage}>
            <Form.Group className="mb-0">
              <div className="d-flex gap-2">
                <Form.Control
                  type="text"
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  placeholder="Type your message..."
                  disabled={loading}
                />
                <Button variant="primary" type="submit" disabled={loading}>
                  {loading ? <Spinner animation="border" size="sm" /> : 'Send'}
                </Button>
              </div>
            </Form.Group>
          </Form>
        </Card.Footer>
      </Card>
    </Container>
  );
}
