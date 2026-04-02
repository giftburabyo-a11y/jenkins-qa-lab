package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

/**
 * BaseTest configures REST Assured once for all test classes.
 * Sets the base URI, content type, and attaches Allure logging.
 */
public class BaseTest {

    protected static final String BASE_URL = "https://reqres.in";
    protected static final String API_PATH = "/api";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = API_PATH;
    }

    // Shared request spec: JSON content type + Allure + logging
    protected RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }
}