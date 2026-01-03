# Sri-Care Platform - Setup & Run Guide

**Version:** 1.0.0  
**Date:** January 3, 2026

---

## 📋 Table of Contents

1. [System Requirements](#system-requirements)
2. [Installation Steps](#installation-steps)
3. [Starting the System](#starting-the-system)
4. [Accessing the Application](#accessing-the-application)
5. [Testing Procedures](#testing-procedures)
6. [API Testing](#api-testing)
7. [Database Management](#database-management)
8. [Monitoring & Logs](#monitoring--logs)
9. [Troubleshooting](#troubleshooting)
10. [Verification Checklist](#verification-checklist)

---

## 💻 System Requirements

### Minimum Requirements
- **OS:** Windows 10+, macOS 10.14+, or Ubuntu 18.04+
- **RAM:** 8GB (minimum)
- **Disk Space:** 30GB (minimum)
- **Docker:** 20.10 or later
- **Docker Compose:** 2.0 or later

### Recommended
- **RAM:** 16GB or more
- **Disk Space:** 50GB
- **SSD:** For faster performance
- **CPU:** 4+ cores

### Network
- **Internet:** Required for downloading images (first time only)
- **Ports Required:** 3000, 5432, 5672, 15672, 8081-8088
- **Firewall:** No restrictions between localhost ports

### Verification Commands

```bash
# Windows PowerShell
# Check Docker
docker --version
# Expected: Docker version 20.10.x or higher

docker-compose --version
# Expected: Docker Compose version 2.x.x or higher

# Check available disk space
Get-Volume | Where-Object DriveLetter -eq 'C'
# Should show > 30GB Free Space

# Check available RAM
Get-CimInstance Win32_ComputerSystem | Select-Object TotalPhysicalMemory
# Should show >= 8GB
```

---

## 🔧 Installation Steps

### Step 1: Verify Docker Installation

```bash
# Windows PowerShell
docker --version
docker-compose --version
docker run hello-world
```

**Expected Output:**
```
Docker version 20.10.x, build xxx
Docker Compose version 2.x.x, build xxx
Hello from Docker!
```

### Step 2: Clone/Extract Project

```bash
# Navigate to project location
cd path\to\Sri-Care-Platform

# Verify project structure
dir  # Should show: backend, frontend, docker-compose.yml, etc.
```

### Step 3: Verify Project Files

```bash
# Check essential files exist
if (Test-Path "docker-compose.yml") { "✓ docker-compose.yml found" }
if (Test-Path "backend") { "✓ backend folder found" }
if (Test-Path "frontend") { "✓ frontend folder found" }
```

---

## 🚀 Starting the System

### Option 1: Fresh Start (First Time)

```bash
# Navigate to project directory
cd E:\Projects\Telecom-self-care-portal\sri-care-platform

# Build all services and start
docker-compose up -d --build

# This will:
# - Build all Docker images
# - Create all containers
# - Initialize database schema
# - Start all services
# 
# First build: 8-10 minutes
# Subsequent builds: 2-3 minutes
```

### Option 2: Normal Start (Service Already Built)

```bash
# Start all services (without rebuilding)
docker-compose up -d

# Services start in ~1 minute
```

### Option 3: Manual Service Management

```bash
# Start specific service
docker-compose up -d frontend

# Start multiple services
docker-compose up -d frontend postgres user-service

# Start all services
docker-compose up -d

# Background information
# Services start automatically after container start
# Startup dependencies managed by docker-compose
```

### Waiting for Services to Be Ready

**First Run (Full Initialization):**
```bash
# Wait 2-3 minutes for complete startup
# This includes:
# - Database schema creation
# - Java services to start Tomcat
# - RabbitMQ to initialize
```

**Subsequent Runs:**
```bash
# Services typically ready in 30-60 seconds
# Verify with: docker-compose ps
```

---

## ✅ Verifying All Services Running

```bash
# Check container status
docker-compose ps

# Expected output:
# NAME                    STATUS
# sri-care-frontend       Up (healthy)
# sri-care-db             Up (healthy)
# sri-care-rabbitmq       Up (healthy)
# sri-care-user-service   Up
# sri-care-billing-service Up
# ... (all other services should show "Up")
```

### Port Verification

```powershell
# Windows PowerShell - Check all ports listening
netstat -an | Select-String "8081|8082|8083|8084|8085|8086|8087|8088|3000|5432|15672"

# Expected output:
# TCP    0.0.0.0:3000    0.0.0.0:0    LISTENING
# TCP    0.0.0.0:5432    0.0.0.0:0    LISTENING
# TCP    0.0.0.0:5672    0.0.0.0:0    LISTENING
# TCP    0.0.0.0:15672   0.0.0.0:0    LISTENING
# TCP    0.0.0.0:8081-8088    0.0.0.0:0    LISTENING
```

---

## 🌐 Accessing the Application

### Web Portal

**URL:** http://localhost:3000

**Initial Screen:** Login Page

**Test Credentials:**
- Phone Number: `0712345678`
- Password: `password123`

### Navigation Guide

1. **Dashboard:** View bills and services summary
2. **Bills:** View all bills, filter by status
3. **Services:** View and manage active services
4. **Chat:** Send messages to support team
5. **Profile:** View account information

---

## 🔍 Accessing Services Directly

### User Service API
- **URL:** http://localhost:8081
- **Primary Endpoint:** /api/users/login, /api/users/register

### Billing Service API
- **URL:** http://localhost:8082
- **Primary Endpoint:** /api/bills/user/{userId}

### Service Management API
- **URL:** http://localhost:8083
- **Primary Endpoint:** /api/services/activate

### Chat Service
- **URL:** http://localhost:3000 (via frontend)
- **WebSocket:** ws://localhost:3000/ws/chat

### RabbitMQ Management UI
- **URL:** http://localhost:15672
- **Username:** admin
- **Password:** admin123
- **Features:**
  - View message queues
  - Monitor connections
  - Check node health
  - View exchange/bindings

### PostgreSQL Database
- **Host:** localhost
- **Port:** 5432
- **Database:** sri_care
- **Username:** admin
- **Password:** admin123

---

## 🧪 Testing Procedures

### Test Workflow 1: User Registration & Login

```bash
# Step 1: Navigate to frontend
# Open: http://localhost:3000

# Step 2: Click "Sign up here" on login page

# Step 3: Fill registration form
# First Name:   John
# Last Name:    Doe
# Phone:        0799999999
# Email:        john.doe@example.com
# Password:     TestPass123
# Confirm:      TestPass123

# Step 4: Click "Create Account"

# Step 5: Login with new credentials
# Phone:        0799999999
# Password:     TestPass123

# Expected: Access to dashboard
```

### Test Workflow 2: View Bills

```bash
# Step 1: Login with test credentials
# Phone:     0712345678
# Password:  password123

# Step 2: Click "Bills" navigation

# Step 3: View bills list
# Expected: See 2 test bills
# - 5000.00 (UNPAID)
# - 4500.00 (PAID)

# Step 4: Click on bill for details

# Expected: Full bill information displayed
```

### Test Workflow 3: Manage Services

```bash
# Step 1: Login

# Step 2: Navigate to "Services"

# Step 3: View active services
# Expected: See 2 services
# - Premium Voice Plan
# - Unlimited Data 50GB

# Step 4: Activate new service
# (If UI supports it)

# Expected: Service appears in list
```

### Test Workflow 4: Chat Support

```bash
# Step 1: Login

# Step 2: Navigate to "Chat"

# Step 3: Send message
# Type: "Hello support team"

# Step 4: Send

# Expected: Message appears in chat
# Auto-response may appear

# Step 5: View chat history

# Expected: All messages visible
```

---

## 📡 API Testing

### Test Login Endpoint

```powershell
# Windows PowerShell
$body = @{
    phoneNumber = "0712345678"
    password = "password123"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8081/api/users/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body `
    -UseBasicParsing

Write-Host "Status: $($response.StatusCode)"
Write-Host "Response: $($response.Content)"

# Expected:
# Status: 200
# Token: JWT token in response
# UserId: 3
```

### Test Get User Profile

```powershell
# First, login to get JWT token
$loginBody = @{
    phoneNumber = "0712345678"
    password = "password123"
} | ConvertTo-Json

$loginResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/users/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $loginBody `
    -UseBasicParsing

$token = ($loginResponse.Content | ConvertFrom-Json).token

# Now get profile with token
$headers = @{
    "Authorization" = "Bearer $token"
}

$profileResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/users/3" `
    -Headers $headers `
    -UseBasicParsing

Write-Host "Status: $($profileResponse.StatusCode)"
Write-Host $profileResponse.Content
```

### Test Get Bills

```powershell
# Using token from previous login
$headers = @{
    "Authorization" = "Bearer $token"
}

$billsResponse = Invoke-WebRequest -Uri "http://localhost:8082/api/bills/user/3" `
    -Headers $headers `
    -UseBasicParsing

Write-Host "Bills:"
$billsResponse.Content | ConvertFrom-Json | ForEach-Object {
    Write-Host "- Amount: $($_.amount), Status: $($_.status)"
}

# Expected:
# - Amount: 5000, Status: UNPAID
# - Amount: 4500, Status: PAID
```

### Test Get Services

```powershell
$headers = @{
    "Authorization" = "Bearer $token"
}

$servicesResponse = Invoke-WebRequest -Uri "http://localhost:8083/api/services/user/3" `
    -Headers $headers `
    -UseBasicParsing

Write-Host "Services:"
$servicesResponse.Content | ConvertFrom-Json | ForEach-Object {
    Write-Host "- Type: $($_.type), Name: $($_.name), Status: $($_.status)"
}

# Expected:
# - Type: VOICE, Name: Premium Voice Plan, Status: ACTIVE
# - Type: DATA, Name: Unlimited Data 50GB, Status: ACTIVE
```

### Test Send Chat Message

```powershell
# Using token from previous login
$headers = @{
    "Authorization" = "Bearer $token"
}

$chatBody = @{
    customerId = 3
    message = "Hello support team!"
    senderType = "CUSTOMER"
    agentId = $null
} | ConvertTo-Json

$chatResponse = Invoke-WebRequest -Uri "http://localhost:8086/api/chat/send" `
    -Method POST `
    -Headers $headers `
    -ContentType "application/json" `
    -Body $chatBody `
    -UseBasicParsing

Write-Host "Chat Message Sent:"
$chatResponse.Content | ConvertFrom-Json | Format-Table

# Expected output:
# id  customerId message                senderType createdAt
# --  ---------- -------                ---------- ---------
# 1   3          Hello support team!    CUSTOMER   2026-01-03T10:41:21.508244
```

### Test Get Chat Messages

```powershell
$headers = @{
    "Authorization" = "Bearer $token"
}

$messagesResponse = Invoke-WebRequest -Uri "http://localhost:8086/api/chat/customer/3" `
    -Headers $headers `
    -UseBasicParsing

Write-Host "Chat Messages:"
$messagesResponse.Content | ConvertFrom-Json | ForEach-Object {
    Write-Host "[$($_.senderType)] $($_.message)"
    Write-Host "  Sent: $($_.createdAt)"
}

# Expected:
# [CUSTOMER] Hello support team!
#   Sent: 2026-01-03T10:41:21.508244
```

---

## 💾 Database Management

### Access Database

```bash
# Connect to PostgreSQL
docker exec -it sri-care-db psql -U admin -d sri_care

# List all tables
\dt

# View users
SELECT * FROM users;

# View bills
SELECT * FROM bills;

# View services
SELECT * FROM services;

# Exit database
\q
```

### Query Examples

```sql
-- Get all users
SELECT id, phone_number, email, account_status FROM users;

-- Get user's bills
SELECT * FROM bills WHERE user_id = 3;

-- Get unpaid bills
SELECT * FROM bills WHERE status = 'UNPAID';

-- Get user's services
SELECT * FROM services WHERE user_id = 3 AND status = 'ACTIVE';

-- Count total transactions
SELECT COUNT(*) FROM payments;
```

### Reset Database

```bash
# Stop all services
docker-compose down -v

# Remove database volume
docker volume rm sri-care-db-volume

# Restart services
docker-compose up -d

# Database reinitializes automatically
```

---

## 📊 Monitoring & Logs

### View Service Logs

```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs user-service
docker-compose logs billing-service
docker-compose logs frontend

# Real-time log streaming
docker-compose logs -f user-service

# Last 50 lines
docker-compose logs --tail=50 user-service

# Since specific time
docker-compose logs --since 2024-01-01 user-service
```

### Check Service Health

```bash
# Container status
docker-compose ps

# Detailed container info
docker inspect sri-care-user-service

# Resource usage
docker stats

# Network connectivity
docker network ls
docker network inspect sri-care-platform_default
```

### RabbitMQ Monitoring

```
1. Open: http://localhost:15672
2. Login: admin/admin123
3. View:
   - Overview (connections, channels, queues)
   - Connections (active connections)
   - Channels (message channels)
   - Queues (notification queue)
   - Node Status (health)
```

---

## 🔧 Troubleshooting

### Problem 1: Containers Won't Start

**Symptom:** `docker-compose up -d` fails or containers exit immediately

**Solution:**
```bash
# Check logs
docker-compose logs

# Ensure ports are not in use
netstat -an | grep -E "3000|5432|8081"

# Kill process using port (Windows)
# If port 8081 is in use:
Get-Process | Where-Object { $_.Handles -eq 8081 } | Stop-Process

# Restart with rebuild
docker-compose down -v
docker-compose up -d --build
```

### Problem 2: Frontend Not Loading

**Symptom:** http://localhost:3000 shows "Connection refused"

**Solution:**
```bash
# Check if frontend container is running
docker-compose ps | grep frontend

# Rebuild frontend
docker-compose up -d --build frontend

# Check logs
docker-compose logs frontend

# Wait 1 minute and retry
```

### Problem 3: Login Fails

**Symptom:** "Invalid credentials" error with test user

**Solution:**
```bash
# Verify user exists in database
docker exec -it sri-care-db psql -U admin -d sri_care -c \
  "SELECT * FROM users WHERE phone_number = '0712345678';"

# If no user, create test user
# Register via API or web portal

# Check password hash
docker exec -it sri-care-db psql -U admin -d sri_care -c \
  "SELECT phone_number, password_hash FROM users;"
```

### Problem 4: Database Connection Error

**Symptom:** "Error connecting to database" messages in logs

**Solution:**
```bash
# Check if database container is running
docker-compose ps | grep postgres

# Restart database
docker-compose restart postgres

# Wait 10 seconds
Start-Sleep -Seconds 10

# Verify connection
docker exec sri-care-db psql -U admin -d sri_care -c "SELECT 1;"

# If still failing, reset database
docker-compose down -v
docker-compose up -d
```

### Problem 5: RabbitMQ Not Responding

**Symptom:** Notifications not working, RabbitMQ management UI not accessible

**Solution:**
```bash
# Check RabbitMQ container
docker-compose ps | grep rabbitmq

# Restart RabbitMQ
docker-compose restart rabbitmq

# Wait 30 seconds
Start-Sleep -Seconds 30

# Check management UI
# Open: http://localhost:15672
# Login: admin/admin123

# View queues created
# Check if notification queue exists
```

### Problem 6: High Disk Usage

**Symptom:** System running out of disk space

**Solution:**
```bash
# Check Docker disk usage
docker system df

# Clean up unused images
docker image prune -a

# Clean up unused volumes
docker volume prune

# Clean up unused containers
docker container prune

# Full cleanup (removes all)
docker system prune -a --volumes
```

### Problem 7: Services Slow/Unresponsive

**Symptom:** API responses slow, timeouts occurring

**Solution:**
```bash
# Check resource usage
docker stats

# Check memory usage per container
docker ps --format "{{.Names}}" | ForEach-Object {
    docker stats $_ --no-stream
}

# Increase Docker resources:
# Windows: Docker Desktop → Settings → Resources
# Mac: Docker Desktop → Preferences → Resources
# Recommended: 4+ CPU cores, 8+ GB RAM

# Restart services
docker-compose restart
```

---

## ✅ Verification Checklist

### Pre-Deployment Verification

- [ ] Docker installed and running
- [ ] Docker Compose version 2.0+
- [ ] At least 8GB RAM available
- [ ] At least 30GB disk space
- [ ] All project files present
- [ ] Internet connection for pulling images

### Post-Deployment Verification

- [ ] All 11 containers showing "Up" status
- [ ] All ports listening (3000, 5432, 5672, 15672, 8081-8088)
- [ ] Frontend loads at http://localhost:3000
- [ ] Login works with test credentials
- [ ] Database initialized with tables
- [ ] RabbitMQ accessible at http://localhost:15672

### Functional Verification

- [ ] Can register new user
- [ ] Can login with credentials
- [ ] Can view bills
- [ ] Can view services
- [ ] Can send chat message
- [ ] Database queries returning results
- [ ] RabbitMQ queues created

### Security Verification

- [ ] JWT tokens valid (24-hour expiration)
- [ ] Passwords hashed (BCrypt)
- [ ] CORS allowing frontend requests
- [ ] Protected endpoints require token
- [ ] User data isolated per user

---

## 🚀 Next Steps

### Development
1. Explore codebase in backend/ and frontend/
2. Review API endpoints in each service
3. Test all features manually
4. Examine database schema

### Production Deployment
1. Configure environment variables
2. Use external database (AWS RDS, Azure Database)
3. Use managed message broker (AWS SQS, Azure Service Bus)
4. Implement HTTPS/TLS
5. Deploy to Kubernetes/Container Service

### Enhancement
1. Add monitoring (Prometheus, Grafana)
2. Add logging (ELK stack)
3. Implement API Gateway
4. Add authentication service
5. Implement rate limiting

---

## 📚 Additional Resources

- **Docker Documentation:** https://docs.docker.com
- **Docker Compose Docs:** https://docs.docker.com/compose
- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **React Documentation:** https://react.dev
- **PostgreSQL Documentation:** https://www.postgresql.org/docs

---

## 📞 Support & Issues

1. Check logs: `docker-compose logs <service>`
2. Review troubleshooting section above
3. Check port availability
4. Verify system requirements
5. Restart services: `docker-compose restart`

---

**Last Updated:** January 3, 2026  
**Version:** 1.0.0  
**Status:** Production Ready
