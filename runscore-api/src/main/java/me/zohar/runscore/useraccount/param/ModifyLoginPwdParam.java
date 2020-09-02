package me.zohar.runscore.useraccount.param;

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

	/**
	 * 用户账号id
	 */
	@NotBlank
	private String userAccountId;

}
