# Jenkins QA Lab — REST Assured API Test Suite

Automated API tests using REST Assured + JUnit 5, executed via a Jenkins CI/CD pipeline.

## Test Coverage

| Test Class | Scenarios |
|---|---|
| `GetUsersTest` | List users (page 1 & 2), single user, 404 not found |
| `CreateUserTest` | Create user (full + name-only payload) |
| `UpdateUserTest` | PUT full update, PATCH partial update |
| `DeleteUserTest` | DELETE returns 204 |
| `AuthTest` | Login success, login missing password, register success, register missing password |

**Total: 13 test scenarios**

## Project Structure

```
jenkins-qa-lab/
├── src/test/java/tests/
│   ├── BaseTest.java
│   ├── GetUsersTest.java
│   ├── CreateUserTest.java
│   ├── UpdateUserTest.java
│   ├── DeleteUserTest.java
│   └── AuthTest.java
├── pom.xml
├── Dockerfile
├── Jenkinsfile
└── README.md
```

## Run Locally

```bash
mvn test
```

## Run via Docker

```bash
docker build -t jenkins-qa-lab .
docker run --rm jenkins-qa-lab
```

## Jenkins Pipeline Stages

1. **Checkout** — pulls latest code from GitHub
2. **Build & Install Dependencies** — downloads Maven dependencies
3. **Run API Tests** — executes all REST Assured tests
4. **Generate Allure Report** — builds interactive HTML report
5. **Publish Reports** — archives Surefire XML + publishes Allure report

## Notifications

- ✅ Slack notification on pass/fail/unstable
- ✅ Email notification on pass/fail
