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
    @DisplayName("DELETE /users/{id} returns 204 No Content")
    @Description("Verify that deleting an existing user returns HTTP 204 with no body")
    public void testDeleteUser() {
        spec()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204)
                .body(equalTo(""));
    }
}