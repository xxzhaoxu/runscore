package me.zohar.runscore.statisticalanalysis.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantTradeResultsSituation;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;

@Data
public class MerchantTradeResultsSituationVO {

	private String id;

	private String userName;

	private Double totalTradeAmount;

	private Double totalBounty;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double totalSuccessRate;

	private Double monthTradeAmount;

	private Double monthBounty;

	private Long monthPaidOrderNum;

	private Long monthOrderNum;

	private Double monthSuccessRate;

	private Double yesterdayTradeAmount;

	private Double yesterdayBounty;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double todayTradeAmount;

	private Double todayBounty;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

	public static List<MerchantTradeResultsSituationVO> convertFor(List<MerchantTradeResultsSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}
		List<MerchantTradeResultsSituationVO> vos = new ArrayList<>();
		for (MerchantTradeResultsSituation situation : situations) {
			vos.add(convertFor(situation));
		}
		return vos;
	}


	public static MerchantTradeResultsSituationVO convertFor(MerchantTradeResultsSituation situation) {
		if (situation == null) {
			return null;
		}
		MerchantTradeResultsSituationVO vo = new MerchantTradeResultsSituationVO();
		BeanUtils.copyProperties(situation, vo);
		return vo;
	}

}
