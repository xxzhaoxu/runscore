package me.zohar.runscore.merchant.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.MerchantOrder;

public interface MerchantOrderRepo
		extends JpaRepository<MerchantOrder, String>, JpaSpecificationExecutor<MerchantOrder> {
	
	List<MerchantOrder> findByOrderStateAndReceivedAccountIdAndGatheringCodePayee(String orderState,
			String receivedAccountId, String payee);
	
	List<MerchantOrder> findByOrderState(String orderState);
	
	List<MerchantOrder> findByOrderStateAndReceivedTimeLessThan(String orderState, Date receivedTime);

	long deleteBySubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(Date startTime, Date endTime);

	List<MerchantOrder> findByOrderStateAndSubmitTimeLessThan(String orderState, Date submitTime);

	List<MerchantOrder> findTop10ByOrderStateAndSpecifiedReceivedAccountId(String orderState,
			String specifiedReceivedAccountId);

	List<MerchantOrder> findTop10ByOrderStateAndSpecifiedReceivedAccountIdIsNull(String orderState);

	List<MerchantOrder> findTop10ByOrderStateAndGatheringAmountIsLessThanEqualAndGatheringChannelChannelCodeInAndAndSpecifiedReceivedAccountIdIsNullOrderBySubmitTimeDesc(
			String orderState, Double gatheringAmount, List<String> gatheringChannelCodes);

	List<MerchantOrder> findByOrderStateInAndReceivedAccountIdOrderBySubmitTimeDesc(List<String> orderStates,
			String receivedAccountId);

	MerchantOrder findByIdAndReceivedAccountId(String id, String receivedAccountId);

	List<MerchantOrder> findTop10ByOrderStateAndGatheringAmountInAndGatheringChannelChannelCodeAndSpecifiedReceivedAccountIdIsNullOrderBySubmitTimeDesc(
			String orderState, List<Double> gatheringAmounts, String gatheringChannelCode);

	MerchantOrder findByIdAndMerchantId(String id, String merchantId);

	MerchantOrder findByOrderNo(String orderNo);

	List<MerchantOrder> findByOrderStateAndUsefulTimeLessThan(String orderState, Date usefulTime);
	
	MerchantOrder findTopByReceivedAccountIdOrderByReceivedTimeDesc(String receivedAccountId);

}
