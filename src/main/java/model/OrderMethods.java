package model;

import io.restassured.response.ValidatableResponse;
import static config.Endpoints.*;


import static io.restassured.RestAssured.given;

public class OrderMethods {

    //создание заказа авторизованным пользователем
    public ValidatableResponse createOrderAuthorisedUser(Order order, String accessToken){
        return given()
                .header("Content-type", "application/json")
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    //создание заказа без авторизации
    public ValidatableResponse createOrderNotAuthorisedUser(Order order){
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    //получение списка заказов авторизованным пользователем
    public ValidatableResponse gerOrdersForAuthorisedUser(String accessToken){
        return given()
                .header("Content-type", "application/json")
                .header("authorization", accessToken)
                .when()
                .get(ORDER)
                .then();
    }

    //получение списка заказов до авторизации
    public ValidatableResponse getOrdersNotAuthorisedUser(){
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDER)
                .then();
    }
}
