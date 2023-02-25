package apitests;

import static org.junit.jupiter.api.Assertions.*;

import com.api.dto.request.UserCreateReq;
import com.api.dto.response.*;
import com.api.managers.ApiModule;
import com.api.managers.UserManager;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;
import java.util.stream.Collectors;


public class UserTests {
  ApplicationContext context = new AnnotationConfigApplicationContext(ApiModule.class);
  private final UserManager userManager = context.getBean(UserManager.class);
  private final UserCreateReq userBody = new UserCreateReq("Neo", "Saver");
  private final UserCreateReq userBodyForUpdate = new UserCreateReq("Neo", "resident");

  @Test
  @DisplayName("Get Users List")
  public void checkRootInfo() {
    int page = 2;
    ListUserResp getRootInfo = userManager.getListUsers(page)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ListUsers.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(ListUserResp.class);


    assertAll(
        () -> assertEquals(2, getRootInfo.getPage()),
        () -> assertEquals(6, getRootInfo.getPerPage()),
        () -> assertEquals(12, getRootInfo.getTotal()),
        () -> assertEquals(2, getRootInfo.getTotalPages())
    );
  }

  @Test
  @DisplayName("Check avatar info")
  public void checkAvatarId() {
    int page = 1;
    List<UserDataResp> getUsersList = userManager.getListUsers(page)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ListUsers.json"))
        .log().all().assertThat().statusCode(200)
        .extract().body().jsonPath().getList("data", UserDataResp.class);

    List<String> avatars = getUsersList.stream().map(UserDataResp::getAvatar).collect(Collectors.toList());
    List<String> ids = getUsersList.stream().map(x -> x.getId().toString()).collect(Collectors.toList());

    for (int i = 0; i < avatars.size(); i++) {
      Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
      Assertions.assertTrue(avatars.get(i).endsWith(".jpg"));
    }
  }

  @Test
  @DisplayName("Get single user - success")
  public void getSingleUser() {
    String userId = "2";
    SingleUserResp singleUser = userManager.getSingleUser(userId)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/SingleUser.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(SingleUserResp.class);

    assertAll(
        () -> assertEquals(userId, singleUser.getData().getId().toString()),
        () -> assertEquals("Janet", singleUser.getData().getFirstName()),
        () -> assertEquals("Weaver", singleUser.getData().getLastName()),
        () -> assertTrue(singleUser.getData().getEmail().endsWith("@reqres.in")),
        () -> assertEquals("https://reqres.in/img/faces/" + userId + "-image.jpg", singleUser.getData().getAvatar()),
        () -> assertNotNull(singleUser.getSupport())
    );
  }

  @Test
  @DisplayName("Get single user - not found")
  public void getSingleUserNotFound() {
    String userId = "22";
    SingleUserResp singleUser = userManager.getSingleUser(userId)
        .log().all().assertThat().statusCode(404)
        .extract().as(SingleUserResp.class);

    Assertions.assertNull(singleUser.getData());
  }

  @Test
  @DisplayName("Create new user - success")
  public void createNewUser() {
    UserCreateResp resp = userManager.createNewUser(userBody)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/CreateUser.json"))
        .log().all().assertThat().statusCode(201)
        .extract().as(UserCreateResp.class);

    assertAll(
        () -> assertEquals(userBody.getName(), resp.getName()),
        () -> assertEquals(userBody.getJob(), resp.getJob()),
        () -> assertFalse(resp.getId().isEmpty()),
        () -> assertFalse(resp.getCreatedAt().isEmpty())
    );
  }

  @Test
  @DisplayName("Update user by put - success")
  public void updateUserByPut() {
    int userId = 2;
    UserUpdateResp resp = userManager.updateUser(userId, userBodyForUpdate)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/UpdateUser.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(UserUpdateResp.class);

    assertAll(
        () -> assertEquals(userBodyForUpdate.getName(), resp.getName()),
        () -> assertEquals(userBodyForUpdate.getJob(), resp.getJob()),
        () -> assertFalse(resp.getUpdatedAt().isEmpty())
    );
  }

  @Test
  @DisplayName("Delete user - success")
  public void deleteUser() {
    int userId = 2;
    ValidatableResponse resp = userManager.deleteUser(userId)
        .log().all().assertThat().statusCode(204);

  }

  @Test
  @DisplayName("Git users list with delay")
  public void checkLoadingUsersListWithDelay() {
    int page = 2;
    int delay = 5;
    ListUserResp resp = userManager.getListUsers(page, delay)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ListUsers.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(ListUserResp.class);

    assertAll(
        () -> assertEquals(resp.getPerPage(), resp.getData().size()),
        () -> assertNotNull(resp.getSupport())
    );
  }
}
