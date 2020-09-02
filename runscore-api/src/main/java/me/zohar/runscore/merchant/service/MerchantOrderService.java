package me.zohar.runscore.merchant.service;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.ip.IpInfoVO;
import me.zohar.runscore.common.ip.IpUtils;
import me.zohar.runscore.common.utils.ListUtils;
import me.zohar.runscore.common.utils.ThreadPoolUtils;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.gatheringcode.domain.GatheringCode;
import me.zohar.runscore.gatheringcode.domain.GatheringCodeUsage;
import me.zohar.runscore.gatheringcode.repo.GatheringCodeRepo;
import me.zohar.runscore.gatheringcode.repo.GatheringCodeUsageRepo;
import me.zohar.runscore.gatheringcode.vo.GatheringCodeUsageVO;
import me.zohar.runscore.mastercontrol.domain.ReceiveOrderSetting;
import me.zohar.runscore.mastercontrol.domain.RegisterSetting;
import me.zohar.runscore.mastercontrol.repo.ReceiveOrderSettingRepo;
import me.zohar.runscore.mastercontrol.repo.RegisterSettingRepo;
import me.zohar.runscore.merchant.domain.ActualIncomeRecord;
import me.zohar.runscore.merchant.domain.FreezeRecord;
import me.zohar.runscore.merchant.domain.GatheringChannel;
import me.zohar.runscore.merchant.domain.GatheringChannelRate;
import me.zohar.runscore.merchant.domain.Merchant;
import me.zohar.runscore.merchant.domain.MerchantOrder;
import me.zohar.runscore.merchant.domain.MerchantOrderPayInfo;
import me.zohar.runscore.merchant.domain.OrderRebate;
import me.zohar.runscore.merchant.domain.QueueRecord;
import me.zohar.runscore.merchant.param.AppConfirmToPaidParam;
import me.zohar.runscore.merchant.param.LowerLevelAccountReceiveOrderQueryCondParam;
import me.zohar.runscore.merchant.param.ManualStartOrderParam;
import me.zohar.runscore.merchant.param.MerchantOrderQueryCondParam;
import me.zohar.runscore.merchant.param.MyReceiveOrderRecordQueryCondParam;
import me.zohar.runscore.merchant.param.StartOrderParam;
import me.zohar.runscore.merchant.repo.ActualIncomeRecordRepo;
import me.zohar.runscore.merchant.repo.FreezeRecordRepo;
import me.zohar.runscore.merchant.repo.GatheringChannelRateRepo;
import me.zohar.runscore.merchant.repo.GatheringChannelRepo;
import me.zohar.runscore.merchant.repo.MerchantOrderPayInfoRepo;
import me.zohar.runscore.merchant.repo.MerchantOrderRepo;
import me.zohar.runscore.merchant.repo.MerchantRepo;
import me.zohar.runscore.merchant.repo.OrderRebateRepo;
import me.zohar.runscore.merchant.repo.QueueRecordRepo;
import me.zohar.runscore.merchant.vo.DispatchOrderTipVO;
import me.zohar.runscore.merchant.vo.MerchantOrderDetailsVO;
import me.zohar.runscore.merchant.vo.MerchantOrderVO;
import me.zohar.runscore.merchant.vo.MyWaitConfirmOrderVO;
import me.zohar.runscore.merchant.vo.MyWaitReceivingOrderVO;
import me.zohar.runscore.merchant.vo.OrderGatheringCodeVO;
import me.zohar.runscore.merchant.vo.PayUrlPicVO;
import me.zohar.runscore.merchant.vo.QueueRecordVO;
import me.zohar.runscore.merchant.vo.ReceiveOrderRecordVO;
import me.zohar.runscore.merchant.vo.StartOrderSuccessVO;
import me.zohar.runscore.useraccount.domain.AccountChangeLog;
import me.zohar.runscore.useraccount.domain.AccountReceiveOrderChannel;
import me.zohar.runscore.useraccount.domain.UserAccount;
import me.zohar.runscore.useraccount.repo.AccountChangeLogRepo;
import me.zohar.runscore.useraccount.repo.AccountReceiveOrderChannelRepo;
import me.zohar.runscore.useraccount.repo.UserAccountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpUtil;

import com.zengtengpeng.annotation.Lock;

