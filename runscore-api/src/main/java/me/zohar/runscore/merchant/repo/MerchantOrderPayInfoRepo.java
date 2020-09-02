package me.zohar.runscore.merchant.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.MerchantOrderPayInfo;

public interface MerchantOrderPayInfoRepo
		extends JpaRepository<MerchantOrderPayInfo, String>, JpaSpecificationExecutor<MerchantOrderPayInfo> {

	long deleteByNoticeTimeGreaterThanEqualAndNoticeTimeLessThanEqual(Date startTime, Date endTime);

	MerchantOrderPayInfo findByMerchantOrderId(String merchantOrderId);

	List<MerchantOrderPayInfo> findByOrderNo(String orderNo);

	List<MerchantOrderPayInfo> findByNoticeStateAndMerchantOrderOrderState(String noticeState, String orderState);

}
