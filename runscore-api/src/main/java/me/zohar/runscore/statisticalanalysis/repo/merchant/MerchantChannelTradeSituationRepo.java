package me.zohar.runscore.statisticalanalysis.repo.merchant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantChannelTradeSituation;

public interface MerchantChannelTradeSituationRepo extends JpaRepository<MerchantChannelTradeSituation, String>,
		JpaSpecificationExecutor<MerchantChannelTradeSituation> {

	List<MerchantChannelTradeSituation> findByMerchantId(String merchantId);
}
