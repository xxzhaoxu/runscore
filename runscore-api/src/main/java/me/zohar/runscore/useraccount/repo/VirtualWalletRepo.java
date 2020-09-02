package me.zohar.runscore.useraccount.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.VirtualWallet;

public interface VirtualWalletRepo
		extends JpaRepository<VirtualWallet, String>, JpaSpecificationExecutor<VirtualWallet> {

	List<VirtualWallet> findByUserAccountIdAndDeletedFlagFalse(String userAccountId);

	VirtualWallet findByIdAndUserAccountId(String id, String userAccountId);

}
