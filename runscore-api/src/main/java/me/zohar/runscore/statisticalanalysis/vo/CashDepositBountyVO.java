package me.zohar.runscore.statisticalanalysis.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.CashDepositBounty;

@Data
public class CashDepositBountyVO {

	private Double totalCashDeposit;

	private Double totalBounty;

	private Double monthBounty;

	private Double yesterdayBounty;

	private Double todayBounty;

	public static CashDepositBountyVO convertFor(CashDepositBounty cashDepositBounty) {
		if (cashDepositBounty == null) {
			return null;
		}
		CashDepositBountyVO vo = new CashDepositBountyVO();
		BeanUtils.copyProperties(cashDepositBounty, vo);
		return vo;
	}

}
