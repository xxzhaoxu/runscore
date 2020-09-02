package me.zohar.runscore.merchant.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class GatheringChannelQueryCondParam extends PageParam {

	/**
	 * 通道code
	 */
	private String channelCode;

	/**
	 * 通道名称
	 */
	private String channelName;

}
