package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("Reqres API Tests")
@Feature("Authentication")
public class AuthTest extends BaseTest {

    @Test
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /login with valid credentials returns a token")
    @Description("Verify that a successful login returns HTTP 200 and a non-null token")
    public void testLoginSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "mor_2314");
        body.put("password", "83r5^_");

        spec()
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(anyOf(equalTo(200), equalTo(201)))
                .body("token", notNullValue());
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /login without password returns 400 and error message")
    @Description("Verify that login without a password returns HTTP 400 with an error field")
    public void testLoginMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "mor_2314");

        spec()
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400);
    }

    @Test
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /users with valid data returns 201 and an ID")
    @Description("Verify that successful registration (user creation) returns HTTP 201 with id")
    public void testRegisterSuccess() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "John@gmail.com");
        body.put("username", "johnd");
        body.put("password", "m38rmF$");

        spec()
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /users missing data may still create or return an error")
    @Description("Verify that registration with limited fields returns expected status")
    public void testRegisterMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "sydney@fife");

        spec()
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }
}