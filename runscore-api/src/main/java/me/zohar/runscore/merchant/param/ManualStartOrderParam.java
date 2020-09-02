package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ManualStartOrderParam {

	/**
	 * 商户号
	 */
	@NotBlank
	private String merchantNum;

	/**
	 * 收款渠道
	 */
	@NotBlank
	private String gatheringChannelCode;

	/**
	 * 收款金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double gatheringAmount;

	/**
	 * 商户订单号
	 */
	@NotBlank
	private String orderNo;

	@NotBlank
	private String notifyUrl;

	private String returnUrl;

	private String attch;

	private String sign;

	private String specifiedReceivedAccountId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date publishTime;

}
