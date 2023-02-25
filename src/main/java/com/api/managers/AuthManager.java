package com.api.managers;

import com.api.dto.request.AuthDataReq;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class AuthManager extends SpecificationAbs {

  public ValidatableResponse registerUser(AuthDataReq reqBody) {
    return given(spec)
        .body(reqBody)
        .when()
        .post("/register")
        .then();
  }

  public ValidatableResponse loginUser(AuthDataReq reqBody) {
    return given(spec)
        .body(reqBody)
        .when()
        .post("/login")
        .then();
  }
}
