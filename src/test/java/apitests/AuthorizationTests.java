package apitests;

import com.api.dto.request.AuthDataReq;
import com.api.dto.response.ErrorResp;
import com.api.dto.response.AuthSuccessResp;
import com.api.managers.ApiModule;
import com.api.managers.AuthManager;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationTests {
  ApplicationContext context = new AnnotationConfigApplicationContext(ApiModule.class);
  private AuthManager authManager = context.getBean(AuthManager.class);
  private final String VALID_EMAIL = "eve.holt@reqres.in";
  private final String VALID_PASSWORD = "pistol123456";



  @Test
  @DisplayName("user login - success")
  public void checkUserLogin() {
    AuthDataReq req = new AuthDataReq(VALID_EMAIL, VALID_PASSWORD);

    AuthSuccessResp resp = authManager.loginUser(req)
        .body("token", notNullValue())
        .log().all().assertThat().statusCode(200)
        .extract().as(AuthSuccessResp.class);

    Assertions.assertNotNull(resp.getToken());
  }

  @Test
  @DisplayName("user login - unsuccessful without pass")
  public void checkLoginUserWithoutPass() {
    AuthDataReq req = new AuthDataReq();
    req.setEmail("peter@klaven");

    ErrorResp resp = authManager.loginUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Missing password", resp.getError());
  }

  @Test
  @DisplayName("user login - unsuccessful without email")
  public void checkUserLoginWithoutEmail() {
    AuthDataReq req = new AuthDataReq();
    req.setPassword("123456qwer");

    ErrorResp resp = authManager.loginUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Missing email", resp.getError());
  }

  @Test
  @DisplayName("user login - unsuccessful without credentials")
  public void checkUserLoginWithoutCredentials() {
    AuthDataReq req = new AuthDataReq();

    ErrorResp resp = authManager.loginUser(req)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ErrorResp.json"))
        .log().all().assertThat().statusCode(400)
        .extract().as(ErrorResp.class);

    Assertions.assertEquals("Missing email or username", resp.getError());
  }
}
