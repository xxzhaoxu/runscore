package me.zohar.runscore.agent.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import me.zohar.runscore.useraccount.domain.UserAccount;

@Getter
@Setter
@Entity
@Table(name = "invite_code")
@DynamicInsert(true)
@DynamicUpdate(true)
public class InviteCode implements Serializable {

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
	 * 邀请码
	 */
	private String code;

	/**
	 * 账号类型
	 */
	private String accountType;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 有效期
	 */
	private Date periodOfValidity;
	
	private Boolean used;

	/**
	 * 邀请人id
	 */
	@Column(name = "inviter_id", length = 32)
	private String inviterId;

	/**
	 * 邀请人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inviter_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount inviter;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "invite_code_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Set<InviteCodeChannelRebate> inviteCodeChannelRebates;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;
	
}
