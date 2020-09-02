package me.zohar.runscore.statisticalanalysis.vo;

import java.io.Serializable;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.RechargeSituation;

import org.springframework.beans.BeanUtils;

@Data
public class RechargeSituationVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Double totalTradeAmount;

	private Long totalSuccessOrderNum;

	private Long totalOrderNum;

	private Double monthTradeAmount;

	private Long monthSuccessOrderNum;

	private Long monthOrderNum;

	private Double yesterdayTradeAmount;

	private Long yesterdaySuccessOrderNum;

	private Long yesterdayOrderNum;

	private Double todayTradeAmount;

	private Long todaySuccessOrderNum;

	private Long todayOrderNum;

	public static RechargeSituationVO convertFor(RechargeSituation rechargeSituation) {
		if (rechargeSituation == null) {
			return null;
		}
		RechargeSituationVO vo = new RechargeSituationVO();
		BeanUtils.copyProperties(rechargeSituation, vo);
		return vo;
	}

}
