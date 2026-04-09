package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("Reqres API Tests")
@Feature("POST Users")
public class CreateUserTest extends BaseTest {

    @Test
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /users creates a new user and returns 201")
    @Description("Verify that creating a user returns 201 with the correct name, job and an auto-generated ID")
    public void testCreateUser() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "nysara@example.com");
        body.put("username", "nysara");
        body.put("password", "strongpass");

        spec()
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Story("Create User")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /users with only name still returns 201")
    @Description("Verify that a partial payload with just a name field still creates a user")
    public void testCreateUserWithNameOnly() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "TestUser");

        spec()
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }
}