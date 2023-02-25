package com.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataResp {
  @JsonProperty("last_name")
  private String lastName;
  private Integer id;
  private String avatar;
  @JsonProperty("first_name")
  private String firstName;
  private String email;
}
