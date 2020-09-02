package me.zohar.runscore.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.MerchantBankCard;

public interface MerchantBankCardRepo
		extends JpaRepository<MerchantBankCard, String>, JpaSpecificationExecutor<MerchantBankCard> {

	List<MerchantBankCard> findByMerchantIdAndDeletedFlagIsFalse(String merchantId);

}
