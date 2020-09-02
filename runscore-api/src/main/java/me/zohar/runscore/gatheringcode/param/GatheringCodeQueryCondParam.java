package me.zohar.runscore.gatheringcode.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper=false)
public class GatheringCodeQueryCondParam extends PageParam {

	private String state;

	/**
	 * 收款通道id
	 */
	private String gatheringChannelId;

	private String payee;

	private String userName;

	private String inviterUserName;

	/**
	 * 用户账号id
	 */
	private String userAccountId;


}
