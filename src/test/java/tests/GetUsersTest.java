package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@Epic("Reqres API Tests")
@Feature("GET Users")
public class GetUsersTest extends BaseTest {

    @Test
    @Story("List Users")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /users returns 200 and a list of users")
    @Description("Verify that requesting page 1 returns HTTP 200 with a non-empty user list")
    public void testGetUsersReturns200() {
        spec()
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(1))
                .body("data", not(empty()))
                .body("data[0].id", notNullValue())
                .body("data[0].email", containsString("@"));
    }

    @Test
    @Story("List Users")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /users page 2 returns correct page number")
    @Description("Verify that page 2 returns HTTP 200 and the correct page value")
    public void testGetUsersPage2() {
        spec()
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", not(empty()));
    }

    @Test
    @Story("Single User")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /users/{id} returns a single user with correct fields")
    @Description("Verify that fetching user with ID 2 returns correct data structure")
    public void testGetSingleUser() {
        spec()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", containsString("https://"));
    }

    @Test
    @Story("Single User")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /users/{id} returns 404 for non-existent user")
    @Description("Verify that requesting a user that does not exist returns HTTP 404")
    public void testGetUserNotFound() {
        spec()
                .when()
                .get("/users/9999")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }
}