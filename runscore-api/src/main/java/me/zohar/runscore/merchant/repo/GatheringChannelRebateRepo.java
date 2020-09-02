package me.zohar.runscore.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.GatheringChannelRebate;

public interface GatheringChannelRebateRepo
		extends JpaRepository<GatheringChannelRebate, String>, JpaSpecificationExecutor<GatheringChannelRebate> {

	List<GatheringChannelRebate> findByChannelDeletedFlagIsFalseOrderByRebate();

	List<GatheringChannelRebate> findByChannelId(String channelId);

	GatheringChannelRebate findByChannelIdAndRebate(String channelId, Double rebate);

}
