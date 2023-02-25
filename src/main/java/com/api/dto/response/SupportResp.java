package com.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class SupportResp {
	private String text;
	private String url;

}
