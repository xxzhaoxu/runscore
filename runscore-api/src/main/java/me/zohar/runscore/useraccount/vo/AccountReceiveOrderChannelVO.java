package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.useraccount.domain.AccountReceiveOrderChannel;

@Data
public class AccountReceiveOrderChannelVO {

	private String id;

	private Double rebate;

	private String state;

	private String stateName;

	private String userAccountId;

	private String channelId;

	private String channelCode;

	private String channelName;

	public static List<AccountReceiveOrderChannelVO> convertFor(List<AccountReceiveOrderChannel> rebates) {
		if (CollectionUtil.isEmpty(rebates)) {
			return new ArrayList<>();
		}
		List<AccountReceiveOrderChannelVO> vos = new ArrayList<>();
		for (AccountReceiveOrderChannel rebate : rebates) {
			vos.add(convertFor(rebate));
		}
		return vos;
	}

	public static AccountReceiveOrderChannelVO convertFor(AccountReceiveOrderChannel rebate) {
		if (rebate == null) {
			return null;
		}
		AccountReceiveOrderChannelVO vo = new AccountReceiveOrderChannelVO();
		BeanUtils.copyProperties(rebate, vo);
		if (rebate.getChannel() != null) {
			vo.setChannelCode(rebate.getChannel().getChannelCode());
			vo.setChannelName(rebate.getChannel().getChannelName());
		}
		vo.setStateName(DictHolder.getDictItemName("accountReceiveOrderChannelState", vo.getState()));
		return vo;
	}

}
