package org.ninjaware.binder.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class UserResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/user/34555")
          .then()
             .statusCode(204);
    }

}