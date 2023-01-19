package ru.praktikum_services.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private static ClientOrder clientOrder = new ClientOrder();
    private Order order;
    private List<ScooterColor> color;
    private int trackId;
    private final int expectedStatus;

    public OrderCreationTest(List<ScooterColor> color, int expectedStatus) {
        this.color = color;
        this.expectedStatus = expectedStatus;
    }

    @After
    public void tearDown() {
        clientOrder.cancel(trackId);
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][]{
                //можно указать оба цвета;
                {List.of(ScooterColor.BLACK, ScooterColor.GREY), 201},
                //можно указать один из цветов — BLACK или GREY;
                {List.of(ScooterColor.BLACK), 201},
                {List.of(ScooterColor.GREY), 201},
                //можно совсем не указывать цвет;
                {List.of(), 201},
        };
    }

    @Test
    @DisplayName("Creating of order with different colors: /api/v1/orders")
    @Description("Creation of order with two colors; with black color; with grey color; without color value")
    public void orderCanCreated() {
        order = Order.getOrder(color);

        ValidatableResponse response = clientOrder.create(order);
        response.assertThat().statusCode(expectedStatus);
        //тело ответа содержит track.
        trackId = response.extract().path("track");

        assertThat("Track ID is incorrect", trackId, is(not(0)));
    }
}