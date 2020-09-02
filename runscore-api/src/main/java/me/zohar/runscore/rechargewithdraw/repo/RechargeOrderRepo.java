package me.zohar.runscore.rechargewithdraw.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.rechargewithdraw.domain.RechargeOrder;

public interface RechargeOrderRepo
		extends JpaRepository<RechargeOrder, String>, JpaSpecificationExecutor<RechargeOrder> {

	long deleteBySubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(Date startTime, Date endTime);

	List<RechargeOrder> findByOrderStateAndUsefulTimeLessThan(String orderState, Date usefulTime);

	RechargeOrder findByOrderNo(String orderNo);

	/**
	 * 获取所有已支付,未进行结算的充值订单
	 * 
	 * @return
	 */
	List<RechargeOrder> findByPayTimeIsNotNullAndSettlementTimeIsNullOrderBySubmitTime();

	List<RechargeOrder> findByOrderStateAndUserAccountId(String orderState, String userAccountId);

}
