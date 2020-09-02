package me.zohar.runscore.agent.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.agent.domain.InviteCodeChannelRebate;
import me.zohar.runscore.common.utils.IdUtils;

@Data
public class ChannelRebateParam {

	@NotBlank
	private String channelId;

	/**
	 * 返点
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double rebate;

	public InviteCodeChannelRebate convertToPo(String inviteCodeId) {
		InviteCodeChannelRebate po = new InviteCodeChannelRebate();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setInviteCodeId(inviteCodeId);
		return po;
	}

}
