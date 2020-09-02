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
public class CopyOfTotalAccountReceiveResultsSituation implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "received_account_id", length = 32)
	private String receivedAccountId;

	@Column(name = "inviter_id", length = 32)
	private String inviterId;

	private String userName;

	private String accountType;

	private Double gatheringAmount;

	private Long orderNum;

	private Double paidAmount;

	private Double paidAmount2;

	private Double bounty;

	private Long paidOrderNum;

	private Double rebateAmount;

	private Double successRate;

	private boolean deletedFlag;

}
