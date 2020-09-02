package me.zohar.runscore.statisticalanalysis.vo;

import java.io.Serializable;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.WithdrawSituation;

import org.springframework.beans.BeanUtils;

@Data
public class WithdrawSituationVO implements Serializable {

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

	public static WithdrawSituationVO convertFor(WithdrawSituation withdrawSituation) {
		if (withdrawSituation == null) {
			return null;
		}
		WithdrawSituationVO vo = new WithdrawSituationVO();
		BeanUtils.copyProperties(withdrawSituation, vo);
		return vo;
	}

}
