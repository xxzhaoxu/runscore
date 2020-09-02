package me.zohar.runscore.merchant.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ModifyLoginPwdParam {

	/**
	 * 旧的登录密码
	 */
	@NotBlank
	private String oldLoginPwd;

	/**
	 * 新的登录密码
	 */
	@NotBlank
	private String newLoginPwd;

	@NotBlank
	private String merchantId;

}
