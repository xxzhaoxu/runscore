package me.zohar.runscore.agent.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.agent.domain.InviteCodeChannelRebate;

@Data
public class InviteCodeChannelRebateVO {

	private String id;

	private String channelId;

	private String channelName;

	private Double rebate;

	public static List<InviteCodeChannelRebateVO> convertFor(
			Collection<InviteCodeChannelRebate> inviteCodeChannelRebates) {
		if (CollectionUtil.isEmpty(inviteCodeChannelRebates)) {
			return new ArrayList<>();
		}
		List<InviteCodeChannelRebateVO> vos = new ArrayList<>();
		for (InviteCodeChannelRebate inviteCodeChannelRebate : inviteCodeChannelRebates) {
			vos.add(convertFor(inviteCodeChannelRebate));
		}
		return vos;
	}

	public static InviteCodeChannelRebateVO convertFor(InviteCodeChannelRebate inviteCodeChannelRebate) {
		if (inviteCodeChannelRebate == null) {
			return null;
		}
		InviteCodeChannelRebateVO vo = new InviteCodeChannelRebateVO();
		BeanUtils.copyProperties(inviteCodeChannelRebate, vo);
		vo.setChannelName(inviteCodeChannelRebate.getChannel().getChannelName());
		return vo;
	}

}
