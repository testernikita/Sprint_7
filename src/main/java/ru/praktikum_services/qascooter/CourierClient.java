package ru.praktikum_services.qascooter;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestAssuredClient{
    private static final String COURIER_CREATION_PATH = "api/v1/courier/";

    @Step("Send POST request to api/v1/courier/")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_CREATION_PATH)
                .then()
                .assertThat();
    }

    @Step("Send POST request to api/v1/courier/login/")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_CREATION_PATH + "login/")
                .then()
                .assertThat();
    }

    @Step("Send DELETE request to api/v1/courier/:id")
    public boolean delete(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_CREATION_PATH + courierId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");
    }
}
