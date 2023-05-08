package methods;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredential;

import static config.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserMethods {


    @Step("создание пользователя")
    public ValidatableResponse createUser(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER_USER)
                .then();
    }

    @Step("логин пользователя")
    public ValidatableResponse loginUser(UserCredential credential){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credential)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("изменение пользователя до авторизации")
    public ValidatableResponse editUserWhenNotAuthorised(User user){

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(USER)
                .then();
    }

    @Step("изменение пользователя после авторизации")
    public ValidatableResponse editUserWhenAuthorised(User user, String accessToken){
        return given()
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(USER)
                .then();
    }

    @Step("удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .auth().oauth2(accessToken)
                .when()
                .delete(USER)
                .then();
    }
}
