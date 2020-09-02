package me.zohar.runscore.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.Merchant;

public interface MerchantRepo extends JpaRepository<Merchant, String>, JpaSpecificationExecutor<Merchant> {

	Merchant findByMerchantNameAndDeletedFlagIsFalse(String merchantName);

	Merchant findByMerchantNumAndDeletedFlagIsFalse(String merchantNum);

	Merchant findByUserNameAndDeletedFlagIsFalse(String userName);

	List<Merchant> findByDeletedFlagIsFalse();
}
