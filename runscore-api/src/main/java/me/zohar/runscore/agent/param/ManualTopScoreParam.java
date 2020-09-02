package me.zohar.runscore.agent.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ManualTopScoreParam {

	@NotBlank
	private String currentAccountId;

	@NotBlank
	private String topScoreAccountId;

	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double topScoreAmount;

}
