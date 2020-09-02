package me.zohar.runscore.mastercontrol.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateSystemSettingParam {

	/**
	 * 网站标题
	 */
	@NotBlank
	private String websiteTitle;

	@NotBlank
	private String payTechnicalSupport;

	@NotBlank
	private String currencyUnit;

	@NotNull
	private Boolean singleDeviceLoginFlag;

	@NotNull
	private Boolean memberMeEnabled;

	@NotNull
	private Boolean showRankingListFlag;

	@NotNull
	private Boolean showCodeTailoring;

	@NotNull
	private Boolean mobileLoginGoogleAuth;

	@NotNull
	private Boolean backgroundLoginGoogleAuth;

}
