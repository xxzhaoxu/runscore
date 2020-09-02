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
import me.zohar.runscore.agent.domain.InviteCodeChannelRebate;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.domain.GatheringChannel;

@Getter
@Setter
@Entity
@Table(name = "account_receive_order_channel")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AccountReceiveOrderChannel implements Serializable {

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

	private Double rebate;

	private String state;

	private Date createTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

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

	@Column(name = "channel_id", length = 32)
	private String channelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private GatheringChannel channel;

	public static AccountReceiveOrderChannel build(InviteCodeChannelRebate inviteCodeChannelRebate,
			String userAccountId) {
		AccountReceiveOrderChannel po = new AccountReceiveOrderChannel();
		po.setId(IdUtils.getId());
		po.setRebate(inviteCodeChannelRebate.getRebate());
		po.setState(Constant.账号接单通道状态_开启中);
		po.setChannelId(inviteCodeChannelRebate.getChannelId());
		po.setUserAccountId(userAccountId);
		po.setCreateTime(new Date());
		return po;
	}

	public static AccountReceiveOrderChannel build(GatheringChannel gatheringChannel, String userAccountId) {
		AccountReceiveOrderChannel po = new AccountReceiveOrderChannel();
		po.setId(IdUtils.getId());
		po.setRebate(gatheringChannel.getDefaultReceiveOrderRabate());
		po.setState(Constant.账号接单通道状态_开启中);
		po.setChannelId(gatheringChannel.getId());
		po.setUserAccountId(userAccountId);
		po.setCreateTime(new Date());
		return po;
	}

}
