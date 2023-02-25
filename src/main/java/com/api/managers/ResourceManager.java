package com.api.managers;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ResourceManager extends SpecificationAbs {

  public ValidatableResponse getListSource() {
    return given(spec)
        .when()
        .get("/unknown/" )
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
