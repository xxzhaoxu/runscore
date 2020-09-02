package me.zohar.runscore.statisticalanalysis.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantTradeSituation;

@Data
public class MerchantTradeSituationVO {

	private String id;

	private String merchantName;

	private Double tradeAmount;

	private Double poundage;

	private Double actualIncome;

	private Long paidOrderNum;

	private Long orderNum;

	private Double successRate;

	private Double withdrawAmount;

	private Double totalTradeAmount;

	private Double totalPoundage;

	private Double totalActualIncome;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double totalSuccessRate;

	private Double monthTradeAmount;

	private Double monthPoundage;

	private Double monthActualIncome;

	private Long monthPaidOrderNum;

	private Long monthOrderNum;

	private Double monthSuccessRate;

	private Double yesterdayTradeAmount;

	private Double yesterdayPoundage;

	private Double yesterdayActualIncome;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double todayTradeAmount;

	private Double todayPoundage;

	private Double todayActualIncome;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

	public static List<MerchantTradeSituationVO> convertFor(List<MerchantTradeSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}
		List<MerchantTradeSituationVO> vos = new ArrayList<>();
		for (MerchantTradeSituation situation : situations) {
			vos.add(convertFor(situation));
		}
		return vos;
	}

	public static MerchantTradeSituationVO convertFor(MerchantTradeSituation situation) {
		if (situation == null) {
			return null;
		}
		MerchantTradeSituationVO vo = new MerchantTradeSituationVO();
		BeanUtils.copyProperties(situation, vo);
		return vo;
	}

}
