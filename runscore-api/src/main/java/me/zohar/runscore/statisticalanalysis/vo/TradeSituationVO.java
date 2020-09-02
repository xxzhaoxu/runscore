package me.zohar.runscore.statisticalanalysis.vo;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.TradeSituation;

@Data
public class TradeSituationVO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private Double totalTradeAmount;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double totalSuccessRate;

	private Double monthTradeAmount;

	private Long monthPaidOrderNum;

	private Long monthOrderNum;

	private Double monthSuccessRate;

	private Double yesterdayTradeAmount;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double todayTradeAmount;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

	public static TradeSituationVO convertFor(TradeSituation tradeSituation) {
		if (tradeSituation == null) {
			return null;
		}
		TradeSituationVO vo = new TradeSituationVO();
		BeanUtils.copyProperties(tradeSituation, vo);
		return vo;
	}

}
