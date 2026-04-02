package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("Reqres API Tests")
@Feature("Update Users")
public class UpdateUserTest extends BaseTest {

    @Test
    @Story("PUT User")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /users/{id} fully updates a user and returns 200")
    @Description("Verify that a full PUT update returns 200 with updated name and job fields")
    public void testPutUpdateUser() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "Updated Name");
        body.put("job", "Senior QA");

        spec()
                .body(body)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Name"))
                .body("job", equalTo("Senior QA"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @Story("PATCH User")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PATCH /users/{id} partially updates a user and returns 200")
    @Description("Verify that a PATCH with only the job field updates it and returns 200")
    public void testPatchUpdateUser() {
        Map<String, String> body = new HashMap<>();
        body.put("job", "Lead QA Engineer");

        spec()
                .body(body)
                .when()
                .patch("/users/2")
                .then()
                .statusCode(200)
                .body("job", equalTo("Lead QA Engineer"))
                .body("updatedAt", notNullValue());
    }
}