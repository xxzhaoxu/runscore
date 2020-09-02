package me.zohar.runscore.useraccount.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserAccountQueryCondParam extends PageParam {

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 邀请人
	 */
	private String inviterUserName;

	/**
	 * 账号类型
	 */
	private String accountType;


}
