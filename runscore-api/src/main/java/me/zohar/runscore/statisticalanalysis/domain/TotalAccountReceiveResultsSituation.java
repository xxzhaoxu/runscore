package me.zohar.runscore.statisticalanalysis.domain;

import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name = "v_total_account_receive_results_situation")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TotalAccountReceiveResultsSituation implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	@Column(name = "inviter_id", length = 32)
	private String inviterId;

	private String userName;

	private String accountType;

	private Boolean deletedFlag;

	private Double totalTradeAmount;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double yesterdayTradeAmount;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double todayTradeAmount;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

}
