package me.zohar.runscore.merchant.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;
import me.zohar.runscore.constants.Constant;

@Getter
@Setter
@Entity
@Table(name = "merchant_settlement_record")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MerchantSettlementRecord implements Serializable {

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
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 提现金额
	 */
	private Double withdrawAmount;

	/**
	 * 申请时间
	 */
	private Date applyTime;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 审核时间
	 */
	private Date approvalTime;

	/**
	 * 确认到帐时间
	 */
	private Date confirmCreditedTime;

	@Column(name = "merchant_id", length = 32)
	private String merchantId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Merchant merchant;

	@Column(name = "merchant_bank_card_id", length = 32)
	private String merchantBankCardId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_bank_card_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MerchantBankCard merchantBankCard;
	
	public void notApproved(String note) {
		this.setState(Constant.商户结算状态_审核不通过);
		this.setApprovalTime(new Date());
		this.setNote(note);
	}
	
	public void approved(String note) {
		this.setState(Constant.商户结算状态_审核通过);
		this.setApprovalTime(new Date());
		this.setNote(note);
	}
	
	public void confirmCredited() {
		this.setState(Constant.商户结算状态_已到账);
		this.setConfirmCreditedTime(new Date());
	}

}
