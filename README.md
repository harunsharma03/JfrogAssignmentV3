# JFrogAssignment

Automated solution for uploading, scanning, and validating a Docker image in JFrog Xray and Artifactory using API and UI-based tests. This project uses Java, Selenium WebDriver, Cucumber, TestNG, RestAssured, and GitHub Actions for CI.

---

## Table of Contents
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [How to Run](#how-to-run)
- [Feature Overview](#feature-overview)
- [CI/CD Integration (Optional)](#cicd-integration-optional)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

- Java 11+  
- Maven  
- Docker Desktop (Running)  
- Google Chrome (latest)  
- GitHub Desktop or Git CLI  
- Eclipse / IntelliJ IDEA  
- JFrog trial account  
- GitHub account

---

## Configuration

All configurable values are stored in `config.properties`.

```properties
base.url=https://<your-jfrog-platform>.jfrog.io
username=your-jfrog-username
password=your-jfrog-password

baseImage=alpine:3.9
customImage=image
tag=v1
repo.name=docker-local

security.policy=sec_policy_1
policy.name=high-severity-only
watch.name=example1-watch

watchnametoapply=harun-watch
watchstartdate=2025-04-07T10:25:00+02:00
watchenddate=2025-04-07T10:30:00+02:00

docker.path=/usr/local/bin/docker
```

---

## How to Run

### Step 1: Clone the repository
```bash
git clone https://github.com/harunsharma03/JfrogAssignmentV2.git
cd JfrogAssignmentV2
```

### Step 2: Configure Docker & Permissions
- Ensure Docker Desktop is installed and **running**
- Make sure `/usr/local/bin/docker` is correct on your system
- Grant execute permissions to the shell script:
```bash
chmod +x src/test/resources/scripts/push-docker-image.sh
```

### Step 3: Import the project in Eclipse/IntelliJ and do a Maven Build
```bash
mvn clean install
```

### Step 4: Run Tests

To execute the full flow (API + UI):
- Locate `TestRunner.java` and right-click → Run as `TestNG Test`

---

## Feature Overview

### 1. Docker Operations
- Push a base image (alpine:3.9) to JFrog Artifactory using `.sh` script
- Verify scan status and trigger re-scan if needed

### 2. API Validations
- Create or validate Docker repository
- Create security policy and watch
- Apply watch on pushed Docker image
- Verify security violations via REST API

### 3. UI Validations (Selenium + Cucumber)
- Login to JFrog Platform
- Navigate to scan reports and policy violations
- Validate violation severity on UI (high & critical only)

---

## CI/CD Integration (Optional)
To integrate with GitHub Actions or Jenkins:
- Set up `.github/workflows/ci.yml`
- Ensure Docker and Java are available on the agent
- You can split jobs for API tests and UI tests

---

## Troubleshooting

- **Docker not found**: Ensure path `/usr/local/bin/docker` is correct
- **Permission Denied**: Run `chmod +x` on the shell script
- **WebDriver Timeout**: Increase explicit wait in UI tests
- **Login Failure on UI**: Check if your JFrog credentials in `config.properties` are valid

---

