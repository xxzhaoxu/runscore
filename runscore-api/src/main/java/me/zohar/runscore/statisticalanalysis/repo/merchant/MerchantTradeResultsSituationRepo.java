package me.zohar.runscore.statisticalanalysis.repo.merchant;

import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantTradeResultsSituation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MerchantTradeResultsSituationRepo
		extends JpaRepository<MerchantTradeResultsSituation, String>, JpaSpecificationExecutor<MerchantTradeResultsSituation> {

}
