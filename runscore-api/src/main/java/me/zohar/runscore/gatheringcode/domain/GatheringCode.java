package me.zohar.runscore.gatheringcode.domain;

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
import me.zohar.runscore.merchant.domain.GatheringChannel;
import me.zohar.runscore.useraccount.domain.UserAccount;

@Getter
@Setter
@Entity
@Table(name = "gathering_code")
@DynamicInsert(true)
@DynamicUpdate(true)
public class GatheringCode implements Serializable {

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
	 * 状态,启用:1;禁用:0
	 */
	private String state;

	private Boolean fixedGatheringAmount;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

	/**
	 * 收款人
	 */
	private String payee;

	/**
	 * 支付宝userid
	 */
	private String alipayUserid;

	/**
	 * 银行
	 */
	private String openAccountBank;

	/**
	 * 开户人
	 */
	private String accountHolder;

	/**
	 * 卡号
	 */
	private String bankCardAccount;

	/**
	 * 银行编号大写
	 */
	private String bankShortName;

	/**
	 * 使用中
	 */
	private Boolean inUse;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 发起审核时间
	 */
	private Date initiateAuditTime;

	/**
	 * 审核类型
	 */
	private String auditType;

	/**
	 * 二维码内容
	 */
	private String codeContent;

	private String mobile;

	private String realName;

	private String account;

	@Column(name = "storage_id", length = 32)
	private String storageId;

	@Column(name = "gathering_channel_id", length = 32)
	private String gatheringChannelId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gathering_channel_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private GatheringChannel gatheringChannel;

	/**
	 * 用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount userAccount;

	public void initiateAudit(String auditType) {
		this.setState(Constant.收款码状态_待审核);
		this.setInitiateAuditTime(new Date());
		this.setAuditType(auditType);
	}

}
