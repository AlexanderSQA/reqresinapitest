package apitests;

import com.api.dto.request.AuthDataReq;
import com.api.dto.response.AuthSuccessResp;
import com.api.dto.response.ErrorResp;
import com.api.managers.ApiModule;
import com.api.managers.AuthManager;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationTests {
  ApplicationContext context = new AnnotationConfigApplicationContext(ApiModule.class);
  private AuthManager authManager = context.getBean(AuthManager.class);
  private final String VALID_EMAIL = "eve.holt@reqres.in";
  private final String VALID_PASSWORD = "pistol123456";

  @Test
  @DisplayName("user registration - success")
  public void checkSuccessRegisterUser() {
    AuthDataReq req = new AuthDataReq(VALID_EMAIL, VALID_PASSWORD);

    AuthSuccessResp resp = authManager.registerUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/RegisterUser.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(AuthSuccessResp.class);

    assertAll(
        () -> assertNotNull(resp.getId()),
        () -> assertNotNull(resp.getToken())
    );
  }

  @Test
  @DisplayName("user registration - unsuccessful without pass")
  public void checkRegisterUserWithoutPass() {
    AuthDataReq req = new AuthDataReq();
    req.setEmail("success@reqres.in");

    ErrorResp resp = authManager.registerUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Missing password", resp.getError());
  }

  @Test
  @DisplayName("user registration - unsuccessful without email")
  public void checkRegisterUserWithoutLogin() {
    AuthDataReq req = new AuthDataReq();
    req.setPassword("123");

    ErrorResp resp = authManager.registerUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Missing email or username", resp.getError());
  }

  @Test
  @DisplayName("user registration - unsuccessful with wrong email")
  public void checkRegisterUserWithWrongEmail() {
    AuthDataReq req = new AuthDataReq();
    req.setEmail("abc@gmail.com");
    req.setPassword("123");

    ErrorResp resp = authManager.registerUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Note: Only defined users succeed registration", resp.getError());
  }

  @Test
  @DisplayName("user registration - unsuccessful without credentials")
  public void checkRegisterUserWithoutCredentials() {
    AuthDataReq req = new AuthDataReq();

    ErrorResp resp = authManager.registerUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Missing email or username", resp.getError());
  }

}
