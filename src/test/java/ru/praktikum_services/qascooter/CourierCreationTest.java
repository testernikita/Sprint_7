package ru.praktikum_services.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandom();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    //курьера можно создать;
    @Test
    @DisplayName("Courier creation: /api/v1/courier")
    @Description("Courier creation using valid data: login, password, name")
    public void courierCanCreatedWithValidData() {
        ValidatableResponse courierCreateResponse = courierClient.create(courier);
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.from(courier));

        courierId = courierLoginResponse.extract().path("id");

        //запрос возвращает правильный код ответа;
        courierCreateResponse.statusCode(201);

        //успешный запрос возвращает ok: true;
        courierCreateResponse.assertThat().body(equalTo("{\"ok\":true}"));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

    //нельзя создать двух одинаковых курьеров;
    @Test
    @DisplayName("Two identical couriers creation: /api/v1/courier")
    @Description("You cannot create two identical couriers")
    public void canNotCreateTwoIdenticalCouriers() {
        courierClient.create(courier);

        ValidatableResponse secondCourierCreateResponse = courierClient.create(courier);

        //запрос возвращает правильный код ответа;
        secondCourierCreateResponse.statusCode(409);
        //если создать пользователя с логином, который уже есть, возвращается ошибка.
        secondCourierCreateResponse.assertThat().body("message", equalTo("Этот логин уже используется."));
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @Test
    @DisplayName("Courier creation without name: /api/v1/courier")
    @Description("Courier creation using valid data: login and password without name")
    public void courierCanCreatedWithLoginAndPassword() {
        courier = Courier.getWithLoginAndPassword();

        ValidatableResponse courierCreateResponse = courierClient.create(courier);
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.from(courier));

        courierId = courierLoginResponse.extract().path("id");

        //запрос возвращает правильный код ответа;
        courierCreateResponse.statusCode(201);
        //успешный запрос возвращает ok: true;
        courierCreateResponse.assertThat().body(equalTo("{\"ok\":true}"));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Courier creation with firstname only: /api/v1/courier")
    @Description("Courier creation using valid data: firstname without login and password")
    public void courierCanNotCreatedWithNameOnly() {
        courier = Courier.getWithFirstNameOnly();

        ValidatableResponse secondCourierCreateResponse = courierClient.create(courier);

        //запрос возвращает правильный код ответа;
        secondCourierCreateResponse.statusCode(400);
        secondCourierCreateResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Courier creation with login value only: /api/v1/courier")
    @Description("Courier creation using valid data: login without firstname and password")
    public void courierCanNotCreatedWithLoginOnly() {
        courier = Courier.getWithLoginOnly();

        ValidatableResponse secondCourierCreateResponse = courierClient.create(courier);

        //запрос возвращает правильный код ответа;
        secondCourierCreateResponse.statusCode(400);
        secondCourierCreateResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Courier creation with password value only: /api/v1/courier")
    @Description("Courier creation using valid data: password without login and firstname")
    public void courierCanNotCreatedWithPasswordOnly() {
        courier = Courier.getWithPasswordOnly();

        ValidatableResponse secondCourierCreateResponse = courierClient.create(courier);

        //запрос возвращает правильный код ответа;
        secondCourierCreateResponse.statusCode(400);
        secondCourierCreateResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
