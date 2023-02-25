package apitests;

import com.api.dto.response.ListResourceResp;
import com.api.dto.response.SingleResourceResp;
import com.api.managers.ApiModule;
import com.api.managers.ResourceManager;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceTests {
  ApplicationContext context = new AnnotationConfigApplicationContext(ApiModule.class);
  private ResourceManager resourceManager = context.getBean(ResourceManager.class);

  @Test
  @DisplayName("Get Resource List - success")
  public void getResourceList() {

    ListResourceResp listResourceResp = resourceManager.getListSource()
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ResourceList.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(ListResourceResp.class);

    assertAll(
        () -> assertEquals(listResourceResp.getPerPage(), listResourceResp.getData().size()),
        () -> assertNotNull(listResourceResp.getData().get(0)),
        () -> assertNotNull(listResourceResp.getSupport())
    );
  }

  @Test
  @DisplayName("Get single Resource - success")
  public void getSingleResource() {
    int resourceId = 2;
    SingleResourceResp resp = resourceManager.getResource(resourceId)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/ResourceSingle.json"))
        .log().all().assertThat().statusCode(200)
        .extract().as(SingleResourceResp.class);

    assertAll(
        () -> assertEquals(resourceId, resp.getData().getId()),
        () -> assertEquals("fuchsia rose", resp.getData().getName()),
        () -> assertEquals(2001, resp.getData().getYear()),
        () -> assertEquals("#C74375", resp.getData().getColor()),
        () -> assertEquals("17-2031", resp.getData().getPantoneValue()),
        () -> assertNotNull(resp.getSupport())
    );
  }

  @Test
  @DisplayName("Get single Resource - not found")
  public void checkSingleResourceNotFound() {
    int resourceId = 25;
    SingleResourceResp resp = resourceManager.getResource(resourceId)
        .log().all().assertThat().statusCode(404)
        .extract().as(SingleResourceResp.class);

    Assertions.assertNull(resp.getData());
  }
}
