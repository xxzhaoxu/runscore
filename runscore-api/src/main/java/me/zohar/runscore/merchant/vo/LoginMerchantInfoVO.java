package me.zohar.runscore.merchant.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.merchant.domain.Merchant;

@Data
public class LoginMerchantInfoVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 登录密码
	 */
	private String loginPwd;

	/**
	 * 商户号
	 */
	private String merchantNum;

	/**
	 * 接入商户名称
	 */
	private String merchantName;

	/**
	 * 状态
	 */
	private String state;

	public static LoginMerchantInfoVO convertFor(Merchant merchant) {
		if (merchant == null) {
			return null;
		}
		LoginMerchantInfoVO vo = new LoginMerchantInfoVO();
		BeanUtils.copyProperties(merchant, vo);
		return vo;
	}

}
