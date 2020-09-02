package me.zohar.runscore.agent.param;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zohar.runscore.agent.domain.InviteCode;
import me.zohar.runscore.common.utils.IdUtils;

@Data
public class GenerateInviteCodeParam {

	@NotBlank
	private String accountType;

	@NotEmpty
	private List<ChannelRebateParam> channelRebates;

	/**
	 * 邀请人账号id
	 */
	@NotBlank
	private String inviterId;

	public InviteCode convertToPo(String code, Integer effectiveDuration) {
		InviteCode po = new InviteCode();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setUsed(false);
		po.setCode(code);
		po.setCreateTime(new Date());
		po.setPeriodOfValidity(DateUtil.offset(po.getCreateTime(), DateField.DAY_OF_YEAR, effectiveDuration));
		return po;
	}

}
