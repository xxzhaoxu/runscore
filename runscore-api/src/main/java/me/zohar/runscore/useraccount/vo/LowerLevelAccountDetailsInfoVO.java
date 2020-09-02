package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.useraccount.domain.UserAccount;

@Data
public class LowerLevelAccountDetailsInfoVO {

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

	/**
	 * 账号类型
	 */
	private String accountType;

	private String accountTypeName;

	/**
	 * 账号级别
	 */
	private Integer accountLevel;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 保证金
	 */
	private Double cashDeposit;

	/**
	 * 状态
	 */
	private String state;

	private String stateName;

	/**
	 * 注册时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registeredTime;

	/**
	 * 最近登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyLoginTime;

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;

	/**
	 * 银行资料最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bankInfoLatelyModifyTime;

	/**
	 * 接单状态
	 */
	private String receiveOrderState;

	private String receiveOrderStateName;

	private String inviterId;

	/**
	 * 邀请人
	 */
	private String inviterUserName;

	private List<AccountReceiveOrderChannelVO> receiveOrderChannels;

	public static List<LowerLevelAccountDetailsInfoVO> convertFor(List<UserAccount> userAccounts) {
		if (CollectionUtil.isEmpty(userAccounts)) {
			return new ArrayList<>();
		}
		List<LowerLevelAccountDetailsInfoVO> vos = new ArrayList<>();
		for (UserAccount userAccount : userAccounts) {
			vos.add(convertFor(userAccount));
		}
		return vos;
	}

	public static LowerLevelAccountDetailsInfoVO convertFor(UserAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		LowerLevelAccountDetailsInfoVO vo = new LowerLevelAccountDetailsInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		vo.setAccountTypeName(DictHolder.getDictItemName("accountType", vo.getAccountType()));
		vo.setStateName(DictHolder.getDictItemName("accountState", vo.getState()));
		vo.setReceiveOrderStateName(DictHolder.getDictItemName("receiveOrderState", vo.getReceiveOrderState()));
		if (userAccount.getInviter() != null) {
			vo.setInviterUserName(userAccount.getInviter().getUserName());
		}
		vo.setReceiveOrderChannels(
				AccountReceiveOrderChannelVO.convertFor(new ArrayList<>(userAccount.getReceiveOrderChannels())));
		return vo;
	}

}
