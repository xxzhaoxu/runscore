package me.zohar.runscore.merchant.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.ActualIncomeRecord;

public interface ActualIncomeRecordRepo
		extends JpaRepository<ActualIncomeRecord, String>, JpaSpecificationExecutor<ActualIncomeRecord> {
	
	long deleteByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(Date startTime, Date endTime);

	ActualIncomeRecord findTopByMerchantOrderIdAndAvailableFlagTrue(String merchantOrderId);

	List<ActualIncomeRecord> findBySettlementTimeIsNullAndAvailableFlagTrue();

}
