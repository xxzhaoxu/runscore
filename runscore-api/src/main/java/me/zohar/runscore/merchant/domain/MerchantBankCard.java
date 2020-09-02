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
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant_bank_card")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MerchantBankCard implements Serializable {

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
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;

	private Date createTime;

	/**
	 * 银行资料最近修改时间
	 */
	private Date bankInfoLatelyModifyTime;

	/**
	 * 逻辑删除
	 */
	private Boolean deletedFlag;

	private Date deletedTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	@Column(name = "merchant_id", length = 32)
	private String merchantId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Merchant merchant;

	public void delete() {
		this.setDeletedFlag(true);
		this.setDeletedTime(new Date());

	}

}
