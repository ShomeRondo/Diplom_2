import config.BaseURI;
import data.OrderData;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.Order;
import methods.OrderMethods;
import model.User;
import methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class OrdersListTest {
    private User user;
    private Order order;
    private UserMethods userMethods;
    private OrderMethods orderMethods;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = BaseURI.BASE_URI;
        user = UserData.defaultUser();
        order = OrderData.defaultOrder();
        userMethods = new UserMethods();
        orderMethods = new OrderMethods();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        orderMethods.createOrderAuthorisedUser(order, "Bearer " + accessToken);
    }

    @Test
    @DisplayName("Получение списка авторизованным пользователем")
    public void getListOfOrdersForAuthorisedUser(){
        orderMethods.gerOrdersForAuthorisedUser("Bearer "+ accessToken)
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void getListOfOrdersForNotAuthorisedUser(){
        orderMethods.getOrdersNotAuthorisedUser()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    @DisplayName("Удаление созданного для тестов пользователя ")
    public void cleanDate(){
        if(accessToken != null) userMethods.deleteUser(accessToken);
    }
}

