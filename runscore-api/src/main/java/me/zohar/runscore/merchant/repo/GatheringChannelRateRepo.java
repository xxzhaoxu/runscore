package me.zohar.runscore.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.GatheringChannelRate;

public interface GatheringChannelRateRepo
		extends JpaRepository<GatheringChannelRate, String>, JpaSpecificationExecutor<GatheringChannelRate> {

	GatheringChannelRate findByChannelIdAndMerchantId(String channelId, String merchantId);

	GatheringChannelRate findByMerchantIdAndChannelChannelCode(String merchantId, String channelName);

	List<GatheringChannelRate> findByMerchantId(String merchantId);
}
