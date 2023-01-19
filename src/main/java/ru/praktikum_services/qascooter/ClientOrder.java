package ru.praktikum_services.qascooter;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ClientOrder extends RestAssuredClient{
    private static final String ORDER_PATH = "/api/v1/orders/";
    private static final String GET_ORDER_PATH = "/v1/orders/";

    @Step("Send POST request to /api/v1/orders")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .assertThat();
    }

    @Step("Send POST request to /v1/orders/track?t=")
    public ValidatableResponse getOrder(int trackId) {
        return given()
                .spec(getBaseSpec())
                .with()
                .queryParam("t", trackId)
                .when()
                .post(GET_ORDER_PATH + "track/")
                .then()
                .assertThat();
    }

    @Step("Send GET request to /api/v1/orders")
    public ValidatableResponse getOrdersList(){
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH)
                .then()
                .assertThat();
    }

    @Step("Send PUT request to /api/v1/orders/cancel/")
    public ValidatableResponse cancel(int trackId) {
        return given()
                .spec(getBaseSpec())
                .body(trackId)
                .when()
                .put(ORDER_PATH + "cancel/")
                .then();
    }
}