package ru.praktikum_services.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp(){
        courier = Courier.getRandom();
        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    //курьер может авторизоваться;
    @Test
    @DisplayName("Courier login: /api/v1/courier/login")
    @Description("Courier login via valid required fields: login and password")
    public void courierCanLoginWithValidData(){
        //для авторизации нужно передать все обязательные поля;
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.fromRequiredCredentials(courier));

        courierLoginResponse.statusCode(200);
        //успешный запрос возвращает id
        courierId = courierLoginResponse.extract().path("id");
        assertThat("Test", courierId, is(not(0)));
    }

    //для авторизации нужно передать все обязательные поля;
    @Test
    @DisplayName("Courier login with password value only: /api/v1/courier/login")
    @Description("Courier login via valid data: login without password")
    public void courierCanNotLoginWithoutLoginValue(){
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.getWithPasswordOnly(courier));

        courierLoginResponse.statusCode(400);
        //если какого-то поля нет, запрос возвращает ошибку;
        courierLoginResponse.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    //для авторизации нужно передать все обязательные поля;
    @Test
    @DisplayName("Courier authorization with login value only: /api/v1/courier/login")
    @Description("Courier login via valid data: password without login")
    public void courierCanNotLoginWithoutPasswordValue(){
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.getWithLoginOnly(courier));

        courierLoginResponse.statusCode(400);
        //если какого-то поля нет, запрос возвращает ошибку;
        courierLoginResponse.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @DisplayName("Courier authorization with wrong login name: /api/v1/courier/login")
    @Description("Courier login via random login value")
    public void courierCanNotLoginWithRandomLoginValue(){
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.getWithRandomLogin(courier));

        courierLoginResponse.statusCode(404);
        //система вернёт ошибку, если неправильно указать логин или пароль;
        //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
        courierLoginResponse.assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @DisplayName("Courier authorization with wrong password: /api/v1/courier/login")
    @Description("Courier login via random password value")
    public void courierCanNotLoginWithRandomPasswordValue(){
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.getWithRandomPassword(courier));

        courierLoginResponse.statusCode(404);
        //система вернёт ошибку, если неправильно указать логин или пароль;
        courierLoginResponse.assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}