@Validated
@Slf4j
@Service
public class MerchantOrderService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private MerchantOrderRepo merchantOrderRepo;

	@Autowired
	private MerchantOrderPayInfoRepo merchantOrderPayInfoRepo;

	@Autowired
	private MerchantRepo merchantRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private GatheringCodeRepo gatheringCodeRepo;

	@Autowired
	private GatheringCodeUsageRepo gatheringCodeUsageRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@Autowired
	private ReceiveOrderSettingRepo receiveOrderSettingRepo;

	@Autowired
	private OrderRebateRepo orderRebateRepo;

	@Autowired
	private AccountReceiveOrderChannelRepo accountReceiveOrderChannelRepo;

	@Autowired
	private GatheringChannelRateRepo gatheringChannelRateRepo;

	@Autowired
	private GatheringChannelRepo gatheringChannelRepo;

	@Autowired
	private ActualIncomeRecordRepo actualIncomeRecordRepo;

	@Autowired
	private FreezeRecordRepo freezeRecordRepo;

	@Autowired
	private QueueRecordRepo queueRecordRepo;

	@Autowired
	private RegisterSettingRepo registerSettingRepo;

	@Lock(keys = "'supplementOrder_' + #id")
	@Transactional
	public void supplementOrder(@NotBlank String id,
			@NotNull @DecimalMin(value = "0", inclusive = true) Double gatheringAmount,
			@NotBlank String dealAccountId) {
		MerchantOrder invalidOrder = merchantOrderRepo.getOne(id);
		MerchantOrderPayInfo invalidOrderPayInfo = invalidOrder.getPayInfo();
		if (!(Constant.商户订单状态_未确认超时取消.equals(invalidOrder.getOrderState()))) {
			throw new BizException(BizError.只有未确认超时取消的订单才能补单);
		}

		MerchantOrder merchantOrder = invalidOrder.supplementOrder();
		merchantOrder.setGatheringAmount(gatheringAmount);
		merchantOrder.confirmToPaid("补单");
		MerchantOrderPayInfo payInfo = invalidOrderPayInfo.supplementOrder();
		payInfo.setMerchantOrderId(merchantOrder.getId());
		payInfo.setSubmitTime(merchantOrder.getSubmitTime());
		merchantOrder.setPayInfoId(payInfo.getId());
		merchantOrderRepo.save(merchantOrder);
		merchantOrderPayInfoRepo.save(payInfo);
		receiveOrderSettlement(merchantOrder);

		UserAccount userAccount = invalidOrder.getReceivedAccount();
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.额度不足);
		}
		if (cashDeposit < 0) {
			throw new BizException(BizError.额度不足);
		}
		cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public Integer getQueueRanking(String userAccountId) {
		List<QueueRecord> queueRecords = queueRecordRepo.findByUsedIsFalseOrderByQueueTime();
		for (int i = 0; i < queueRecords.size(); i++) {
			QueueRecord queueRecord = queueRecords.get(i);
			if (queueRecord.getUserAccountId().equals(userAccountId)) {
				return i + 1;
			}
		}
		return 1;
	}

	@Transactional
	public void cancelOrderWithMerchant(@NotBlank String id, @NotBlank String merchantId) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(id);
		if (!merchantOrder.getMerchantId().equals(merchantId)) {
			throw new BizException(BizError.无权取消订单);
		}
		cancelOrder(id);
	}

	@ParamValid
	@Transactional
	public void appConfirmToPaid(AppConfirmToPaidParam param) {
		UserAccount userAccount = userAccountRepo.findBySecretKeyAndDeletedFlagIsFalse(param.getSecretKey());
		if (userAccount == null) {
			throw new BizException(BizError.账号密钥无效);
		}
		if (!NumberUtil.isNumber(param.getAmount())) {
			throw new BizException(BizError.金额格式不正确);
		}
		if (Double.parseDouble(param.getAmount()) <= 0) {
			throw new BizException(BizError.金额不能小于或等于0);
		}
		boolean hitFlag = false;
		List<MerchantOrder> merchantOrders = merchantOrderRepo
				.findByOrderStateAndReceivedAccountIdAndGatheringCodePayee(Constant.商户订单状态_已接单, userAccount.getId(),
						param.getPayee());
		for (MerchantOrder merchantOrder : merchantOrders) {
			Double gatheringAmount = merchantOrder.getGatheringAmount();
			if (gatheringAmount.compareTo(Double.parseDouble(param.getAmount())) != 0) {
				continue;
			}
			if ((Constant.app监控通道_微信.equals(param.getType())
					&& merchantOrder.getGatheringChannel().getChannelName().contains("微信"))
					|| (Constant.app监控通道_支付宝.equals(param.getType())
							&& merchantOrder.getGatheringChannel().getChannelName().contains("支付宝"))|| (Constant.app监控通道_银行卡.equals(param.getType()))) {
				userConfirmToPaid(userAccount.getId(), merchantOrder.getId());
				hitFlag = true;
				break;
			}
		}
		if (!hitFlag) {
			throw new BizException(BizError.app监控回调失败找不到对应通道金额的订单);
		}
	}

	@Transactional
	public void dispatchOrderTipMarkRead(@NotBlank String userAccountId, @NotBlank String id) {
		QueueRecord queueRecord = queueRecordRepo.findByUserAccountIdAndId(userAccountId, id);
		queueRecord.setMarkRead(true);
		queueRecordRepo.save(queueRecord);
	}

	@Transactional(readOnly = true)
	public DispatchOrderTipVO dispatchOrderTip(@NotBlank String userAccountId) {
		QueueRecord queueRecord = queueRecordRepo.findTopByUserAccountIdAndUsedIsTrueAndMarkReadIsFalseOrderByQueueTimeDesc(userAccountId);
		if (queueRecord == null) {
			return null;
		}
		return DispatchOrderTipVO.build(queueRecord.getId(), queueRecord.getNote());
	}

	@Transactional
	public void confirmToPaidWithUnconfirmedAutoFreeze(@NotBlank String orderId) {
		MerchantOrder merchantOrder = merchantOrderRepo.findById(orderId).orElse(null);
		if (merchantOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!Constant.商户订单状态_未确认超时取消.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.订单状态为未确认超时取消才能转为确认已支付);
		}
		FreezeRecord freezeRecord = freezeRecordRepo.findTopByMerchantOrderId(merchantOrder.getId());
		if (freezeRecord.getDealTime() == null) {
			freezeRecord.setDealTime(new Date());
			freezeRecordRepo.save(freezeRecord);
		}
		merchantOrder.confirmToPaid(null);
		merchantOrderRepo.save(merchantOrder);
		receiveOrderSettlement(merchantOrder);
		receiveOrderCashDeposit(merchantOrder);
	}

	/**
	 * 手动取消冻结余额
	 *
	 * @param orderId
	 */
	@Transactional
	public void confirmToOrderFreezeRecord(@NotBlank String orderId) {
		MerchantOrder merchantOrder = merchantOrderRepo.findById(orderId).orElse(null);
		if (merchantOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!Constant.商户订单状态_未确认超时取消.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.订单状态为未确认超时取消才能手动取消冻结余额);
		}
		FreezeRecord freezeRecord = freezeRecordRepo.findTopByMerchantOrderId(merchantOrder.getId());
		if (freezeRecord.getDealTime() == null) {
			releaseFreezeOrder(freezeRecord.getId());
		}else{
			throw new BizException(BizError.系统已自动取消冻结余额);
		}
	}

	@Transactional(readOnly = true)
	public void releaseFreezeOrder() {
		Date now = new Date();
		List<FreezeRecord> freezeRecords = freezeRecordRepo.findByDealTimeIsNullAndUsefulTimeLessThan(now);
		for (FreezeRecord freezeRecord : freezeRecords) {
			redisTemplate.opsForList().leftPush(Constant.冻结记录ID, freezeRecord.getId());
		}
	}

	@Lock(keys = "'releaseFreezeOrder_' + #freezeRecordId")
	@Transactional
	public void releaseFreezeOrder(@NotBlank String freezeRecordId) {
		FreezeRecord freezeRecord = freezeRecordRepo.getOne(freezeRecordId);
		if (freezeRecord.getDealTime() != null) {
			return;
		}
		freezeRecord.setDealTime(new Date());
		freezeRecordRepo.save(freezeRecord);
		MerchantOrder merchantOrder = freezeRecord.getMerchantOrder();
		if (merchantOrder == null || !Constant.商户订单状态_未确认超时取消.equals(merchantOrder.getOrderState())) {
			return;
		}
		UserAccount userAccount = merchantOrder.getReceivedAccount();
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() + merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		merchantOrderRepo.save(merchantOrder);
		accountChangeLogRepo.save(AccountChangeLog.buildWithReleaseFreezeAmount(userAccount, merchantOrder));
	}

	@Transactional
	public void freezeOrder() {
		ReceiveOrderSetting receiveOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (!receiveOrderSetting.getFreezeModelEnabled()) {
			return;
		}
		DateTime time = DateUtil.offset(new Date(), DateField.MINUTE,
				-receiveOrderSetting.getUnconfirmedAutoFreezeDuration());
		List<MerchantOrder> merchantOrders = merchantOrderRepo
				.findByOrderStateAndReceivedTimeLessThan(Constant.商户订单状态_已接单, time);
		for (MerchantOrder merchantOrder : merchantOrders) {
			merchantOrder.setOrderState(Constant.商户订单状态_未确认超时取消);
			merchantOrderRepo.save(merchantOrder);

			FreezeRecord freezeRecord = FreezeRecord.build(merchantOrder.getReceivedAccountId(), merchantOrder.getId(),
					receiveOrderSetting.getFreezeEffectiveDuration());
			freezeRecordRepo.save(freezeRecord);
		}
	}

	@Transactional
	public void timingPublishOrder() {
		List<MerchantOrder> orders = merchantOrderRepo.findByOrderStateAndSubmitTimeLessThan(Constant.商户订单状态_定时发单,
				new Date());
		for (MerchantOrder order : orders) {
			order.setOrderState(Constant.商户订单状态_等待接单);
			merchantOrderRepo.save(order);
		}

	}

	@Transactional(readOnly = true)
	public MerchantOrderDetailsVO findMerchantOrderDetailsById(@NotBlank String orderId) {
		MerchantOrderDetailsVO vo = MerchantOrderDetailsVO.convertFor(merchantOrderRepo.getOne(orderId));
		return vo;
	}

	/**
	 * 取消订单退款
	 *
	 * @param orderId
	 */
	@Transactional
	public void cancelOrderRefund(@NotBlank String orderId) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(orderId);
		if (Constant.商户订单状态_取消订单退款.equals(merchantOrder.getOrderState())) {
			return;
		}
		if (!(Constant.商户订单状态_申诉中.equals(merchantOrder.getOrderState())
				|| Constant.商户订单状态_已接单.equals(merchantOrder.getOrderState()))) {
			throw new BizException(BizError.只有申诉中或已接单的商户订单才能进行取消订单退款操作);
		}
		UserAccount userAccount = merchantOrder.getReceivedAccount();
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() + merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		merchantOrder.customerCancelOrderRefund();
		merchantOrderRepo.save(merchantOrder);
		accountChangeLogRepo.save(AccountChangeLog.buildWithCustomerCancelOrderRefund(userAccount, merchantOrder));
	}

	@Transactional(readOnly = true)
	public OrderGatheringCodeVO getOrderGatheringCode(@NotBlank String orderNo) {
		MerchantOrder order = merchantOrderRepo.findByOrderNo(orderNo);
		if (order == null) {
			log.error("商户订单不存在;orderNo:{}", orderNo);
			throw new BizException(BizError.商户订单不存在);
		}
		OrderGatheringCodeVO vo = OrderGatheringCodeVO.convertFor(order);
		return vo;
	}

	@Transactional(readOnly = true)
	public PayUrlPicVO payUrlPic(@NotBlank String orderNo) {
		MerchantOrder order = merchantOrderRepo.findByOrderNo(orderNo);
		if (order == null) {
			log.error("商户订单不存在;orderNo:{}", orderNo);
			throw new BizException(BizError.商户订单不存在);
		}
		PayUrlPicVO vo = PayUrlPicVO.convertFor(order);
		return vo;
	}

	@Transactional(readOnly = true)
	public OrderGatheringCodeVO getOrderMerchantCode(@NotBlank String orderNo,@NotBlank String merchantNum,
			@NotBlank String sign) {
		MerchantOrder order = merchantOrderRepo.findByOrderNo(orderNo);
		if (order == null) {
			log.error("商户订单不存在;orderNo:{}", orderNo);
			throw new BizException(BizError.商户订单不存在);
		}
		Merchant merchant = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(merchantNum);
		if (merchant == null) {
			throw new BizException(BizError.商户未接入);
		}
		String signStr = merchantNum + orderNo + merchant.getSecretKey();
		signStr = new Digester(DigestAlgorithm.MD5).digestHex(signStr);
		if (!signStr.equals(sign)) {
			throw new BizException(BizError.签名不正确);
		}
		OrderGatheringCodeVO vo = OrderGatheringCodeVO.convertFor(order);
		return vo;
	}

	@Transactional(readOnly = true)
	public GatheringCodeUsageVO getGatheringCode(List<String> gatheringCodeIds,String receivedAccountId, String gatheringChannelCode,
			Double gatheringAmount) {
		List<GatheringCodeUsage> gatheringCodes = null;
		ReceiveOrderSetting setting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting.getUnfixedGatheringCodeReceiveOrder()) {
			gatheringCodes = gatheringCodeUsageRepo
					.findByUserAccountIdAndGatheringChannelChannelCodeAndStateAndFixedGatheringAmountFalseAndInUseTrue(
							receivedAccountId, gatheringChannelCode, Constant.收款码状态_正常);
		} else {
			gatheringCodes = gatheringCodeUsageRepo
					.findByUserAccountIdAndGatheringChannelChannelCodeAndGatheringAmountAndStateAndInUseTrue(
							receivedAccountId, gatheringChannelCode, gatheringAmount, Constant.收款码状态_正常);
		}
		if (CollectionUtil.isEmpty(gatheringCodes)) {
			return null;
		}
		List<GatheringCodeUsage> tmpGatheringCodes = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(gatheringCodes)) {
			for (GatheringCodeUsage gatheringCode : gatheringCodes) {
				if (setting.getGatheringCodeEverydayUsedUpperLimit()
						&& gatheringCode.getTodayPaidOrderNum() < setting.getGatheringCodeUsedUpperLimit()) {
					tmpGatheringCodes.add(gatheringCode);
				} else if (gatheringCode.getTotalPaidOrderNum() < setting.getGatheringCodeUsedUpperLimit()) {
					tmpGatheringCodes.add(gatheringCode);
				}
			}
			gatheringCodes = tmpGatheringCodes;
		}

		//开启禁止重复接单
		//if (setting.getBanReceiveRepeatOrder()) {
			if(gatheringCodeIds.size() > 0){
				for (String gatheringCodeId : gatheringCodeIds) {
					for (int i = 0; i < gatheringCodes.size(); i++) {
						if(gatheringCodeId.equals(gatheringCodes.get(i).getId())){
							gatheringCodes.remove(i);
						}
					}
				}
			}
		//}

		if (CollectionUtil.isEmpty(gatheringCodes)) {
/*			throw new BizException(BizError.业务异常.getCode(),
					MessageFormat.format("单个收款码最多只能收款{0}次,请上传新的收款码", setting.getGatheringCodeUsedUpperLimit()));
*/
			throw new BizException(BizError.业务异常.getCode(),"暂无空闲收款码可接单，请处理已接订单或上传新的收款码");
		}
		Collections.shuffle(gatheringCodes);
		return GatheringCodeUsageVO.convertFor(gatheringCodes.get(0));
	}

	@Transactional
	public void userConfirmToPaid(@NotBlank String userAccountId, @NotBlank String orderId) {
		MerchantOrder merchantOrder = merchantOrderRepo.findByIdAndReceivedAccountId(orderId, userAccountId);
		if (merchantOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!Constant.商户订单状态_已接单.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.订单状态为已接单才能转为确认已支付);
		}
		merchantOrder.confirmToPaid(null);
		merchantOrderRepo.save(merchantOrder);
		receiveOrderSettlement(merchantOrder);
	}

	@Transactional
	public void confirmToPaidWithCancelOrderRefund(@NotBlank String orderId) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(orderId);
		if (merchantOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!Constant.商户订单状态_取消订单退款.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.只有状态为取消订单退款的订单才能更改状态为已支付);
		}
		UserAccount userAccount = userAccountRepo.getOne(merchantOrder.getReceivedAccountId());
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.账户余额不足无法接单);
		}

		// 接单扣款
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		merchantOrder.setOrderState(Constant.商户订单状态_已接单);
		merchantOrderRepo.save(merchantOrder);
		accountChangeLogRepo.save(AccountChangeLog.buildWithReceiveOrderDeduction(userAccount, merchantOrder));

		// 确认已支付
		userConfirmToPaid(userAccount.getId(), orderId);
	}

	/**
	 * 客服确认已支付
	 *
	 * @param orderId
	 */
	@Transactional
	public void customerServiceConfirmToPaid(@NotBlank String orderId, String note) {
		MerchantOrder platformOrder = merchantOrderRepo.findById(orderId).orElse(null);
		if (platformOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!Constant.商户订单状态_申诉中.equals(platformOrder.getOrderState())) {
			throw new BizException(BizError.订单状态为申述中才能转为确认已支付);
		}
		platformOrder.confirmToPaid(note);
		merchantOrderRepo.save(platformOrder);
		receiveOrderSettlement(platformOrder);
		receiveOrderCashDeposit(platformOrder);
	}

	/**
	 * 接单结算
	 */
	@Transactional
	public void receiveOrderSettlement(MerchantOrder merchantOrder) {
		UserAccount userAccount = merchantOrder.getReceivedAccount();
		double bounty = NumberUtil.round(merchantOrder.getGatheringAmount() * merchantOrder.getRebate() * 0.01, 4)
				.doubleValue();
		merchantOrder.updateBounty(bounty);
		merchantOrderRepo.save(merchantOrder);
		userAccount.setCashDeposit(NumberUtil.round(userAccount.getCashDeposit() + bounty, 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo
				.save(AccountChangeLog.buildWithReceiveOrderBounty(userAccount, bounty, merchantOrder.getRebate(),merchantOrder.getOrderNo()));
		generateOrderRebate(merchantOrder);
		generateActualIncomeRecord(merchantOrder);
		ThreadPoolUtils.getPaidMerchantOrderPool().schedule(() -> {
			redisTemplate.opsForList().leftPush(Constant.商户订单ID, merchantOrder.getId());
		}, 1, TimeUnit.SECONDS);
	}

	/**
	 * 接单结算，扣除会员余额
	 */
	@Transactional
	public void receiveOrderCashDeposit(MerchantOrder merchantOrder) {
		UserAccount userAccount = merchantOrder.getReceivedAccount();
		userAccount.setCashDeposit(NumberUtil.round(userAccount.getCashDeposit() - merchantOrder.getGatheringAmount(), 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithReceiveOrderDeduction(userAccount, merchantOrder));
	}

	/**
	 * 生成实收金额记录
	 *
	 * @param merchantOrder
	 */
	public void generateActualIncomeRecord(MerchantOrder merchantOrder) {
		double actualIncome = merchantOrder.getGatheringAmount() * (100 - merchantOrder.getRate()) / 100;
		actualIncome = NumberUtil.round(actualIncome, 4).doubleValue();
		ActualIncomeRecord actualIncomeRecord = ActualIncomeRecord.build(actualIncome, merchantOrder.getId(),
				merchantOrder.getMerchantId());
		actualIncomeRecordRepo.save(actualIncomeRecord);
	}

	/**
	 * 生成订单返点
	 *
	 * @param bettingOrder
	 */
	public void generateOrderRebate(MerchantOrder merchantOrder) {
		UserAccount userAccount = merchantOrder.getReceivedAccount();
		UserAccount superior = merchantOrder.getReceivedAccount().getInviter();
		RegisterSetting setting = registerSettingRepo.findTopByOrderByLatelyUpdateTime();
		boolean onlyLevelRebate = false;//是否开启几级返佣
		int onlyLevelNum = 0;
		if(setting != null){
			onlyLevelRebate = setting.getOnlyLevelRebate();
			onlyLevelNum = setting.getOnlyLevelNum();
		}
		int i = 1 ;
		while (superior != null) {
			// 管理员账号没有返点
			if (Constant.账号类型_管理员.equals(superior.getAccountType())) {
				break;
			}
			AccountReceiveOrderChannel userAccountRebate = accountReceiveOrderChannelRepo
					.findByUserAccountIdAndChannelId(userAccount.getId(), merchantOrder.getGatheringChannelId());
			AccountReceiveOrderChannel superiorRebate = accountReceiveOrderChannelRepo
					.findByUserAccountIdAndChannelId(superior.getId(), merchantOrder.getGatheringChannelId());
			if (superiorRebate == null) {
				log.error("上级账号没有开通该接单通道,无法获得返点;下级账号id:{},上级账号id:{},接单通道:{}", userAccount.getId(), superior.getId(),
						merchantOrder.getGatheringChannel().getChannelCode());
				break;
			}
			double rebate = NumberUtil.round(superiorRebate.getRebate() - userAccountRebate.getRebate(), 4)
					.doubleValue();
			if (rebate < 0) {
				log.error("订单返点异常,下级账号的返点不能大于上级账号;下级账号id:{},上级账号id:{}", userAccount.getId(), superior.getId());
				break;
			}
			double rebateAmount = NumberUtil.round(merchantOrder.getGatheringAmount() * rebate * 0.01, 4).doubleValue();
			OrderRebate orderRebate = OrderRebate.build(rebate, rebateAmount, merchantOrder.getId(), superior.getId());
			orderRebateRepo.save(orderRebate);
			userAccount = superior;

			superior = superior.getInviter();
			if(onlyLevelRebate){
				//多少级返佣
				//先获取多少级返佣参数
				if(i >= onlyLevelNum){
					superior = null;
				}
			}
			i++;
		}
	}

	@Transactional(readOnly = true)
	public void orderRebateAutoSettlement() {
		List<OrderRebate> orderRebates = orderRebateRepo.findBySettlementTimeIsNullAndAvailableFlagTrue();
		for (OrderRebate orderRebate : orderRebates) {
			redisTemplate.opsForList().leftPush(Constant.订单返点ID, orderRebate.getId());
		}
	}

	/**
	 * 通知指定的订单进行返点结算
	 *
	 * @param issueId
	 */
	@Transactional(readOnly = true)
	public void noticeOrderRebateSettlement(@NotBlank String orderId) {
		List<OrderRebate> orderRebates = orderRebateRepo.findByMerchantOrderIdAndAvailableFlagTrue(orderId);
		for (OrderRebate orderRebate : orderRebates) {
			redisTemplate.opsForList().leftPush(Constant.订单返点ID, orderRebate.getId());
		}
	}

	@Transactional(readOnly = true)
	public void actualIncomeRecordAutoSettlement() {
		List<ActualIncomeRecord> actualIncomeRecords = actualIncomeRecordRepo
				.findBySettlementTimeIsNullAndAvailableFlagTrue();
		for (ActualIncomeRecord actualIncomeRecord : actualIncomeRecords) {
			redisTemplate.opsForList().leftPush(Constant.实收金额记录ID, actualIncomeRecord.getId());
		}
	}

	@Transactional(readOnly = true)
	public void noticeActualIncomeRecordSettlement(@NotBlank String orderId) {
		ActualIncomeRecord actualIncomeRecord = actualIncomeRecordRepo
				.findTopByMerchantOrderIdAndAvailableFlagTrue(orderId);
		if (actualIncomeRecord != null) {
			redisTemplate.opsForList().leftPush(Constant.实收金额记录ID, actualIncomeRecord.getId());
		}
	}

	@Transactional
	public void actualIncomeRecordSettlement(@NotBlank String actualIncomeRecordId) {
		ActualIncomeRecord actualIncomeRecord = actualIncomeRecordRepo.getOne(actualIncomeRecordId);
		if (actualIncomeRecord.getSettlementTime() != null) {
			log.warn("当前的实收金额记录已结算,无法重复结算;id:{}", actualIncomeRecordId);
			return;
		}
		if (!actualIncomeRecord.getAvailableFlag()) {
			return;
		}
		actualIncomeRecord.setSettlementTime(new Date());
		actualIncomeRecordRepo.save(actualIncomeRecord);
		Merchant merchant = merchantRepo.getOne(actualIncomeRecord.getMerchantId());
		double withdrawableAmount = merchant.getWithdrawableAmount() + actualIncomeRecord.getActualIncome();
		merchant.setWithdrawableAmount(NumberUtil.round(withdrawableAmount, 4).doubleValue());
		merchantRepo.save(merchant);
	}

	/**
	 * 订单返点结算
	 */
	@Transactional
	public void orderRebateSettlement(@NotBlank String orderRebateId) {
		OrderRebate orderRebate = orderRebateRepo.getOne(orderRebateId);
		if (orderRebate.getSettlementTime() != null) {
			log.warn("当前的订单返点记录已结算,无法重复结算;id:{}", orderRebateId);
			return;
		}
		orderRebate.settlement();
		orderRebateRepo.save(orderRebate);
		UserAccount userAccount = orderRebate.getRebateAccount();
		double cashDeposit = userAccount.getCashDeposit() + orderRebate.getRebateAmount();
		userAccount.setCashDeposit(NumberUtil.round(cashDeposit, 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithOrderRebate(userAccount, orderRebate));
	}

	@Transactional(readOnly = true)
	public List<MyWaitConfirmOrderVO> findMyWaitConfirmOrder(@NotBlank String userAccountId) {
		return MyWaitConfirmOrderVO
				.convertFor(merchantOrderRepo.findByOrderStateInAndReceivedAccountIdOrderBySubmitTimeDesc(
						Arrays.asList(Constant.商户订单状态_已接单), userAccountId));
	}

	@Transactional(readOnly = true)
	public List<MyWaitConfirmOrderVO> findMyWaitComplaintOrder(@NotBlank String userAccountId) {
		return MyWaitConfirmOrderVO
				.convertFor(merchantOrderRepo.findByOrderStateInAndReceivedAccountIdOrderBySubmitTimeDesc(
						Arrays.asList(Constant.商户订单状态_已接单,Constant.商户订单状态_申诉中), userAccountId));
	}

	@Transactional(readOnly = true)
	public List<MyWaitReceivingOrderVO> findMyWaitReceivingOrder(@NotBlank String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		List<AccountReceiveOrderChannel> accountReceiveOrderChannels = accountReceiveOrderChannelRepo
				.findByUserAccountIdAndChannelDeletedFlagFalse(userAccountId);
		if (CollectionUtil.isEmpty(accountReceiveOrderChannels)) {
			throw new BizException(BizError.未设置接单通道无法接单);
		}
		ReceiveOrderSetting merchantOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (merchantOrderSetting.getShowAllOrder()) {
			List<MerchantOrder> specifiedOrders = merchantOrderRepo.findTop10ByOrderStateAndSpecifiedReceivedAccountId(
					Constant.商户订单状态_等待接单, userAccount.getUserName());
			List<MerchantOrder> waitReceivingOrders = merchantOrderRepo
					.findTop10ByOrderStateAndSpecifiedReceivedAccountIdIsNull(Constant.商户订单状态_等待接单);
			Collections.shuffle(waitReceivingOrders);
			specifiedOrders.addAll(waitReceivingOrders);
			return MyWaitReceivingOrderVO
					.convertFor(specifiedOrders.subList(0, specifiedOrders.size() >= 10 ? 10 : specifiedOrders.size()));
		}
		if (merchantOrderSetting.getUnfixedGatheringCodeReceiveOrder()) {
			//不固定金额收款码接单
			List<GatheringCode> gatheringCodes = gatheringCodeRepo.findByFixedGatheringAmount(false);
			if (CollectionUtil.isEmpty(gatheringCodes)) {
				throw new BizException(BizError.未设置收款码无法接单);
			}

			Map<String, String> gatheringChannelCodeMap = new HashMap<>();
			for (GatheringCode gatheringCode : gatheringCodes) {
				if (!Constant.收款码状态_正常.equals(gatheringCode.getState())) {
					continue;
				}
				if (!gatheringCode.getInUse()) {
					continue;
				}
				boolean flag = false;
				for (AccountReceiveOrderChannel accountReceiveOrderChannel : accountReceiveOrderChannels) {
					if (gatheringCode.getGatheringChannelId().equals(accountReceiveOrderChannel.getChannelId())
							&& Constant.账号接单通道状态_开启中.equals(accountReceiveOrderChannel.getState())) {
						flag = true;
					}
				}
				if (!flag) {
					continue;
				}
				gatheringChannelCodeMap.put(gatheringCode.getGatheringChannel().getChannelCode(),
						gatheringCode.getGatheringChannel().getChannelCode());
			}
			List<MerchantOrder> specifiedOrders = merchantOrderRepo.findTop10ByOrderStateAndSpecifiedReceivedAccountId(
					Constant.商户订单状态_等待接单, userAccount.getUserName());
			List<MerchantOrder> waitReceivingOrders = merchantOrderRepo
					.findTop10ByOrderStateAndGatheringAmountIsLessThanEqualAndGatheringChannelChannelCodeInAndAndSpecifiedReceivedAccountIdIsNullOrderBySubmitTimeDesc(
							Constant.商户订单状态_等待接单, userAccount.getCashDeposit(),
							new ArrayList<>(gatheringChannelCodeMap.keySet()));
			Collections.shuffle(waitReceivingOrders);
			specifiedOrders.addAll(waitReceivingOrders);
			return MyWaitReceivingOrderVO
					.convertFor(specifiedOrders.subList(0, specifiedOrders.size() >= 10 ? 10 : specifiedOrders.size()));
		}
		List<GatheringCode> gatheringCodes = gatheringCodeRepo.findByFixedGatheringAmount(true);
		if (CollectionUtil.isEmpty(gatheringCodes)) {
			throw new BizException(BizError.未设置收款码无法接单);
		}
		Map<String, List<Double>> gatheringChannelCodeMap = new HashMap<>();
		for (GatheringCode gatheringCode : gatheringCodes) {
			if (!Constant.收款码状态_正常.equals(gatheringCode.getState())) {
				continue;
			}
			if (!gatheringCode.getInUse()) {
				continue;
			}
			boolean flag = false;
			for (AccountReceiveOrderChannel accountReceiveOrderChannel : accountReceiveOrderChannels) {
				if (gatheringCode.getGatheringChannelId().equals(accountReceiveOrderChannel.getChannelId())
						&& Constant.账号接单通道状态_开启中.equals(accountReceiveOrderChannel.getState())) {
					flag = true;
				}
			}
			if (!flag) {
				continue;
			}
			if (userAccount.getCashDeposit() < gatheringCode.getGatheringAmount()) {
				continue;
			}
			if (gatheringChannelCodeMap.get(gatheringCode.getGatheringChannel().getChannelCode()) == null) {
				gatheringChannelCodeMap.put(gatheringCode.getGatheringChannel().getChannelCode(), new ArrayList<>());
			}
			gatheringChannelCodeMap.get(gatheringCode.getGatheringChannel().getChannelCode())
					.add(gatheringCode.getGatheringAmount());
		}
		List<MerchantOrder> waitReceivingOrders = new ArrayList<>();
		for (Entry<String, List<Double>> entry : gatheringChannelCodeMap.entrySet()) {
			if (CollectionUtil.isEmpty(entry.getValue())) {
				continue;
			}
			List<MerchantOrder> tmpOrders = merchantOrderRepo
					.findTop10ByOrderStateAndGatheringAmountInAndGatheringChannelChannelCodeAndSpecifiedReceivedAccountIdIsNullOrderBySubmitTimeDesc(
							Constant.商户订单状态_等待接单, entry.getValue(), entry.getKey());
			waitReceivingOrders.addAll(tmpOrders);
		}
		List<MerchantOrder> specifiedOrders = merchantOrderRepo
				.findTop10ByOrderStateAndSpecifiedReceivedAccountId(Constant.商户订单状态_等待接单, userAccount.getUserName());
		Collections.shuffle(waitReceivingOrders);
		specifiedOrders.addAll(waitReceivingOrders);
		return MyWaitReceivingOrderVO
				.convertFor(specifiedOrders.subList(0, specifiedOrders.size() >= 10 ? 10 : specifiedOrders.size()));
	}

	/**
	 * 接单
	 *
	 * @param param
	 * @return
	 */
	@Lock(keys = "'receiveOrder_' + #orderId")
	@Transactional
	public void receiveOrder(@NotBlank String userAccountId, @NotBlank String orderId) {
		ReceiveOrderSetting receiveOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (receiveOrderSetting.getStopStartAndReceiveOrder()) {
			throw new BizException(BizError.系统维护中不能接单);
		}
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		if(userAccount != null && "0".equals(userAccount.getState())){
			throw new BizException(BizError.你的账号已被禁用);
		}
		if (!Constant.接单状态_正在接单.equals(userAccount.getReceiveOrderState())) {
			throw new BizException(BizError.只有状态为正在接单才能进行接单);
		}
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(orderId);
		if (merchantOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!Constant.商户订单状态_等待接单.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.订单已被接或已取消);
		}
		AccountReceiveOrderChannel receiveOrderChannel = accountReceiveOrderChannelRepo
				.findByUserAccountIdAndChannelId(userAccountId, merchantOrder.getGatheringChannelId());
		if (receiveOrderChannel == null) {
			throw new BizException(BizError.接单通道未开通);
		}
		if (Constant.账号接单通道状态_已禁用.equals(receiveOrderChannel.getState())) {
			throw new BizException(BizError.接单通道已禁用);
		}
		if (Constant.账号接单通道状态_关闭中.equals(receiveOrderChannel.getState())) {
			throw new BizException(BizError.接单通道已关闭);
		}
		// 校验若达到接单上限,则不能接单
		List<MyWaitConfirmOrderVO> waitConfirmOrders = findMyWaitConfirmOrder(userAccountId);
		if (waitConfirmOrders.size() >= receiveOrderSetting.getReceiveOrderUpperLimit()) {
			throw new BizException(BizError.已达到接单数量上限);
		}
		MerchantOrder latestOrder = merchantOrderRepo.findTopByReceivedAccountIdOrderByReceivedTimeDesc(userAccountId);
		if (latestOrder != null) {
			if (!DateUtil.offset(new Date(), DateField.SECOND, -receiveOrderSetting.getReceiveOrderInterval())
					.isAfter(latestOrder.getReceivedTime())) {
				throw new BizException(BizError.接单太快了);
			}
		}

		/*// 禁止接重复单
		List<String> gatheringCodeIds = new ArrayList<String>();
		if (receiveOrderSetting.getBanReceiveRepeatOrder()) {
			//是指同一个码未处理完地点不能接单
			//获取该用户的收款码
			List<GatheringCode> list = gatheringCodeRepo.findByUserAccountIdAndInUseTrue(userAccountId);

			if(list.size() == 0){
				throw new BizException(BizError.没有可接单的收款码);
			}

			//findMyWaitComplaintOrder 根据用户id查询该用户 正在接单或者申诉，订单数量
			waitConfirmOrders = findMyWaitComplaintOrder(userAccountId);

			//去重
			gatheringCodeIds = ListUtils.duplicateRemoval(waitConfirmOrders);
			// gatheringCodeIds 表示目前有多少个码在处理问题，接单或者申诉
			// gatheringCodeIds 如果大于或者等于 该用户的收款码数，禁止接重复单
			if(gatheringCodeIds.size() >= list.size()){
				throw new BizException(BizError.禁止接重复单);
			}
		}*/


		// 禁止接重复单
		List<String> gatheringCodeIds = new ArrayList<String>();
		if (receiveOrderSetting.getBanReceiveRepeatOrder()) {
			/* 是指同一个码不能接相同金额的单 */

			/* 同一收款方式禁止接相同金额的订单 */
			for (MyWaitConfirmOrderVO waitConfirmOrder : waitConfirmOrders) {
				if (waitConfirmOrder.getGatheringChannelId().equals(merchantOrder.getGatheringChannelId())
						&& waitConfirmOrder.getGatheringAmount().compareTo(merchantOrder.getGatheringAmount()) == 0) {
					throw new BizException(BizError.禁止接重复金额的订单);
				}
			}

			/*//已接单
			for (MyWaitConfirmOrderVO waitConfirmOrder : waitConfirmOrders) {
				if (waitConfirmOrder.getGatheringChannelId().equals(merchantOrder.getGatheringChannelId())) {
					throw new BizException(BizError.禁止接重复单);
				}
			}
			//查询有没有申诉订单
			waitConfirmOrders = findMyWaitComplaintOrder(userAccountId);
			for (MyWaitConfirmOrderVO waitConfirmOrder : waitConfirmOrders) {
				if (waitConfirmOrder.getGatheringChannelId().equals(merchantOrder.getGatheringChannelId())) {
					throw new BizException(BizError.禁止接重复单);
				}
			}*/

			//获取该用户的收款码
			List<GatheringCode> list = gatheringCodeRepo.findByUserAccountIdAndInUseTrue(userAccountId);

			if(list.size() == 0){
				throw new BizException(BizError.没有可接单的收款码);
			}

			//findMyWaitComplaintOrder 根据用户id查询该用户 正在接单或者申诉，订单数量
			//waitConfirmOrders = findMyWaitComplaintOrder(userAccountId);

			/*for (MyWaitConfirmOrderVO waitConfirmOrder : waitConfirmOrders) {
				for (GatheringCode gc : list) {
					if(waitConfirmOrder.getGatheringCodeId().equals(gc.getId())){
						i = i + 1 ;
					}
				}
			}
			if(i > list.size()){
				throw new BizException(BizError.禁止接重复单);
			}*/

		}


		/* 同一码禁止接相同金额的订单 */
		if (receiveOrderSetting.getBanReceiveCodeOrder()) {
			//获取该用户的收款码
			List<GatheringCode> list = gatheringCodeRepo.findByUserAccountIdAndInUseTrue(userAccountId);
			if(list.size() == 0){
				throw new BizException(BizError.没有可接单的收款码);
			}

			//findMyWaitComplaintOrder 根据用户id查询该用户 正在接单或者申诉，订单列表
			waitConfirmOrders = findMyWaitComplaintOrder(userAccountId);
			//订单去重
			waitConfirmOrders = ListUtils.duplicateRemovalList(waitConfirmOrders);
			//去重
			//gatheringCodeIds = ListUtils.duplicateRemoval(waitConfirmOrders);

			/**
			 * 假如客户有2个码
			 * 目前客户其中一个码已接单
			 *
			 * list=2
			 * waitConfirmOrders=1
			 */
			for (MyWaitConfirmOrderVO waitConfirmOrder : waitConfirmOrders) {
				for (GatheringCode gc : list) {
					if(waitConfirmOrder.getGatheringCodeId().equals(gc.getId())
							&& waitConfirmOrder.getGatheringAmount().compareTo(merchantOrder.getGatheringAmount()) == 0){
						gatheringCodeIds.add(gc.getId());
					}
				}
			}
			if(gatheringCodeIds.size() == list.size()){
				throw new BizException(BizError.禁止接重复金额的订单);
			}
		}


		if (receiveOrderSetting.getCashDepositMinimumRequire() != null) {
			if (userAccount.getCashDeposit() < receiveOrderSetting.getCashDepositMinimumRequire()) {
				throw new BizException(BizError.未达到接单账户余额最低要求);
			}
		}
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		if (cashDeposit - receiveOrderSetting.getCashPledge() < 0) {
			throw new BizException(BizError.账户余额不足无法接单);
		}
		GatheringCodeUsageVO gatheringCode = getGatheringCode(gatheringCodeIds,userAccountId,
				merchantOrder.getGatheringChannel().getChannelCode(), merchantOrder.getGatheringAmount());
		if (gatheringCode == null) {
			throw new BizException(BizError.无法接单找不到对应金额的收款码);
		}



		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		Integer orderEffectiveDuration = receiveOrderSetting.getOrderPayEffectiveDuration();
		merchantOrder.updateReceived(userAccount.getId(), gatheringCode.getId(), gatheringCode.getStorageId(),
				receiveOrderChannel.getRebate());
		merchantOrder.updateUsefulTime(
				DateUtil.offset(merchantOrder.getReceivedTime(), DateField.MINUTE, orderEffectiveDuration));
		merchantOrderRepo.save(merchantOrder);
		accountChangeLogRepo.save(AccountChangeLog.buildWithReceiveOrderDeduction(userAccount, merchantOrder));
	}

	@Transactional(readOnly = true)
	public PageResult<MerchantOrderVO> findMerchantOrderByPage(MerchantOrderQueryCondParam param) {
		Specification<MerchantOrder> spec = new Specification<MerchantOrder>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getMerchantName())) {
					predicates.add(builder.equal(root.join("merchant", JoinType.INNER).get("merchantName"),
							param.getMerchantName()));
				}
				if (StrUtil.isNotBlank(param.getMerchantOrderNo())) {
					predicates.add(builder.equal(root.join("payInfo", JoinType.INNER).get("orderNo"),
							param.getMerchantOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.join("gatheringChannel", JoinType.INNER).get("channelCode"),
							param.getGatheringChannelCode()));
				}
				if (StrUtil.isNotBlank(param.getOrderState())) {
					predicates.add(builder.equal(root.get("orderState"), param.getOrderState()));
				}
				if (StrUtil.isNotBlank(param.getReceiverUserName())) {
					predicates.add(builder.like(root.join("receivedAccount", JoinType.INNER).get("userName"),
							"%" + param.getReceiverUserName() + "%"));
				}
				if (param.getSubmitStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getSubmitStartTime())));
				}
				if (param.getSubmitEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getSubmitEndTime())));
				}
				if (param.getReceiveOrderStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.beginOfDay(param.getReceiveOrderStartTime())));
				}
				if (param.getReceiveOrderEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.endOfDay(param.getReceiveOrderEndTime())));
				}
				if (StrUtil.isNotBlank(param.getPayNoticeState())) {
					predicates.add(builder.equal(root.join("payInfo", JoinType.INNER).get("noticeState"),
							param.getPayNoticeState()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MerchantOrder> result = merchantOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<MerchantOrderVO> pageResult = new PageResult<>(MerchantOrderVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<ReceiveOrderRecordVO> findMyReceiveOrderRecordByPage(MyReceiveOrderRecordQueryCondParam param) {
		Specification<MerchantOrder> spec = new Specification<MerchantOrder>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.join("gatheringChannel", JoinType.INNER).get("channelCode"),
							param.getGatheringChannelCode()));
				}
				if (StrUtil.isNotBlank(param.getReceiverUserName())) {
					predicates.add(builder.equal(root.join("receivedAccount", JoinType.INNER).get("userName"),
							param.getReceiverUserName()));
				}
				if (param.getReceiveOrderTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.beginOfDay(param.getReceiveOrderTime())));
					predicates.add(builder.lessThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.endOfDay(param.getReceiveOrderTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MerchantOrder> result = merchantOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<ReceiveOrderRecordVO> pageResult = new PageResult<>(
				ReceiveOrderRecordVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	/**
	 * 取消订单
	 *
	 * @param id
	 */
	@Transactional
	public void cancelOrder(@NotBlank String id) {
		MerchantOrder platformOrder = merchantOrderRepo.getOne(id);
		if (!Constant.商户订单状态_等待接单.equals(platformOrder.getOrderState())) {
			throw new BizException(BizError.只有等待接单状态的商户订单才能取消);
		}
		platformOrder.setOrderState(Constant.商户订单状态_人工取消);
		platformOrder.setDealTime(new Date());
		merchantOrderRepo.save(platformOrder);
	}

	@Transactional
	public void forceCancelOrder(@NotBlank String id) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(id);
		if (!Constant.商户订单状态_已支付.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.只有已支付状态的商户订单才能强制取消);
		}
		merchantOrder.customerCancelOrderRefund();
		merchantOrderRepo.save(merchantOrder);
		UserAccount userAccount = merchantOrder.getReceivedAccount();
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() + merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithCustomerCancelOrderRefund(userAccount, merchantOrder));
		cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - merchantOrder.getBounty(), 4).doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.账户余额不足);
		}
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo
				.save(AccountChangeLog.buildWithCustomerCancelOrderDeductBounty(userAccount, merchantOrder));
		List<OrderRebate> orderRebates = orderRebateRepo.findByMerchantOrderIdAndAvailableFlagTrue(id);
		for (OrderRebate orderRebate : orderRebates) {
			orderRebate.setAvailableFlag(false);
			orderRebateRepo.save(orderRebate);
			UserAccount superiorAccount = orderRebate.getRebateAccount();
			Double superiorAccountCashDeposit = NumberUtil
					.round(superiorAccount.getCashDeposit() - orderRebate.getRebateAmount(), 4).doubleValue();
			if (superiorAccountCashDeposit < 0) {
				throw new BizException(BizError.账户余额不足);
			}
			superiorAccount.setCashDeposit(superiorAccountCashDeposit);
			userAccountRepo.save(superiorAccount);
			accountChangeLogRepo
					.save(AccountChangeLog.buildWithCustomerCancelOrderDeductRebate(superiorAccount, orderRebate));
		}

		ActualIncomeRecord actualIncomeRecord = actualIncomeRecordRepo.findTopByMerchantOrderIdAndAvailableFlagTrue(id);
		if (actualIncomeRecord != null) {
			actualIncomeRecord.setAvailableFlag(false);
			actualIncomeRecordRepo.save(actualIncomeRecord);
			Merchant merchant = actualIncomeRecord.getMerchant();
			Double withdrawableAmount = NumberUtil
					.round(merchant.getWithdrawableAmount() - actualIncomeRecord.getActualIncome(), 4).doubleValue();
			if (withdrawableAmount < 0) {
				throw new BizException(BizError.可提现金额不足);
			}
			merchant.setWithdrawableAmount(withdrawableAmount);
			merchantRepo.save(merchant);
		}
	}

	@Transactional
	public void orderTimeoutDeal() {
		Date now = new Date();
		List<MerchantOrder> orders = merchantOrderRepo.findByOrderStateAndUsefulTimeLessThan(Constant.商户订单状态_等待接单, now);
		for (MerchantOrder order : orders) {
			order.setDealTime(now);
			order.setOrderState(Constant.商户订单状态_超时取消);
			merchantOrderRepo.save(order);
		}
	}

	public void noticeDispatchOrder() {
		ReceiveOrderSetting receiveOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (!receiveOrderSetting.getDispatchMode()) {
			return;
		}
		List<MerchantOrder> merchantOrders = merchantOrderRepo.findByOrderState(Constant.商户订单状态_等待接单);
		for (MerchantOrder merchantOrder : merchantOrders) {
			ThreadPoolUtils.getDispatchOrderPool().schedule(() -> {
				redisTemplate.opsForList().leftPush(Constant.派单订单ID, merchantOrder.getId());
			}, 1, TimeUnit.SECONDS);
		}
	}

	public List<QueueRecordVO> realTimeQueueRecord() {
		 List<QueueRecord> queueRecords = queueRecordRepo.findByUsedIsFalseOrderByQueueTime();
		 return QueueRecordVO.convertFor(queueRecords);
	}

	@Lock(keys = "'dispatchOrder_' + #orderId")
	@Transactional
	public void dispatchOrder(@NotBlank String orderId) {
		List<String> userAccountIds = new ArrayList<>();
		  Set<String> keys = redisTemplate.keys("DISPATCH_ORDER_ACCOUNT_*");
		  for (String key : keys) {
		   userAccountIds.add(key.split("DISPATCH_ORDER_ACCOUNT_")[1]);
		  }
		  if (CollectionUtil.isEmpty(userAccountIds)) {
		   userAccountIds.add("NON_EXISTENT_ID");
		  }
		  List<QueueRecord> queueRecords = queueRecordRepo.findByUserAccountIdNotInAndUsedIsFalseOrderByQueueTime(userAccountIds);
		  if (CollectionUtil.isEmpty(queueRecords)) {
			  return;
		  }
		  ReceiveOrderSetting receiveOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		  if (receiveOrderSetting.getSameCityPriority()) {
			   MerchantOrder merchantOrder = merchantOrderRepo.getOne(orderId);
			   String cityCode = merchantOrder.getPayInfo().getCityCode();
			   if (StrUtil.isNotBlank(cityCode)) {
				   queueRecords = sameCitySort(cityCode, queueRecords);
			   }
		  }

		for (QueueRecord queueRecord : queueRecords) {
			String userAccountId = queueRecord.getUserAccountId();
			redisTemplate.opsForValue().set("DISPATCH_ORDER_ACCOUNT_" + userAccountId, userAccountId, 5L, TimeUnit.SECONDS);
			String note = "接单成功";
			try {
				receiveOrder(userAccountId, orderId);
			} catch (BizException e) {
				note = e.getMsg();
			}
			if (!"接单成功".equals(note)) {
				System.err.println("异常信息：" + note);
				continue;
			}
			redisTemplate.delete("DISPATCH_ORDER_ACCOUNT_" + userAccountId);
			queueRecord.used(note);
			queueRecordRepo.save(queueRecord);

			UserAccount userAccount = userAccountRepo.getOne(userAccountId);
			if (!Constant.接单状态_正在接单.equals(userAccount.getReceiveOrderState())) {
				return;
			}
			QueueRecord newQueueRecord = QueueRecord.build(userAccountId);
			queueRecordRepo.save(newQueueRecord);
		}
	}

	public List<QueueRecord> sameCitySort(String cityCode, List<QueueRecord> queueRecords) {
	  List<QueueRecord> list = new ArrayList<>();
	  Map<String, String> queueRecordMap = new HashMap<String, String>();
	  // 取同一城市
	  for (QueueRecord queueRecord : queueRecords) {
	   if (cityCode.equals(queueRecord.getCityCode())) {
	    list.add(queueRecord);
	    queueRecordMap.put(queueRecord.getId(), queueRecord.getId());
	   }
	  }
	  // 取同一省份
	  for (QueueRecord queueRecord : queueRecords) {
	   if (StrUtil.isNotBlank(queueRecord.getCityCode()) && cityCode.length() >= 2
	     && cityCode.substring(0, 2).equals(queueRecord.getCityCode().substring(0, 2))
	     && queueRecordMap.get(queueRecord.getId()) == null) {
	    list.add(queueRecord);
	    queueRecordMap.put(queueRecord.getId(), queueRecord.getId());
	   }
	  }
	  // 取别的省份
	  for (QueueRecord queueRecord : queueRecords) {
	   if (queueRecordMap.get(queueRecord.getId()) == null) {
	    list.add(queueRecord);
	    queueRecordMap.put(queueRecord.getId(), queueRecord.getId());
	   }
	  }
	  return list;
	}

	@Transactional
	public void manualStartOrder(@NotEmpty List<ManualStartOrderParam> params) {
		for (ManualStartOrderParam param : params) {
			Merchant merchant = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(param.getMerchantNum());
			String amount = new DecimalFormat("###################.###########").format(param.getGatheringAmount());
			StartOrderParam startOrderParam = new StartOrderParam();
			startOrderParam.setMerchantNum(param.getMerchantNum());
			startOrderParam.setOrderNo(param.getOrderNo());
			startOrderParam.setPayType(param.getGatheringChannelCode());
			startOrderParam.setAmount(amount);
			startOrderParam.setNotifyUrl(param.getNotifyUrl());
			startOrderParam.setReturnUrl(param.getReturnUrl());
			startOrderParam.setAttch(param.getAttch());
			startOrderParam.setSpecifiedReceivedAccountId(param.getSpecifiedReceivedAccountId());
			startOrderParam.setPublishTime(param.getPublishTime());
			String sign = startOrderParam.getMerchantNum() + startOrderParam.getOrderNo() + amount
					+ param.getNotifyUrl() + merchant.getSecretKey();
			sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);
			startOrderParam.setSign(sign);
			startOrderInner(startOrderParam);
		}

	}

	@ParamValid
	@Transactional
	public StartOrderSuccessVO startOrder(StartOrderParam param) {
		if (StrUtil.isNotBlank(param.getIp())) {
		   IpInfoVO ipInfo = IpUtils.getIpInfo(param.getIp());
		   if (ipInfo != null) {
		    param.setProvince(ipInfo.getProvince());
		    param.setCity(ipInfo.getCity());
		    param.setCityCode(ipInfo.getCityCode());
		   }
		}
		StartOrderSuccessVO vo = startOrderInner(param);
		ThreadPoolUtils.getDispatchOrderPool().schedule(() -> {
			redisTemplate.opsForList().leftPush(Constant.派单订单ID, vo.getId());
		}, 1, TimeUnit.SECONDS);
		return vo;
	}

	@ParamValid
	@Transactional
	public StartOrderSuccessVO startOrderPic(StartOrderParam param) {
		StartOrderSuccessVO vo = startOrderInnerPic(param);
		ThreadPoolUtils.getDispatchOrderPool().schedule(() -> {
			redisTemplate.opsForList().leftPush(Constant.派单订单ID, vo.getId());
		}, 1, TimeUnit.SECONDS);
		return vo;
	}

	@ParamValid
	@Transactional
	public StartOrderSuccessVO startOrderInner(StartOrderParam param) {
		List<MerchantOrderPayInfo> info = merchantOrderPayInfoRepo.findByOrderNo(param.getOrderNo());
		if(info.size() > 0){
			throw new BizException(BizError.商户订单号不能重复);
		}

		ReceiveOrderSetting receiveOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (receiveOrderSetting.getStopStartAndReceiveOrder()) {
			throw new BizException(BizError.系统维护中不能发起订单);
		}
		Merchant merchant = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(param.getMerchantNum());
		if (merchant == null) {
			throw new BizException(BizError.商户未接入);
		}
		if ("0".equals(merchant.getState()) ) {
			throw new BizException(BizError.商户账号已被禁用);
		}

		if (!NumberUtil.isNumber(param.getAmount())) {
			throw new BizException(BizError.金额格式不正确);
		}
		if (Double.parseDouble(param.getAmount()) <= 0) {
			throw new BizException(BizError.金额不能小于或等于0);
		}
		String sign = param.getMerchantNum() + param.getOrderNo() + param.getAmount() + param.getNotifyUrl()
				+ merchant.getSecretKey();
		sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);
		if (!sign.equals(param.getSign())) {
			throw new BizException(BizError.签名不正确);
		}
		GatheringChannel gatheringChannel = gatheringChannelRepo.findByChannelCodeAndDeletedFlagIsFalse(param.getPayType());
		if (gatheringChannel == null) {
			throw new BizException(BizError.发起订单失败通道不存在);
		}
		if (!gatheringChannel.getEnabled()) {
			throw new BizException(BizError.发起订单失败通道维护中);
		}
		GatheringChannelRate gatheringChannelRate = gatheringChannelRateRepo
				.findByMerchantIdAndChannelChannelCode(merchant.getId(), param.getPayType());
		if (gatheringChannelRate == null) {
			throw new BizException(BizError.发起订单失败该通道未开通);
		}
		if (!gatheringChannelRate.getEnabled()) {
			throw new BizException(BizError.发起订单失败该通道已关闭);
		}
		if (Double.parseDouble(param.getAmount()) < gatheringChannelRate.getMinAmount()) {
			throw new BizException(BizError.业务异常.getCode(),
					MessageFormat.format("最低限额为:{0}", gatheringChannelRate.getMinAmount()));
		}
		if (Double.parseDouble(param.getAmount()) > gatheringChannelRate.getMaxAmount()) {
			throw new BizException(BizError.业务异常.getCode(),
					MessageFormat.format("最高限额为:{0}", gatheringChannelRate.getMaxAmount()));
		}

		Integer orderEffectiveDuration = receiveOrderSetting.getReceiveOrderEffectiveDuration();
		MerchantOrder merchantOrder = param.convertToPo(merchant.getId(), gatheringChannel.getId(),
				gatheringChannelRate.getRate(), orderEffectiveDuration);
		MerchantOrderPayInfo payInfo = param.convertToPayInfoPo(merchantOrder.getId());
		merchantOrder.setPayInfoId(payInfo.getId());
		merchantOrderRepo.save(merchantOrder);
		merchantOrderPayInfoRepo.save(payInfo);
		return StartOrderSuccessVO.convertFor(merchantOrder.getId());
	}

	@ParamValid
	@Transactional
	public StartOrderSuccessVO startOrderInnerPic(StartOrderParam param) {
		List<MerchantOrderPayInfo> info = merchantOrderPayInfoRepo.findByOrderNo(param.getOrderNo());
		if(info.size() > 0){
			throw new BizException(BizError.商户订单号不能重复);
		}

		ReceiveOrderSetting receiveOrderSetting = receiveOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (receiveOrderSetting.getStopStartAndReceiveOrder()) {
			throw new BizException(BizError.系统维护中不能发起订单);
		}
		Merchant merchant = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(param.getMerchantNum());
		if (merchant == null) {
			throw new BizException(BizError.商户未接入);
		}
		if ("0".equals(merchant.getState()) ) {
			throw new BizException(BizError.商户账号已被禁用);
		}

		if (!NumberUtil.isNumber(param.getAmount())) {
			throw new BizException(BizError.金额格式不正确);
		}
		if (Double.parseDouble(param.getAmount()) <= 0) {
			throw new BizException(BizError.金额不能小于或等于0);
		}
		/*String sign = param.getMerchantNum() + param.getOrderNo() + param.getAmount() + param.getNotifyUrl()
				+ merchant.getSecretKey();
		sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);
		if (!sign.equals(param.getSign())) {
			throw new BizException(BizError.签名不正确);
		}*/
		GatheringChannel gatheringChannel = gatheringChannelRepo
				.findByChannelCodeAndDeletedFlagIsFalse(param.getPayType());
		if (gatheringChannel == null) {
			throw new BizException(BizError.发起订单失败通道不存在);
		}
		if (!gatheringChannel.getEnabled()) {
			throw new BizException(BizError.发起订单失败通道维护中);
		}
		GatheringChannelRate gatheringChannelRate = gatheringChannelRateRepo
				.findByMerchantIdAndChannelChannelCode(merchant.getId(), param.getPayType());
		if (gatheringChannelRate == null) {
			throw new BizException(BizError.发起订单失败该通道未开通);
		}
		if (!gatheringChannelRate.getEnabled()) {
			throw new BizException(BizError.发起订单失败该通道已关闭);
		}
		if (Double.parseDouble(param.getAmount()) < gatheringChannelRate.getMinAmount()) {
			throw new BizException(BizError.业务异常.getCode(),
					MessageFormat.format("最低限额为:{0}", gatheringChannelRate.getMinAmount()));
		}
		if (Double.parseDouble(param.getAmount()) > gatheringChannelRate.getMaxAmount()) {
			throw new BizException(BizError.业务异常.getCode(),
					MessageFormat.format("最高限额为:{0}", gatheringChannelRate.getMaxAmount()));
		}

		Integer orderEffectiveDuration = receiveOrderSetting.getReceiveOrderEffectiveDuration();
		MerchantOrder merchantOrder = param.convertToPo(merchant.getId(), gatheringChannel.getId(),
				gatheringChannelRate.getRate(), orderEffectiveDuration);
		MerchantOrderPayInfo payInfo = param.convertToPayInfoPo(merchantOrder.getId());
		merchantOrder.setPayInfoId(payInfo.getId());
		merchantOrderRepo.save(merchantOrder);
		merchantOrderPayInfoRepo.save(payInfo);
		return StartOrderSuccessVO.convertForPic(payInfo.getOrderNo(),merchantOrder.getId());
	}

	@Transactional(readOnly = true)
	public void paySuccessAutoAsynNotice() {
		List<MerchantOrderPayInfo> payInfos = merchantOrderPayInfoRepo
				.findByNoticeStateAndMerchantOrderOrderState(Constant.商户订单支付通知状态_未通知, Constant.商户订单状态_已支付);
		for (MerchantOrderPayInfo payInfo : payInfos) {
			noticePaySuccessAsynNotice(payInfo.getMerchantOrderId());
		}
	}

	@Transactional(readOnly = true)
	public void noticePaySuccessAsynNotice(@NotBlank String merchantOrderId) {
		redisTemplate.opsForList().leftPush(Constant.异步通知订单ID, merchantOrderId);
	}

	/**
	 * 支付成功异步通知
	 *
	 * @param merchantOrderId
	 */
	@Lock(keys = "'paySuccessAsynNotice_' + #merchantOrderId")
	@Transactional
	public String paySuccessAsynNotice(@NotBlank String merchantOrderId) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(merchantOrderId);
		if (!Constant.商户订单状态_已支付.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.只有已支付的订单才能进行异步通知);
		}
		MerchantOrderPayInfo payInfo = merchantOrderPayInfoRepo.findByMerchantOrderId(merchantOrderId);
		if (Constant.商户订单支付通知状态_通知成功.equals(payInfo.getNoticeState())) {
			log.warn("商户订单支付已通知成功,无需重复通知;商户订单id为{}", merchantOrderId);
			return Constant.商户订单通知成功返回值;
		}
		Merchant merchant = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(payInfo.getMerchantNum());
		if (merchant == null) {
			throw new BizException(BizError.商户未接入);
		}

		String sign = Constant.商户订单支付成功 + payInfo.getMerchantNum() + payInfo.getOrderNo() + payInfo.getAmount()
				+ merchant.getSecretKey();
		sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("merchantNum", payInfo.getMerchantNum());
		paramMap.put("orderNo", payInfo.getOrderNo());
		paramMap.put("platformOrderNo", payInfo.getMerchantOrder().getOrderNo());
		paramMap.put("amount", payInfo.getAmount());
		paramMap.put("actualPayAmount", merchantOrder.getGatheringAmount());
		paramMap.put("attch", payInfo.getAttch());
		paramMap.put("state", Constant.商户订单支付成功);
		paramMap.put("payTime",
				DateUtil.format(payInfo.getMerchantOrder().getConfirmTime(), DatePattern.NORM_DATETIME_PATTERN));
		paramMap.put("sign", sign);
		String result = "fail";
		// 通知3次
		for (int i = 0; i < 3; i++) {
			try {
				result = HttpUtil.get(payInfo.getNotifyUrl(), paramMap, 2500);
				if (Constant.商户订单通知成功返回值.equals(result)) {
					break;
				}
			} catch (Exception e) {
				result = e.getMessage();
				log.error(MessageFormat.format("商户订单支付成功异步通知地址请求异常,id为{0}", merchantOrderId), e);
			}
		}
		payInfo.setNoticeState(
				Constant.商户订单通知成功返回值.equals(result) ? Constant.商户订单支付通知状态_通知成功 : Constant.商户订单支付通知状态_通知失败);
		merchantOrderPayInfoRepo.save(payInfo);
		return result;
	}

	@Transactional(readOnly = true)
	public PageResult<ReceiveOrderRecordVO> findLowerLevelAccountReceiveOrderRecordByPage(
			LowerLevelAccountReceiveOrderQueryCondParam param) {
		UserAccount currentAccount = userAccountRepo.getOne(param.getCurrentAccountId());
		UserAccount lowerLevelAccount = currentAccount;
		if (StrUtil.isNotBlank(param.getUserName())) {
			lowerLevelAccount = userAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
			if (lowerLevelAccount == null) {
				throw new BizException(BizError.用户名不存在);
			}
			// 说明该用户名对应的账号不是当前账号的下级账号
			if (!lowerLevelAccount.getAccountLevelPath().startsWith(currentAccount.getAccountLevelPath())) {
				throw new BizException(BizError.不是上级账号无权查看该账号及下级的接单记录);
			}
		}
		String lowerLevelAccountId = lowerLevelAccount.getId();
		String lowerLevelAccountLevelPath = lowerLevelAccount.getAccountLevelPath();

		Specification<MerchantOrder> spec = new Specification<MerchantOrder>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getUserName())) {
					predicates.add(builder.equal(root.get("receivedAccountId"), lowerLevelAccountId));
				} else {
					predicates.add(builder.like(root.join("receivedAccount", JoinType.INNER).get("accountLevelPath"),
							lowerLevelAccountLevelPath + "%"));
				}
				if (StrUtil.isNotBlank(param.getOrderState())) {
					predicates.add(builder.equal(root.get("orderState"), param.getOrderState()));
				}
				if (StrUtil.isNotBlank(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.join("gatheringChannel", JoinType.INNER).get("channelCode"),
							param.getGatheringChannelCode()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MerchantOrder> result = merchantOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<ReceiveOrderRecordVO> pageResult = new PageResult<>(
				ReceiveOrderRecordVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void updateNote(@NotBlank String id, String note, String merchantId) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(id);
		if (!merchantOrder.getMerchantId().equals(merchantId)) {
			throw new BizException(BizError.无权修改商户订单备注);
		}
		updateNoteInner(id, note);
	}

	@Transactional
	public void updateNoteInner(@NotBlank String id, String note) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(id);
		merchantOrder.setNote(note);
		merchantOrderRepo.save(merchantOrder);
	}

}
