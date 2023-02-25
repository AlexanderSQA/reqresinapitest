package com.api.managers;

import static io.restassured.RestAssured.given;

import com.api.dto.request.UserCreateReq;
import io.restassured.response.ValidatableResponse;


public class UserManager extends SpecificationAbs {

  public ValidatableResponse getListUsers(int page) {
    return given(spec)
        .when()
        .get("/users?page=" + page)
        .then();
  }

  public ValidatableResponse getListUsers(int page, int delay) {
    return given(spec)
        .when()
        .get("/users?page=" + page)
        .then();
  }

  public ValidatableResponse getSingleUser(String user) {
    return given(spec)
        .when()
        .get("/users/" + user)
        .then();
  }

  public ValidatableResponse createNewUser(UserCreateReq userBody) {
    return given(spec)
        .body(userBody)
        .when()
        .post("/users")
        .then();
  }

  public ValidatableResponse updateUser(int userId,UserCreateReq userBody) {
    return given(spec)
        .body(userBody)
        .when()
        .patch("/users/" + userId)
        .then();
  }

  public ValidatableResponse deleteUser(int userId) {
    return given(spec)
        .when()
        .delete("/users/" + userId)
        .then();
  }

}
