package me.zohar.runscore.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.GatheringChannel;

public interface GatheringChannelRepo
		extends JpaRepository<GatheringChannel, String>, JpaSpecificationExecutor<GatheringChannel> {

	GatheringChannel findByChannelCodeAndDeletedFlagIsFalse(String channelCode);
	
	List<GatheringChannel> findByDeletedFlagIsFalse();

}
