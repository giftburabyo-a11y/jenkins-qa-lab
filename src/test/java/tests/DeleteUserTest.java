package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@Epic("Reqres API Tests")
@Feature("DELETE Users")
public class DeleteUserTest extends BaseTest {

    @Test
    @Story("Delete User")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /users/{id} returns 200 with deleted user info")
    @Description("Verify that deleting an existing user returns HTTP 200")
    public void testDeleteUser() {
        spec()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(200)
                .body("id", equalTo(2));
    }
}