package me.zohar.runscore.merchant.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.OrderRebate;

public interface OrderRebateRepo extends JpaRepository<OrderRebate, String>, JpaSpecificationExecutor<OrderRebate> {

	long deleteByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(Date startTime, Date endTime);
	
	List<OrderRebate> findByMerchantOrderIdAndAvailableFlagTrue(String merchantOrderId);
	
	List<OrderRebate> findBySettlementTimeIsNullAndAvailableFlagTrue();
	
}
