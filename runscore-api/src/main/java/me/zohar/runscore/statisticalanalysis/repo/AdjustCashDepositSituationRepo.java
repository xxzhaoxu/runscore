package me.zohar.runscore.statisticalanalysis.repo;

import me.zohar.runscore.statisticalanalysis.domain.AdjustCashDepositSituation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdjustCashDepositSituationRepo
		extends JpaRepository<AdjustCashDepositSituation, String>, JpaSpecificationExecutor<AdjustCashDepositSituation> {

	AdjustCashDepositSituation findTopBy();

}
