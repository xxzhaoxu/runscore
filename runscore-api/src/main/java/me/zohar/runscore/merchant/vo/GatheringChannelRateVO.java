package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.merchant.domain.GatheringChannelRate;

@Data
public class GatheringChannelRateVO {

	private String id;

	/**
	 * 费率
	 */
	private Double rate;
	
	private Double minAmount;
	
	private Double maxAmount;

	private String channelId;

	/**
	 * 通道code
	 */
	private String channelCode;

	/**
	 * 通道名称
	 */
	private String channelName;

	private String merchantId;

	/**
	 * 商户号
	 */
	private String merchantNum;

	private String merchantName;

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static List<GatheringChannelRateVO> convertFor(List<GatheringChannelRate> rates) {
		if (CollectionUtil.isEmpty(rates)) {
			return new ArrayList<>();
		}
		List<GatheringChannelRateVO> vos = new ArrayList<>();
		for (GatheringChannelRate rate : rates) {
			vos.add(convertFor(rate));
		}
		return vos;
	}

	public static GatheringChannelRateVO convertFor(GatheringChannelRate rate) {
		if (rate == null) {
			return null;
		}
		GatheringChannelRateVO vo = new GatheringChannelRateVO();
		BeanUtils.copyProperties(rate, vo);
		if (rate.getChannel() != null) {
			vo.setChannelCode(rate.getChannel().getChannelCode());
			vo.setChannelName(rate.getChannel().getChannelName());
		}
		if (rate.getMerchant() != null) {
			vo.setMerchantNum(rate.getMerchant().getMerchantNum());
			vo.setMerchantName(rate.getMerchant().getMerchantName());
		}
		return vo;
	}

}
