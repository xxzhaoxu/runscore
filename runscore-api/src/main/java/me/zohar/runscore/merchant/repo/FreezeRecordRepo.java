package me.zohar.runscore.merchant.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.FreezeRecord;

public interface FreezeRecordRepo extends JpaRepository<FreezeRecord, String>, JpaSpecificationExecutor<FreezeRecord> {

	List<FreezeRecord> findByDealTimeIsNullAndUsefulTimeLessThan(Date usefulTime);

	List<FreezeRecord> findByDealTimeIsNull();

	List<FreezeRecord> findByDealTimeIsNullAndReceivedAccountId(String receivedAccountId);

	FreezeRecord findTopByMerchantOrderId(String merchantOrderId);

}
