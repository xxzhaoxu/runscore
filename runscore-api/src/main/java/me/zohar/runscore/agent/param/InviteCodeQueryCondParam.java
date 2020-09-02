package me.zohar.runscore.agent.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class InviteCodeQueryCondParam extends PageParam {

	private String userAccountId;
	
	private String state;

}
