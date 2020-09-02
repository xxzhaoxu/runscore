package me.zohar.runscore.mastercontrol.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateCustomerQrcodeSettingParam {

	@NotBlank
	private String customerServiceExplain;

	@NotBlank
	private String customerServiceUrl;

	@NotBlank
	private String qrcodeStorageId;

}
