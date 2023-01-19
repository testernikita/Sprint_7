package ru.praktikum_services.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest {
    private static ClientOrder clientOrder = new ClientOrder();

    @Test
    @DisplayName("Check order list: /api/v1/orders")
    @Description("Response body contains order list")
    public void checkingGettingOrdersList() {
        ValidatableResponse response = clientOrder.getOrdersList();
        response.assertThat().statusCode(200);
        //в тело ответа возвращается список заказов
        response.body("orders.id", not(emptyArray()));

        ArrayList<HashMap> ordersList = response.extract().body().path("orders");
        ordersList.forEach(item -> assertThat("Order ID is incorrect", item.get("id"), is(not(0))));
    }
}