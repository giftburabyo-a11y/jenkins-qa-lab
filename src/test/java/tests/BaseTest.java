package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static final String BASE_URL = "https://fakestoreapi.com";
    protected static final String API_PATH = "";
    protected static final String API_KEY  =
            "pro_05d2f318d72c9ec9948a1b522f63d43849e4266a47b3ad896d947a4d1f4d79c4";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = API_PATH;
    }

    protected RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .header("x-api-key", API_KEY)
                .filter(new AllureRestAssured())
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }
}