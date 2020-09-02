package me.zohar.runscore.useraccount.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.useraccount.domain.UserAccount;

/**
 * 用户账号信息vo
 *
 * @author zohar
 * @date 2018年12月27日
 *
 */
@Data
public class UserAccountInfoVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;

	private String state;

	private String mobile;

	private String secretKey;

	/**
	 * 账号类型
	 */
	private String accountType;

	private String accountTypeName;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 保证金
	 */
	private Double cashDeposit;

	/**
	 * 接单状态
	 */
	private String receiveOrderState;

	private Integer inviteCodeQuota;

	private String cardWithStorageId;

	private String cardTheStorageId;

	private String cardIsStorageId;

	private String roleId;

	public static UserAccountInfoVO convertFor(UserAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		UserAccountInfoVO vo = new UserAccountInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		vo.setAccountTypeName(DictHolder.getDictItemName("accountType", vo.getAccountType()));
		return vo;
	}

}
