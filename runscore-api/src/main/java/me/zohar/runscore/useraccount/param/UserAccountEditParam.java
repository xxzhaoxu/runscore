package me.zohar.runscore.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserAccountEditParam {

	/**
	 * 主键id
	 */
	@NotBlank
	private String id;

	/**
	 * 用户名
	 */
	@NotBlank
	private String userName;

	/**
	 * 真实姓名
	 */
	@NotBlank
	private String realName;

	@NotBlank
	private String mobile;

	/**
	 * 账号类型
	 */
	@NotBlank
	private String accountType;

	/**
	 * 状态
	 */
	@NotBlank
	private String state;

	private String roleId;

}
