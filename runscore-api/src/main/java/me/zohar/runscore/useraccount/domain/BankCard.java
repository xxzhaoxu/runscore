package me.zohar.runscore.useraccount.domain;

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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bank_card")
@DynamicInsert(true)
@DynamicUpdate(true)
public class BankCard implements Serializable {

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
	 * 开户人
	 */
	private String accountHolder;

	/**
	 * 银行卡号
	 */
	private String bankCardAccount;

	private Date createTime;

	/**
	 * 最近修改时间
	 */
	private Date latelyModifyTime;

	/**
	 * 逻辑删除
	 */
	private Boolean deletedFlag;

	private Date deletedTime;

	/**
	 * 用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

	/**
	 * 用户账号
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount userAccount;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	public void deleted() {
		this.setDeletedFlag(true);
		this.setDeletedTime(new Date());
	}

}
