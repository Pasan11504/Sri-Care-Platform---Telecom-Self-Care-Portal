-- Initialize Predefined Services
INSERT INTO predefined_services (service_name, service_type, price, description, billing_cycle) VALUES
('Premium Voice Plan', 'VOICE', 1500.00, 'Unlimited voice calls with premium features', 'MONTHLY'),
('Data Bundle 5GB', 'DATA', 800.00, 'High-speed 4G data for web browsing and streaming', 'MONTHLY'),
('International Roaming', 'VAS', 2000.00, 'Stay connected while traveling abroad with special roaming rates', 'MONTHLY'),
('SMS & MMS Pack', 'VAS', 300.00, 'Unlimited SMS and MMS to any network', 'MONTHLY'),
('Entertainment Bundle', 'VAS', 500.00, 'Premium access to music and video streaming services', 'MONTHLY'),
('Business Plus', 'VOICE', 3000.00, 'Dedicated support for business customers with priority routing', 'MONTHLY')
ON CONFLICT DO NOTHING;
