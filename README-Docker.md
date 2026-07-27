# MONA MATTI — Docker Deployment & Containerization Guide

This document provides step-by-step instructions for building, running, and managing the **MONA MATTI** multi-nib pencil application using Docker and Docker Compose.

---

## 🚀 Quick Start

### 1. Build and Run Entire Stack with Docker Compose
To launch both the **Spring Boot application** and **MySQL 8.0 database** with persistent storage:

```bash
docker compose up -d --build
```

---

## 🔍 Verification & Health Checks

Once the containers are running, verify service statuses:

### Check Running Containers
```bash
docker ps
```

### Test Public Application Endpoint
Open your browser or run:
```bash
curl -I http://localhost:8099/
```

### Test Dark Modern Admin CMS Panel
Access the content management dashboard at:
```bash
http://localhost:8099/admin
```

### Test Spring Boot Actuator Health Check
```bash
curl -s http://localhost:8099/actuator/health
```
**Expected Output**:
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP", "details": { "database": "MySQL" } },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

---

## 🛠 Single Image Build Instructions

If you wish to build only the standalone application image (`mona-matti:latest`):

```bash
docker build -t mona-matti:latest .
```

---

## ⚙️ Environment Variables Reference

The container configuration is entirely externalized using environment variables:

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `prod` | Active Spring profile (`dev` or `prod`) |
| `SERVER_PORT` | `8099` | Application HTTP server port |
| `DB_HOST` | `mysql` | MySQL database host / service name |
| `DB_PORT` | `3306` | MySQL database port |
| `DB_NAME` | `mona_matti` | Database schema name |
| `DB_USERNAME` | `mona_user` | Database user username |
| `DB_PASSWORD` | `mona_pass` | Database user password |

---

## 💾 Database Persistence & Data Safety

The MySQL database data is stored in a dedicated named volume `mysql_data`.
Stopping or restarting containers will **NOT** erase your database contents:

```bash
# Stop containers safely
docker compose down

# Restart containers (data remains intact)
docker compose up -d
```

---

## 📦 Ready for CI/CD Pipelines

This setup is fully prepared for Jenkins CI/CD pipelines, Kubernetes pod manifests, and Helm chart packaging.
