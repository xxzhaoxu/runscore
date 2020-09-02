package me.zohar.runscore.agent.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.runscore.agent.domain.InviteCode;
import me.zohar.runscore.dictconfig.ConfigHolder;
import me.zohar.runscore.dictconfig.DictHolder;

@Data
public class InviteCodeVO {

	private String id;

	/**
	 * 邀请码
	 */
	private String code;

	/**
	 * 账号类型
	 */
	private String accountType;

	private String accountTypeName;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 有效期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date periodOfValidity;

	private Boolean used;

	/**
	 * 邀请人id
	 */
	private String inviterId;

	/**
	 * 有效标识
	 */
	private Boolean validFlag;

	private String inviteRegisterLink;

	public static InviteCodeVO convertFor(InviteCode inviteCode) {
		if (inviteCode == null) {
			return null;
		}
		InviteCodeVO vo = new InviteCodeVO();
		BeanUtils.copyProperties(inviteCode, vo);
		vo.setAccountTypeName(DictHolder.getDictItemName("accountType", vo.getAccountType()));
		vo.setValidFlag(vo.getPeriodOfValidity().getTime() > new Date().getTime());
		vo.setInviteRegisterLink(ConfigHolder.getConfigValue("register.inviteRegisterLink") + vo.getCode());

		return vo;
	}

}
