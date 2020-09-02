package me.zohar.runscore.useraccount.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdjustInviteCodeQuotaParam {

	/**
	 * 用户账号id
	 */
	@NotBlank
	private String userAccountId;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer inviteCodeQuota;

}
