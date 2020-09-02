package me.zohar.runscore.merchant.param;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class QuickSettingRebateParam {

	@NotBlank
	private String channelId;

	private List<Double> rebates;

}
