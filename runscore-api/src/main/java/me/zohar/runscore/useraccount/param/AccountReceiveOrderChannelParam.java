package me.zohar.runscore.useraccount.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.useraccount.domain.AccountReceiveOrderChannel;

@Data
public class AccountReceiveOrderChannelParam {

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double rebate;

	@NotBlank
	private String state;

	@NotBlank
	private String channelId;

	public AccountReceiveOrderChannel convertToPo() {
		AccountReceiveOrderChannel po = new AccountReceiveOrderChannel();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		return po;
	}

}
