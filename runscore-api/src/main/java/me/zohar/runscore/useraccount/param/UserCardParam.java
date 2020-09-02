package me.zohar.runscore.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserCardParam {

	/**
	 * 手持身份证
	 */
	@NotBlank
	private String cardWithStorageId;

	/**
	 * 身份证反面
	 */
	@NotBlank
	private String cardTheStorageId;

	/**
	 * 身份证正面
	 */
	@NotBlank
	private String cardIsStorageId;

}
