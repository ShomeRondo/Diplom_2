import config.BaseURI;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredential;
import methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    private User user;
    private UserCredential credential;
    private UserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = BaseURI.BASE_URI;
        user = UserData.defaultUser();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Позитивный тест на авторизацию")
    public void userLogin(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        userMethods.loginUser(credential.from(user))
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с неправильным email")
    public void loginWIthWrongEmail(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        user.setEmail("parirumchik@yandex.ru");
        userMethods.loginUser(credential.from(user))
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }


    @Test
    @DisplayName("Авторизация с неправильным паролем")
    public void loginWithWrongPassword(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        user.setPassword("Парирумчик");
        userMethods.loginUser(credential.from(user))
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }


    @After
    @DisplayName("Удаление созданного для тестов пользователя")
    public void cleanDate(){
        if(accessToken != null) userMethods.deleteUser(accessToken);
    }
}
