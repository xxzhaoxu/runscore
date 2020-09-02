package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.domain.MerchantOrder;
import me.zohar.runscore.merchant.domain.MerchantOrderPayInfo;

@Data
public class StartOrderParam {

	/**
	 * 商户号
	 */
	@NotBlank
	private String merchantNum;

	/**
	 * 商户订单号
	 */
	@NotBlank
	private String orderNo;

	@NotBlank
	private String payType;

	/**
	 * 支付金额
	 */
	@NotBlank
	private String amount;

	@NotBlank
	private String notifyUrl;

	private String returnUrl;

	private String attch;

	@NotBlank
	private String sign;

	private String specifiedReceivedAccountId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date publishTime;

	private String ip;

	private String province;

	private String city;

	private String cityCode;

	public MerchantOrder convertToPo(String merchantId, String gatheringChannelId, Double rate,
			Integer orderEffectiveDuration) {
		MerchantOrder po = new MerchantOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setMerchantId(merchantId);
		po.setGatheringChannelId(gatheringChannelId);
		po.setGatheringAmount(Double.parseDouble(this.getAmount()));
		po.setSubmitTime(new Date());
		po.setOrderState(Constant.商户订单状态_等待接单);
		po.setRate(rate);
		po.setUsefulTime(DateUtil.offset(po.getSubmitTime(), DateField.MINUTE, orderEffectiveDuration));
		if (StrUtil.isBlank(po.getSpecifiedReceivedAccountId())) {
			po.setSpecifiedReceivedAccountId(null);
		}
		if (this.getPublishTime() != null) {
			po.setOrderState(Constant.商户订单状态_定时发单);
			po.setSubmitTime(this.getPublishTime());
			po.setUsefulTime(DateUtil.offset(po.getSubmitTime(), DateField.MINUTE, orderEffectiveDuration));
		}
		return po;
	}

	public MerchantOrderPayInfo convertToPayInfoPo(String merchantOrderId) {
		MerchantOrderPayInfo po = new MerchantOrderPayInfo();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setMerchantOrderId(merchantOrderId);
		po.setNoticeState(Constant.商户订单支付通知状态_未通知);
		return po;
	}

}
