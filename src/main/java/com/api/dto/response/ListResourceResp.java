package com.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListResourceResp {
  @JsonProperty("per_page")
  private Integer perPage;
  private Integer total;
  private List<SingleUserResp> data;
  private Integer page;
  @JsonProperty("total_pages")
  private Integer totalPages;
  private SupportResp support;
}
