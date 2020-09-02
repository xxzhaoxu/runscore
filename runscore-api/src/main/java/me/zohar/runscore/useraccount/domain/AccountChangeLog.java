package me.zohar.runscore.useraccount.domain;

import java.io.Serializable;
import java.text.MessageFormat;
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

import cn.hutool.core.util.NumberUtil;
import lombok.Getter;
import lombok.Setter;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.domain.MerchantOrder;
import me.zohar.runscore.merchant.domain.OrderRebate;
import me.zohar.runscore.rechargewithdraw.domain.RechargeOrder;
import me.zohar.runscore.rechargewithdraw.domain.WithdrawRecord;

@Getter
@Setter
@Entity
@Table(name = "account_change_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AccountChangeLog implements Serializable {

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
	 * 账变时间
	 */
	private Date accountChangeTime;

	/**
	 * 账变类型代码
	 */
	private String accountChangeTypeCode;

	/**
	 * 账变金额
	 */
	private Double accountChangeAmount;

	/**
	 * 账户余额
	 */
	private Double cashDeposit;

	/**
	 * 备注
	 */
	private String note;

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

	public static AccountChangeLog buildWithTopScore(UserAccount userAccount, Double topScoreAmount) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(log.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_上分);
		log.setAccountChangeAmount(topScoreAmount);
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	public static AccountChangeLog buildWithTopScoreDeduction(UserAccount userAccount, Double topScoreAmount,
			String topScoreAccountUserName) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(log.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_上分扣款);
		log.setAccountChangeAmount(-topScoreAmount);
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		log.setNote(MessageFormat.format("上分账号:{0}", topScoreAccountUserName));
		return log;
	}

	public static AccountChangeLog buildWithReleaseFreezeAmount(UserAccount userAccount, MerchantOrder merchantOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(merchantOrder.getOrderNo());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_退还冻结金额);
		log.setAccountChangeAmount(NumberUtil.round(merchantOrder.getGatheringAmount(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	public static AccountChangeLog buildWithCustomerCancelOrderRefund(UserAccount userAccount,
			MerchantOrder platformOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(platformOrder.getOrderNo());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_客服取消订单并退款);
		log.setAccountChangeAmount(NumberUtil.round(platformOrder.getGatheringAmount(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	public static AccountChangeLog buildWithCustomerCancelOrderDeductRebate(UserAccount userAccount,
			OrderRebate orderRebate) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(orderRebate.getMerchantOrder().getOrderNo());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_客服取消订单并扣除返点);
		log.setAccountChangeAmount(-NumberUtil.round(orderRebate.getRebateAmount(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	public static AccountChangeLog buildWithCustomerCancelOrderDeductBounty(UserAccount userAccount,
			MerchantOrder merchantOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(merchantOrder.getOrderNo());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_客服取消订单并扣除奖励金);
		log.setAccountChangeAmount(-NumberUtil.round(merchantOrder.getBounty(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	public static AccountChangeLog buildWithAlterToActualPayAmountRefund(UserAccount userAccount, String orderNo,
			Double refundAmount) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(orderNo);
		log.setAccountChangeTime(new Date());
		if (refundAmount >= 0) {
			log.setAccountChangeTypeCode(Constant.账变日志类型_改单为实际支付金额并退款);
		} else {
			log.setAccountChangeTypeCode(Constant.账变日志类型_改单为实际支付金额并扣款);
		}
		log.setAccountChangeAmount(NumberUtil.round(refundAmount, 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建充值账变日志
	 *
	 * @param userAccount
	 * @param bettingOrder
	 * @return
	 */
	public static AccountChangeLog buildWithRecharge(UserAccount userAccount, RechargeOrder rechargeOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(rechargeOrder.getOrderNo());
		log.setAccountChangeTime(rechargeOrder.getSettlementTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_账号充值);
		log.setAccountChangeAmount(NumberUtil.round(rechargeOrder.getActualPayAmount(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建接单奖励金账变日志
	 *
	 * @param userAccount
	 * @param bounty
	 * @param rebate
	 * @return
	 */
	public static AccountChangeLog buildWithReceiveOrderBounty(UserAccount userAccount, Double bounty, Double rebate,String orderNo) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(orderNo);
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_接单奖励金);
		log.setAccountChangeAmount(NumberUtil.round(bounty, 4).doubleValue());
		log.setNote(MessageFormat.format("接单返点:{0}%", rebate));
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建充值优惠账变日志
	 *
	 * @param userAccount
	 * @param returnWater
	 * @param returnWaterRate
	 * @return
	 */
	public static AccountChangeLog buildWithRechargePreferential(UserAccount userAccount, Double returnWater,
			Double returnWaterRate) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(log.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_充值优惠);
		log.setAccountChangeAmount(NumberUtil.round(returnWater, 4).doubleValue());
		log.setNote(MessageFormat.format("充值返水率:{0}%", returnWaterRate));
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建接单扣款日志
	 *
	 * @param userAccount
	 * @param platformOrder
	 * @return
	 */
	public static AccountChangeLog buildWithReceiveOrderDeduction(UserAccount userAccount,
			MerchantOrder merchantOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(merchantOrder.getOrderNo());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_接单扣款);
		log.setAccountChangeAmount(-merchantOrder.getGatheringAmount());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建发起提现账变日志
	 *
	 * @param userAccount
	 * @param bettingOrder
	 * @return
	 */
	public static AccountChangeLog buildWithStartWithdraw(UserAccount userAccount, WithdrawRecord withdrawRecord) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(withdrawRecord.getOrderNo());
		log.setAccountChangeTime(withdrawRecord.getSubmitTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_账号提现);
		log.setAccountChangeAmount(-withdrawRecord.getWithdrawAmount());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建提现不符退款账变日志
	 *
	 * @param userAccount
	 * @param bettingOrder
	 * @return
	 */
	public static AccountChangeLog buildWithWithdrawNotApprovedRefund(UserAccount userAccount,
			WithdrawRecord withdrawRecord) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(withdrawRecord.getOrderNo());
		log.setAccountChangeTime(withdrawRecord.getApprovalTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_提现不符退款);
		log.setAccountChangeAmount(withdrawRecord.getWithdrawAmount());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建退还账户余额账变日志
	 *
	 * @param userAccount
	 * @param withdrawRecord
	 * @return
	 */
	public static AccountChangeLog buildWithRefundCashDeposit(UserAccount userAccount, MerchantOrder merchantOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(merchantOrder.getOrderNo());
		log.setAccountChangeTime(merchantOrder.getDealTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_退还账户余额);
		log.setAccountChangeAmount(NumberUtil.round(merchantOrder.getGatheringAmount(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建手工调整账户余额日志
	 *
	 * @param userAccount
	 * @param platformOrder
	 * @return
	 */
	public static AccountChangeLog buildWithHandworkAdjustCashDeposit(UserAccount userAccount,
			Double accountChangeAmount) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(accountChangeAmount > 0 ? Constant.账变日志类型_手工增账户余额 : Constant.账变日志类型_手工减账户余额);
		log.setAccountChangeAmount(accountChangeAmount);
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建订单返点账变日志
	 *
	 * @param userAccount
	 * @param orderRebate
	 * @return
	 */
	public static AccountChangeLog buildWithOrderRebate(UserAccount userAccount, OrderRebate orderRebate) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(orderRebate.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_奖励金返点);
		log.setAccountChangeAmount(NumberUtil.round(orderRebate.getRebateAmount(), 4).doubleValue());
		log.setCashDeposit(userAccount.getCashDeposit());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

}
