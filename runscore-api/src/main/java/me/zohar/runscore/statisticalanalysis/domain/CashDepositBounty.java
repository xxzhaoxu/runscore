package me.zohar.runscore.statisticalanalysis.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_cash_deposit_bounty")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CashDepositBounty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Double totalCashDeposit;

	private Double totalBounty;

	private Double monthBounty;

	private Double yesterdayBounty;

	private Double todayBounty;

}
