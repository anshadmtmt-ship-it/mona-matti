# MONA MATTI — CI/CD Pipeline Documentation

## 1. Pipeline Overview
The MONA MATTI project features an automated Jenkins Declarative Pipeline defined in [`Jenkinsfile`](file:///home/anshad/project/Jenkinsfile).

## 2. Pipeline Stages

```
[ Git Push ] ──► [ Stage 1: Checkout ]
                      │
                      ▼
                 [ Stage 2: Maven Compile ]
                      │
                      ▼
                 [ Stage 3: Test Execution ]
                      │
                      ▼
                 [ Stage 4: JaCoCo Coverage Audit ]
                      │
                      ▼
                 [ Stage 5: Docker Container Build ]
                      │
                      ▼
                 [ Stage 6: Kubernetes Helm Deploy ]
```

### Stage Details
1. **Checkout SCM**: Clones latest `main` branch from `https://github.com/anshadmtmt-ship-it/mona-matti.git`.
2. **Compile**: Executes `mvn clean compile` to ensure zero compilation warnings or broken syntax.
3. **Run Unit & Integration Tests**: Runs JUnit 5 & Mockito test suite with `mvn test`.
4. **JaCoCo Quality Gate**: Generates execution report (`target/site/jacoco/index.html`) enforcing minimum **85%** code coverage threshold.
5. **Build Docker Image**: Builds multi-stage production container labeled with Git commit SHA and `latest`.
6. **Deploy to Kubernetes**: Executes `helm upgrade --install mona-matti ./helm/mona-matti` targeting cluster environment.

## 3. Local Verification Commands
To simulate the CI/CD pipeline locally before pushing code:
```bash
# 1. Clean & Compile
mvn clean compile

# 2. Run Tests & Coverage
mvn verify

# 3. Docker Container Build
docker build -t mona-matti:v1.0.0 .
```
