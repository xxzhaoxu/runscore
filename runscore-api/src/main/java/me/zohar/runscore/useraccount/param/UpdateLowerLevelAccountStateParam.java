package me.zohar.runscore.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateLowerLevelAccountStateParam {

	@NotBlank
	private String userAccountId;

	@NotBlank
	private String state;

}
