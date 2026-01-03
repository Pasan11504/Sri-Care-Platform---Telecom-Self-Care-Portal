-- Add bills for user
INSERT INTO bills (user_id, bill_amount, bill_date, due_date, status, description) 
VALUES 
  (1, 5000.00, '2025-12-01', '2025-12-15', 'UNPAID', 'Monthly bill - January 2026'),
  (1, 4500.00, '2025-11-01', '2025-11-15', 'PAID', 'Monthly bill - December 2025')
ON CONFLICT DO NOTHING;

-- Add services
INSERT INTO services (user_id, service_name, service_type, status, activated_date)
VALUES
  (1, 'Premium Voice Plan', 'VOICE', 'ACTIVE', NOW()),
  (1, 'Unlimited Data 50GB', 'DATA', 'ACTIVE', NOW())
ON CONFLICT DO NOTHING;
