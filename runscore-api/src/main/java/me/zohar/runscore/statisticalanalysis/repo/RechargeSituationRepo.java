package me.zohar.runscore.statisticalanalysis.repo;

import me.zohar.runscore.statisticalanalysis.domain.RechargeSituation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RechargeSituationRepo
		extends JpaRepository<RechargeSituation, String>, JpaSpecificationExecutor<RechargeSituation> {

	RechargeSituation findTopBy();

}
