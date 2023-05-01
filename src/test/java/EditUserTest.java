import config.BaseURI;
import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredential;
import model.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class EditUserTest {

    private User user;
    private UserMethods userMethods;
    private String accessToken;
    private UserCredential credential;


    @Before
    public void setUp(){
        RestAssured.baseURI = BaseURI.baseURI;
        user = UserData.defaultUser();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    public void editEmailForAuthorisedUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        userMethods.loginUser(credential.from(user));
        ValidatableResponse responseOnEdit = userMethods.editUserWhenAuthorised( new User("parirumchik@yandex.ru", user.getPassword(), user.getName()), "bearer " + accessToken);
        responseOnEdit.assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }


    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void  editPasswordForAuthorisedUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        userMethods.loginUser(credential.from(user));
        ValidatableResponse responseOnEdit = userMethods.editUserWhenAuthorised( new User(user.getEmail(), "Парирумчик", user.getName()), "bearer " + accessToken);
        responseOnEdit.assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void editNameForAuthorisedUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        userMethods.loginUser(credential.from(user));
        ValidatableResponse responseOnEdit = userMethods.editUserWhenAuthorised( new User(user.getEmail(), user.getPassword(), "Джотаро"), "bearer " +accessToken);
        responseOnEdit.assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }


    @Test
    @DisplayName("Изменение email неавторизованного пользователя")
    public void editEmailNotAuthorisedUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        ValidatableResponse responseOnEdit = userMethods.editUserWhenNotAuthorised(new User("parirumchik@yandex.ru", user.getPassword(), user.getName()));
        responseOnEdit.assertThat()
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    public void editPasswordNotAuthorisedUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        ValidatableResponse responseOnEdit = userMethods.editUserWhenNotAuthorised(new User(user.getEmail(), "Парирумчик", user.getName()));
        responseOnEdit.assertThat()
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    public void editNameNotAuthorisedUser(){
        ValidatableResponse response = userMethods.createUser(user);
        accessToken =response.extract().path("accessToken").toString().substring(7);
        ValidatableResponse responseOnEdit = userMethods.editUserWhenNotAuthorised(new User(user.getEmail(), user.getPassword(), "Жи есть"));
        responseOnEdit.assertThat()
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @After
    @DisplayName("Удаление созданного для тестов пользователя")
    public void cleanDate(){
        if(accessToken != null) userMethods.deleteUser(accessToken);
    }
}
