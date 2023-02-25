package com.api.managers;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public abstract class SpecificationAbs {
  public static final String BASE_URL = System.getProperty("base_url").toLowerCase(Locale.ROOT);
  protected final RequestSpecification spec;

  @Autowired
  public SpecificationAbs() {
    spec = given()
        .baseUri(BASE_URL)
        .basePath("/api")
        .contentType(ContentType.JSON);
  }

}
