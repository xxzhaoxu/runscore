package me.zohar.runscore.statisticalanalysis.repo;

import me.zohar.runscore.statisticalanalysis.domain.WithdrawSituation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WithdrawSituationRepo
		extends JpaRepository<WithdrawSituation, String>, JpaSpecificationExecutor<WithdrawSituation> {

	WithdrawSituation findTopBy();

}
