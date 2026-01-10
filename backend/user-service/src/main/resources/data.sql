-- Insert test users for development/testing
INSERT INTO users (phone_number, email, password, first_name, last_name, account_status, created_at, updated_at) 
VALUES 
    ('0712345678', 'testuser@example.com', 'password123', 'Test', 'User', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0712345679', 'johndoe@example.com', 'password123', 'John', 'Doe', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0712345680', 'janedoe@example.com', 'password123', 'Jane', 'Doe', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0712345999', 'customer@sri.com', 'Test@123', 'Sri', 'Care', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
