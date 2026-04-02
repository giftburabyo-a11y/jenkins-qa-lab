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
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");

        spec()
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /login without password returns 400 and error message")
    @Description("Verify that login without a password returns HTTP 400 with an error field")
    public void testLoginMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");

        spec()
                .body(body)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /register with valid data returns 200 and an ID")
    @Description("Verify that successful registration returns HTTP 200 with id and token")
    public void testRegisterSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        spec()
                .body(body)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /register without password returns 400")
    @Description("Verify that registration without a password returns HTTP 400 with an error")
    public void testRegisterMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "sydney@fife");

        spec()
                .body(body)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", notNullValue());
    }
}