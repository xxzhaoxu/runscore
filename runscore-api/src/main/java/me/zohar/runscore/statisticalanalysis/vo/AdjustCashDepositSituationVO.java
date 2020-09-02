package me.zohar.runscore.statisticalanalysis.vo;

import java.io.Serializable;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.AdjustCashDepositSituation;

import org.springframework.beans.BeanUtils;

@Data
public class AdjustCashDepositSituationVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Double totalAddAmount;

	private Double totalMinusAmount;

	private Double monthAddAmount;

	private Double monthMinusAmount;

	private Double yesterdayAddAmount;

	private Double yesterdayMinusAmount;

	private Double todayAddAmount;

	private Double todayMinusAmount;

	public static AdjustCashDepositSituationVO convertFor(AdjustCashDepositSituation adjustCashDepositSituation) {
		if (adjustCashDepositSituation == null) {
			return null;
		}
		AdjustCashDepositSituationVO vo = new AdjustCashDepositSituationVO();
		BeanUtils.copyProperties(adjustCashDepositSituation, vo);
		return vo;
	}

}
