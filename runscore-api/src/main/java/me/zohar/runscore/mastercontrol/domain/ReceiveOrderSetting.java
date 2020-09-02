package me.zohar.runscore.mastercontrol.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.runscore.common.utils.IdUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receive_order_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ReceiveOrderSetting implements Serializable {

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

	private Boolean stopStartAndReceiveOrder;
	//同一收款方式禁止接相同金额的订单  1是 true  0否 false
	private Boolean banReceiveRepeatOrder;

	//同一码禁止接相同金额的订单  1是 true  0否 false
	private Boolean banReceiveCodeOrder;

	/**
	 * 接单有效时长
	 */
	private Integer receiveOrderEffectiveDuration;

	/**
	 * 支付有效时长
	 */
	private Integer orderPayEffectiveDuration;

	private Integer receiveOrderInterval;

	private Integer receiveOrderAppealInterval;

	/**
	 * 接单数量上限
	 */
	private Integer receiveOrderUpperLimit;

	private Boolean showAllOrder;

	/**
	 * 保证金最低要求
	 */
	private Double cashDepositMinimumRequire;

	private Boolean unfixedGatheringCodeReceiveOrder;

	//谷歌验证器
	private Boolean gatheringCodeGoogleAuth;

	private Double cashPledge;

	private Boolean receiveOrderSound;

	private Boolean auditGatheringCode;

	private Boolean openAutoReceiveOrder;

	private Boolean freezeModelEnabled;

	private Integer freezeEffectiveDuration;

	private Integer unconfirmedAutoFreezeDuration;

	private Integer noOpsStopReceiveOrder;

	private Boolean dispatchMode;

	private Boolean sameCityPriority;

	private Boolean gatheringCodeEverydayUsedUpperLimit;

	private Long gatheringCodeUsedUpperLimit;

	/**
	 * 最近修改时间
	 */
	private Date latelyUpdateTime;

	public static ReceiveOrderSetting build() {
		ReceiveOrderSetting setting = new ReceiveOrderSetting();
		setting.setId(IdUtils.getId());
		return setting;
	}

}
