package me.zohar.runscore.mastercontrol.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateWithdrawSettingParam {

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer everydayWithdrawTimesUpperLimit;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double withdrawLowerLimit;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double withdrawUpperLimit;

	@NotBlank
	private String withdrawExplain;

}
