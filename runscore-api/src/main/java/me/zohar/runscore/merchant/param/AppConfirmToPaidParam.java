package me.zohar.runscore.merchant.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AppConfirmToPaidParam {

	@NotBlank
	private String secretKey;

	@NotBlank
	private String amount;

	@NotBlank
	private String type;

	@NotBlank
	private String payee;

	@NotBlank
	private String sign;

}
