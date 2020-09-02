package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.merchant.domain.GatheringChannel;

@Data
public class AddOrUpdateGatheringChannelParam {

	private String id;

	/**
	 * 通道code
	 */
	@NotBlank
	private String channelCode;

	/**
	 * 通道名称
	 */
	@NotBlank
	private String channelName;
	
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double defaultReceiveOrderRabate;

	/**
	 * 是否启用
	 */
	@NotNull
	private Boolean enabled;

	public GatheringChannel convertToPo() {
		GatheringChannel po = new GatheringChannel();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setDeletedFlag(false);
		return po;
	}

}
