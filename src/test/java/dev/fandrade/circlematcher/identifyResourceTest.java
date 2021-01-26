package dev.fandrade.circlematcher;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class identifyResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/identify")
          .then()
             .statusCode(200)
             .body(is("Identify"));
    }

}