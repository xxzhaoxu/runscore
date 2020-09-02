package me.zohar.runscore.agent.vo;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.agent.domain.InviteCode;

@Data
public class InviteCodeDetailsVO {

	private InviteCodeVO inviteCode;

	private List<InviteCodeChannelRebateVO> inviteCodeChannelRebates;

	public static List<InviteCodeDetailsVO> convertFor(List<InviteCode> inviteCodes) {
		if (CollectionUtil.isEmpty(inviteCodes)) {
			return new ArrayList<>();
		}
		List<InviteCodeDetailsVO> vos = new ArrayList<>();
		for (InviteCode inviteCode : inviteCodes) {
			vos.add(convertFor(inviteCode));
		}
		return vos;
	}

	public static InviteCodeDetailsVO convertFor(InviteCode inviteCode) {
		if (inviteCode == null) {
			return null;
		}
		InviteCodeDetailsVO vo = new InviteCodeDetailsVO();
		vo.setInviteCode(InviteCodeVO.convertFor(inviteCode));
		vo.setInviteCodeChannelRebates(InviteCodeChannelRebateVO.convertFor(inviteCode.getInviteCodeChannelRebates()));
		return vo;
	}

}
