package me.zohar.runscore.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.statisticalanalysis.domain.CashDepositBounty;

public interface CashDepositBountyRepo
		extends JpaRepository<CashDepositBounty, String>, JpaSpecificationExecutor<CashDepositBounty> {
	
	CashDepositBounty findTopBy();

}
