import config.BaseURI;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.User;
import methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private User user;
    private UserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = BaseURI.BASE_URI;
        user = UserData.defaultUser();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Позитивный тест на создание пользователя")
    public void userIsCreated(){
        ValidatableResponse response = userMethods.createUser(user);
        response.statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken").toString().substring(7);
    }

    @Test
    @DisplayName("Создание пользователя, если такой email уже есть в БД")
    public void duplicateUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        user.setPassword("парирумчик");
        ValidatableResponse responseForDuplication = userMethods.createUser(user);
        responseForDuplication.statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutLogin(){
        user = UserData.userWithoutEmail();
        ValidatableResponse response = userMethods.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPassword(){
        user = UserData.userWithoutPassword();
        ValidatableResponse response = userMethods.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutName(){
        user = UserData.userWithoutName();
        ValidatableResponse response = userMethods.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @After
    @DisplayName("Удаление созданного для тестов пользователя")
    public void cleanDate(){
        if(accessToken != null) userMethods.deleteUser(accessToken);
    }


}
