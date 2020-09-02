package me.zohar.runscore.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateCityInfoParam {

	@NotBlank
	private String userAccountId;

	@NotBlank
	private String province;

	@NotBlank
	private String city;

	@NotBlank
	private String cityCode;

}
