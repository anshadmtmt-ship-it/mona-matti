# MONA MATTI — Deployment Guide

## 1. Local Development Deployment (H2 In-Memory/File DB)
```bash
# Clone Repository
git clone https://github.com/anshadmtmt-ship-it/mona-matti.git
cd mona-matti

# Run Spring Boot Application
mvn spring-boot:run
```
Access the application at `http://localhost:8099`.

---

## 2. Docker & Docker Compose Deployment
### Standalone Docker Image
```bash
# Build Image
docker build -t mona-matti:1.0.0 .

# Run Container
docker run -d -p 8099:8099 --name mona-matti-app mona-matti:1.0.0
```

### Docker Compose (Spring Boot + MySQL 8.0)
```bash
docker-compose up -d
```

---

## 3. Kubernetes Deployment (Helm Chart)
```bash
# Install / Upgrade Helm Release
helm upgrade --install mona-matti ./helm/mona-matti \
  --namespace production \
  --create-namespace

# Verify Deployment Status
kubectl get pods -n production
kubectl get svc -n production
```
