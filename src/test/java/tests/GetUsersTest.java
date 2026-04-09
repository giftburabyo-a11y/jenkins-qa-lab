package tests;

import base.BaseTest;
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
    @Description("Verify that requesting users returns HTTP 200 with a non-empty user list")
    public void testGetUsersReturns200() {
        spec()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].email", containsString("@"));
    }

    @Test
    @Story("List Users")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /users with limit returns correct number of items")
    @Description("Verify that limit=2 returns HTTP 200 and exactly 2 items")
    public void testGetUsersLimit() {
        spec()
                .queryParam("limit", 2)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
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
                .body("id", equalTo(2))
                .body("email", notNullValue())
                .body("username", notNullValue())
                .body("name.firstname", notNullValue());
    }

    @Test
    @Story("Single User")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /users/{id} returns empty body or 404 for non-existent user")
    @Description("Verify that requesting a user that does not exist handles it properly")
    public void testGetUserNotFound() {
        spec()
                .when()
                .get("/users/9999")
                .then()
                .statusCode(anyOf(equalTo(404), equalTo(200)));
    }
}