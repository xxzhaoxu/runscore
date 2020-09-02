package me.zohar.runscore.useraccount.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.AccountReceiveOrderChannel;

public interface AccountReceiveOrderChannelRepo extends JpaRepository<AccountReceiveOrderChannel, String>,
		JpaSpecificationExecutor<AccountReceiveOrderChannel> {

	List<AccountReceiveOrderChannel> findByUserAccountIdAndChannelDeletedFlagFalse(String userAccountId);

	List<AccountReceiveOrderChannel> findByUserAccountId(String userAccountId);

	AccountReceiveOrderChannel findByUserAccountIdAndChannelId(String userAccountId, String channelId);

}
