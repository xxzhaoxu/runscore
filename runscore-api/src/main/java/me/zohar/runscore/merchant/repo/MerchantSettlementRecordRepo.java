package me.zohar.runscore.merchant.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.MerchantSettlementRecord;

public interface MerchantSettlementRecordRepo
		extends JpaRepository<MerchantSettlementRecord, String>, JpaSpecificationExecutor<MerchantSettlementRecord> {

	long deleteByApplyTimeGreaterThanEqualAndApplyTimeLessThanEqual(Date startTime, Date endTime);
	
}
