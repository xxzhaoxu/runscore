package me.zohar.runscore.rechargewithdraw.domain;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "v_recharge_withdraw_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class RechargeWithdrawLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 订单类型:充值,提现
	 */
	private String orderType; 
	
	/**
	 * 金额
	 */
	private Double amount;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 提交时间
	 */
	private Date submitTime;

	/**
	 * 订单状态
	 */
	private String orderState;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

}
