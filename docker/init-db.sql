-- Create pgcrypto extension for password hashing
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    account_status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bills Table
CREATE TABLE IF NOT EXISTS bills (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    bill_amount DECIMAL(10, 2) NOT NULL,
    bill_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'UNPAID',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Payments Table
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    bill_id INTEGER REFERENCES bills(id) ON DELETE SET NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    transaction_id VARCHAR(100) UNIQUE,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (bill_id) REFERENCES bills(id)
);

-- Services Table
CREATE TABLE IF NOT EXISTS services (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    service_name VARCHAR(100),
    service_type VARCHAR(50), -- VOICE, DATA, VAS
    status VARCHAR(20) DEFAULT 'INACTIVE',
    activated_date TIMESTAMP,
    deactivated_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Chat Messages Table
CREATE TABLE IF NOT EXISTS chat_messages (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    agent_id VARCHAR(100),
    message TEXT NOT NULL,
    sender_type VARCHAR(20), -- CUSTOMER or AGENT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id)
);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    notification_type VARCHAR(50), -- EMAIL, SMS, PUSH
    subject VARCHAR(255),
    body TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Predefined Services Table
CREATE TABLE IF NOT EXISTS predefined_services (
    id SERIAL PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL,
    service_type VARCHAR(50),
    description TEXT,
    price DECIMAL(10, 2),
    billing_cycle VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Indexes
CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_bills_user ON bills(user_id);
CREATE INDEX idx_payments_user ON payments(user_id);
CREATE INDEX idx_services_user ON services(user_id);
CREATE INDEX idx_chat_customer ON chat_messages(customer_id);
CREATE INDEX idx_notifications_user ON notifications(user_id);

-- Insert test users for development/testing
-- Note: Passwords are stored as plain text for development. Use registration endpoint for production with BCrypt encoding.
INSERT INTO users (phone_number, email, password, first_name, last_name, account_status, created_at, updated_at) 
VALUES 
    ('0712345678', 'testuser@example.com', 'password123', 'Test', 'User', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0712345679', 'johndoe@example.com', 'password123', 'John', 'Doe', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0712345680', 'janedoe@example.com', 'password123', 'Jane', 'Doe', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0712345999', 'customer@sri.com', 'Test@123', 'Sri', 'Care', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert predefined services
INSERT INTO predefined_services (service_name, service_type, description, price, billing_cycle, created_at)
VALUES
    ('Premium Voice Plan', 'VOICE', 'Unlimited voice calls across the network', 1500.00, 'MONTHLY', CURRENT_TIMESTAMP),
    ('Data Bundle 5GB', 'DATA', 'High-speed 4G data for web browsing and streaming', 800.00, 'MONTHLY', CURRENT_TIMESTAMP),
    ('International Roaming', 'VAS', 'Stay connected while traveling abroad with special roaming rates', 2000.00, 'MONTHLY', CURRENT_TIMESTAMP),
    ('SMS & MMS Pack', 'VAS', 'Unlimited SMS and MMS to any network', 300.00, 'MONTHLY', CURRENT_TIMESTAMP),
    ('Entertainment Bundle', 'VAS', 'Premium access to music and video streaming services', 500.00, 'MONTHLY', CURRENT_TIMESTAMP),
    ('Business Plus', 'VOICE', 'Dedicated support for business customers with priority routing', 3000.00, 'MONTHLY', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
