package me.zohar.runscore.mastercontrol.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateReceiveOrderSettingParam {

	@NotNull
	private Boolean stopStartAndReceiveOrder;

	@NotNull
	private Boolean banReceiveRepeatOrder;

	@NotNull
	private Boolean banReceiveCodeOrder;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer receiveOrderEffectiveDuration;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer orderPayEffectiveDuration;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer receiveOrderInterval;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer receiveOrderAppealInterval;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer receiveOrderUpperLimit;

	@NotNull
	private Boolean showAllOrder;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double cashDepositMinimumRequire;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double cashPledge;

	@NotNull
	private Boolean unfixedGatheringCodeReceiveOrder;

	@NotNull
	private Boolean gatheringCodeGoogleAuth;

	@NotNull
	private Boolean receiveOrderSound;

	@NotNull
	private Boolean auditGatheringCode;

	@NotNull
	private Boolean openAutoReceiveOrder;

	@NotNull
	private Boolean freezeModelEnabled;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer freezeEffectiveDuration;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer unconfirmedAutoFreezeDuration;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Integer noOpsStopReceiveOrder;

	@NotNull
	private Boolean dispatchMode;

	@NotNull
	private Boolean sameCityPriority;

	@NotNull
	private Boolean gatheringCodeEverydayUsedUpperLimit;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Long gatheringCodeUsedUpperLimit;

}
