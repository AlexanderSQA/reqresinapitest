package com.api.managers;

import static io.restassured.RestAssured.given;

import io.restassured.response.ValidatableResponse;


public class ResourceManager extends SpecificationAbs {

  public ValidatableResponse getListSource() {
    return given(spec)
        .when()
        .get("/unknown/")
        .then()
        ;
  }

  public ValidatableResponse getResource(int recourseId) {
    return given(spec)
        .when()
        .get("/unknown/" + recourseId)
        .then();
  }


}
