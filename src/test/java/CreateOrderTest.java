import config.BaseURI;
import data.OrderData;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private User user;
    private Order order;
    private UserMethods userMethods;
    private OrderMethods orderMethods;
    private String accessToken;
    private UserCredential credential;
    private int statusCode;

    public CreateOrderTest(Order order, int statusCode){
        this.order = order;
        this.statusCode = statusCode;
    }


    @Before
    public void setUp(){
        RestAssured.baseURI = BaseURI.baseURI;
        user = UserData.defaultUser();
        userMethods = new UserMethods();
        orderMethods = new OrderMethods();
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);

    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {OrderData.defaultOrder(), 200},
                {OrderData.emptyOrder(), 400},
                {OrderData.orderWithWrongIDs(), 500}
        };
    }

    @Test
    @DisplayName("Создание заказа после авторизации")
    public void  createOrderAfterAuthorisation(){
        userMethods.loginUser(credential.from(user));
        orderMethods.createOrderAuthorisedUser(order, "Bearer " + accessToken)
                .statusCode(statusCode);
    }

    @Test
    @DisplayName("Создание заказа до авторизации")
    public void createOrderBeforeAuthorisation(){
        orderMethods.createOrderNotAuthorisedUser(order)
                .statusCode(statusCode);
    }

    @After
    @DisplayName("Удаление созданного для тестов пользователя ")
    public void cleanDate(){
        if(accessToken != null) userMethods.deleteUser(accessToken);
    }
}
