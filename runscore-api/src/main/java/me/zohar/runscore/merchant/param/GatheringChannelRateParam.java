package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.merchant.domain.GatheringChannelRate;

@Data
public class GatheringChannelRateParam {

	@NotBlank
	private String channelId;

	/**
	 * 费率
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double rate;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double minAmount;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double maxAmount;

	/**
	 * 是否启用
	 */
	@NotNull
	private Boolean enabled;

	public GatheringChannelRate convertToPo(String merchantId) {
		GatheringChannelRate po = new GatheringChannelRate();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setMerchantId(merchantId);
		po.setCreateTime(new Date());
		return po;
	}

}
