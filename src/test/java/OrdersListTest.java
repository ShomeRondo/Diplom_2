import config.BaseURI;
import data.OrderData;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.OrderMethods;
import model.User;
import model.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrdersListTest {
    private User user;
    private Order order;
    private UserMethods userMethods;
    private OrderMethods orderMethods;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = BaseURI.baseURI;
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
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void getListOfOrdersForNotAuthorisedUser(){
        orderMethods.getOrdersNotAuthorisedUser()
                .statusCode(401);
    }

    @After
    @DisplayName("Удаление созданного для тестов пользователя ")
    public void cleanDate(){
        if(accessToken != null) userMethods.deleteUser(accessToken);
    }
}

