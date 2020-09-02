package me.zohar.runscore.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.statisticalanalysis.domain.TradeSituation;

public interface TradeSituationRepo
		extends JpaRepository<TradeSituation, String>, JpaSpecificationExecutor<TradeSituation> {

	TradeSituation findTopBy();

}
