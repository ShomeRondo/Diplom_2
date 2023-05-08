package model;

import io.restassured.response.ValidatableResponse;

import static config.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserMethods {

    //создание пользователя
    public ValidatableResponse createUser(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER_USER)
                .then();
    }

    //логин пользователя
    public ValidatableResponse loginUser(UserCredential credential){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credential)
                .when()
                .post(LOGIN)
                .then();
    }

    //изменение пользователя
    public ValidatableResponse editUserWhenNotAuthorised(User user){

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(USER)
                .then();
    }

    public ValidatableResponse editUserWhenAuthorised(User user, String accessToken){
        return given()
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(USER)
                .then();
    }

    //удаление пользователя
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .auth().oauth2(accessToken)
                .when()
                .delete(USER)
                .then();
    }
}
