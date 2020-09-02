package me.zohar.runscore;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.runscore.common.utils.ThreadPoolUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.service.MerchantOrderService;
import me.zohar.runscore.rechargewithdraw.service.RechargeService;

@Slf4j
@Component
public class RedisMessageListener {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RechargeService rechargeService;

	@Autowired
	private MerchantOrderService merchantOrderService;

	@PostConstruct
	public void init() {
		listenRechargeSettlement();
		listenPaidMerchantOrder();
		listenDispatchOrder();
	}

	public void listenRechargeSettlement() {
		new Thread(() -> {
			while (true) {
				try {
					String orderNo = redisTemplate.opsForList().rightPop(Constant.充值订单_已支付订单单号, 1200L,
							TimeUnit.MILLISECONDS);
					if (StrUtil.isBlank(orderNo)) {
						continue;
					}

					ThreadPoolUtils.getRechargeSettlementPool().execute(() -> {
						try {
							log.info("系统进行充值结算操作,订单单号为{}", orderNo);
							rechargeService.rechargeOrderSettlement(orderNo);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行充值结算操作出现异常,订单单号为{0}", orderNo), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("充值结算消息队列异常", e);
				}
			}
		}).start();
	}

	public void listenDispatchOrder() {
		new Thread(() -> {
			while (true) {
				try {
					String orderId = redisTemplate.opsForList().rightPop(Constant.派单订单ID, 1100L, TimeUnit.MILLISECONDS);
					if (StrUtil.isBlank(orderId)) {
						continue;
					}

					ThreadPoolUtils.getRechargeSettlementPool().execute(() -> {
						try {
							log.info("系统进行派单操作,订单id为{}", orderId);
							merchantOrderService.dispatchOrder(orderId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行派单操作出现异常,订单id为{0}", orderId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("派单消息队列异常", e);
				}
			}
		}).start();
	}

	public void listenPaidMerchantOrder() {
		new Thread(() -> {
			while (true) {
				try {
					String orderId = redisTemplate.opsForList().rightPop(Constant.商户订单ID, 1300L, TimeUnit.MILLISECONDS);
					if (StrUtil.isBlank(orderId)) {
						continue;
					}

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统通知该订单进行支付成功异步通知,id为{}", orderId);
							merchantOrderService.noticePaySuccessAsynNotice(orderId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统通知该订单进行支付成功异步通知异常,id为{0}", orderId), e);
							throw new RuntimeException();
						}
					});

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统通知该订单进行返点结算,id为{}", orderId);
							merchantOrderService.noticeOrderRebateSettlement(orderId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统通知该订单进行返点结算异常,id为{0}", orderId), e);
							throw new RuntimeException();
						}
					});

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统通知该订单进行实收金额记录结算,id为{}", orderId);
							merchantOrderService.noticeActualIncomeRecordSettlement(orderId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统通知该订单进行实收金额记录结算异常,id为{0}", orderId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("商户订单ID消息队列异常", e);
				}
			}
		}).start();

		new Thread(() -> {
			while (true) {
				try {
					String orderId = redisTemplate.opsForList().rightPop(Constant.异步通知订单ID, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(orderId)) {
						continue;
					}

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统进行异步通知操作,id为{}", orderId);
							merchantOrderService.paySuccessAsynNotice(orderId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行异步通知操作出现异常,id为{0}", orderId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("订单异步通知消息队列异常", e);
				}
			}
		}).start();

		new Thread(() -> {
			while (true) {
				try {
					String orderRebateId = redisTemplate.opsForList().rightPop(Constant.订单返点ID, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(orderRebateId)) {
						continue;
					}

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统进行订单返点结算操作,id为{}", orderRebateId);
							merchantOrderService.orderRebateSettlement(orderRebateId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行订单返点结算操作出现异常,id为{0}", orderRebateId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("订单返点结算消息队列异常", e);
				}
			}
		}).start();

		new Thread(() -> {
			while (true) {
				try {
					String actualIncomeRecordId = redisTemplate.opsForList().rightPop(Constant.实收金额记录ID, 2L,
							TimeUnit.SECONDS);
					if (StrUtil.isBlank(actualIncomeRecordId)) {
						continue;
					}

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统进行实收金额记录结算操作,id为{}", actualIncomeRecordId);
							merchantOrderService.actualIncomeRecordSettlement(actualIncomeRecordId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行实收金额记录结算操作出现异常,id为{0}", actualIncomeRecordId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("实收金额记录结算消息队列异常", e);
				}
			}
		}).start();

		new Thread(() -> {
			while (true) {
				try {
					String freezeRecordId = redisTemplate.opsForList().rightPop(Constant.冻结记录ID, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(freezeRecordId)) {
						continue;
					}

					ThreadPoolUtils.getPaidMerchantOrderPool().execute(() -> {
						try {
							log.info("系统进行释放冻结订单操作,id为{}", freezeRecordId);
							merchantOrderService.releaseFreezeOrder(freezeRecordId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行释放冻结订单操作出现异常,id为{0}", freezeRecordId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("释放冻结订单消息队列异常", e);
				}
			}
		}).start();
	}

}
